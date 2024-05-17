package it.polimi.ingsw.controller;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.controller.timer.TurnTimerController;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.PlayerColor;
import it.polimi.ingsw.model.exceptions.DeckException;
import it.polimi.ingsw.model.exceptions.PlayerHandException;
import it.polimi.ingsw.server.VirtualClient;

import java.util.*;
import java.util.stream.Collectors;

public class SetupState extends GameState{
    public Set<String> playersWhoPlacedStartingCard;
    public Set<String> playersWhoChoseColor;
    public Set<String> playersWhoChoseSecretObjective;

    private final TurnTimerController timers;
    public SetupState(Board board, BoardController controller, List<String> disconnectingPlayers) {
        super(board, controller, disconnectingPlayers);
        board.setGamePhase(GamePhase.SETUP);
        playersWhoPlacedStartingCard = new HashSet<>();
        playersWhoChoseColor=new HashSet<>();
        playersWhoChoseSecretObjective=new HashSet<>();
        timers=new TurnTimerController(controller);
        giveStartingCard();
        board.setGamePhase(GamePhase.PLACESTARTING);
        timers.startAll(board.getPlayerAreas().keySet().stream().toList(), 62);
    }

    @Override
    public void join(String nickname, VirtualClient client) throws IllegalStateException {
        try{
            board.reconnectPlayer(nickname);
            //TODO resubscribe player's client to observers
            //   and push current state to client (possibly done in board.replaceClient())
        }catch (IllegalStateException e){
            throw new IllegalStateException("IMPOSSIBLE TO JOIN A GAME DURING SETUP STATE", e);
        }
    }

    @Override
    public void setNumOfPlayers(String nickname, int num) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO CHANGE THE NUMBER OF PLAYERS DURING SETUP STATE");
    }

    @Override
    public void disconnect(String nickname)
            throws IllegalStateException, IllegalArgumentException {

        if(!board.getPlayerAreas().keySet().stream().map(Player::getNickname).toList().contains(nickname))
            throw  new IllegalArgumentException(nickname+" non fa parte della partita");
        //TODO handle disconnection.
        //    Disconnected player completes setup randomly.
        board.disconnectPlayer(nickname);
        //TODO unsubscribe player's client from observers
        //   and push current state to client (possibly done in board.replaceClient())
        Set<Player> playerConnessi=board.getPlayerAreas().keySet().stream()
                // only look at players that are connected
                .filter(Player:: isConnected)
                .collect(Collectors.toSet());
        System.err.println(playerConnessi+" è vuoto? "+playerConnessi.isEmpty());
        if(playerConnessi.isEmpty()) {
            //se si sono disconnessi tutti i giocatori => torno a creation state
            for(Player p : new HashSet<>(board.getPlayerAreas().keySet()))
                board.removePlayer(p.getNickname());
            transition(new CreationState(board, controller, new ArrayList<>()));
            return;
        }

        //Player player=board.getPlayerByNickname(nickname);
        if(board.getGamePhase()==GamePhase.PLACESTARTING)
            if(!playersWhoPlacedStartingCard.contains(nickname))
                controller.placeStartingCard(nickname, new Random().nextBoolean());
        if(board.getGamePhase()==GamePhase.CHOOSECOLOR)
            if(!playersWhoChoseColor.contains(nickname))
                controller.chooseYourColor(nickname, board.getRandomAvailableColor());
        if(board.getGamePhase()==GamePhase.CHOOSEOBJECTIVE)
            if(!playersWhoChoseSecretObjective.contains(nickname))
                controller.chooseSecretObjective(nickname, new Random().nextInt(2)+1);
    }

    @Override
    public void placeStartingCard(String nickname, boolean placeOnFront)
            throws IllegalStateException, IllegalArgumentException {
        if(board.getGamePhase() != GamePhase.PLACESTARTING)
            throw switch (board.getGamePhase()){
                default -> new IllegalStateException("IMPOSSIBLE TO PLACE A STARTING CARD IN THIS PHASE"); // default should never be thrown
                case SETUP ->
                        new IllegalStateException("Must wait for all players to receive their starting card");
                case CHOOSECOLOR, DEALCARDS, CHOOSEOBJECTIVE, CHOOSEFIRSTPLAYER ->
                        new IllegalStateException("Starting cards were already placed!");
            };
        if(playersWhoPlacedStartingCard.contains(nickname))
            throw new IllegalStateException(nickname + " has already placed their starting card.");

        Player player = board.getPlayerByNickname(nickname);
        board.placeStartingCard(player, placeOnFront);
        timers.stopTimer(player);

        playersWhoPlacedStartingCard.add(nickname);
        if(playersWhoPlacedStartingCard.size() == board.getPlayerAreas().size()){
            board.setGamePhase(GamePhase.CHOOSECOLOR);
            timers.startAll(board.getPlayerAreas().keySet().stream().toList(), 62);
            for(Player p: board.getPlayerAreas().keySet().stream().filter((p)->!p.isConnected()).collect(Collectors.toSet()))
                controller.chooseYourColor(p.getNickname(), board.getRandomAvailableColor());
        }
    }
    @Override
    public void chooseYourColor(String nickname, PlayerColor color)
            throws IllegalStateException, IllegalArgumentException {
        //Controls on gamePhase
        if(board.getGamePhase() != GamePhase.CHOOSECOLOR)
            throw switch (board.getGamePhase()){
                default -> new IllegalStateException("IMPOSSIBLE TO CHOOSE A COLOR IN THIS PHASE"); // default should never be thrown
                case SETUP, PLACESTARTING -> new IllegalStateException("Must wait for all players to place their starting card");
                case DEALCARDS, CHOOSEOBJECTIVE, CHOOSEFIRSTPLAYER -> new IllegalStateException("Colors were already chosen!");
            };

        if(playersWhoChoseColor.contains(nickname))
            throw new IllegalStateException(nickname + " has already chosen his color.");
        if(!board.getAvailableColors().contains(color))
            throw new IllegalStateException(color + " not available.");

        Player player = board.getPlayerByNickname(nickname);
        player.setColor(color);
        timers.stopTimer(player);

        playersWhoChoseColor.add(nickname);
        if(playersWhoChoseColor.size()==board.getPlayerAreas().size()) {
            board.setGamePhase(GamePhase.DEALCARDS);
            drawFirstHand();
            board.revealObjectives();
            giveSecretObjectives();
            board.setGamePhase(GamePhase.CHOOSEOBJECTIVE);
            timers.startAll(board.getPlayerAreas().keySet().stream().toList(), 62);
            System.err.println();
            for(Player p: board.getPlayerAreas().keySet().stream().filter((p)->!p.isConnected()).collect(Collectors.toSet()))
                controller.chooseSecretObjective(p.getNickname(), new Random().nextInt(2)+1);
        }
    }

    @Override
    public void chooseSecretObjective(String nickname, int choice) throws IllegalStateException, IllegalArgumentException {
        if(board.getGamePhase() != GamePhase.CHOOSEOBJECTIVE)
            throw switch (board.getGamePhase()){
                default -> new IllegalStateException("IMPOSSIBLE TO CHOOSE A SECRET OBJECTIVE CARD IN THIS PHASE");
                case SETUP, PLACESTARTING, CHOOSECOLOR, DEALCARDS ->
                        new IllegalStateException("Must wait until all players have received their first hand");
                case CHOOSEFIRSTPLAYER ->
                        new IllegalStateException("Secret objectives were already chosen!");
            };
        if(playersWhoChoseSecretObjective.contains(nickname))
            throw new IllegalStateException(nickname + " has already chosen his secret objective card.");

        Player player = board.getPlayerByNickname(nickname); // throws IllegalArgumentException on player not in game
        try{
            player.getHand().chooseObjective(choice);
        }catch (PlayerHandException e){
            throw new IllegalStateException(e.getMessage()); // should never be thrown given the above checks
        }catch (IndexOutOfBoundsException e){
            throw new IllegalArgumentException("Invalid choice. Must be 1 or 2, entered " + choice + " instead.");
        }
        timers.stopTimer(player);
        playersWhoChoseSecretObjective.add(nickname);

        if(playersWhoChoseSecretObjective.size() == board.getPlayerAreas().size()) {
            nextState();
        }
    }

    @Override
    public void draw(String nickname, char deckFrom, int cardPos) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO DRAW DURING SETUP STATE");
    }

    @Override
    public void placeCard(String nickname, String cardID, Point cardPos, CornerDirection cornerDir, boolean placeOnFront) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO PLACE CARDS OTHER THAN THE STARTING CARD DURING SETUP STATE");
    }


    private void nextState() throws IllegalStateException {
        board.setGamePhase(GamePhase.CHOOSEFIRSTPLAYER);
        board.setCurrentTurn(1);
        List<Player> players = new LinkedList<>(board.getPlayerAreas().keySet());
        final int numOfPlayers = players.size();
        Random random = new Random();
        for (int i = 1; i <= numOfPlayers; i++) {
            int randomIndex = random.nextInt(players.size());
            players.remove(randomIndex).setTurn(i); // removes from list and sets player turn
        }

        transition(new PlayState(board, controller, disconnectingPlayers));
    }

    private void drawFirstHand(){
        try {
            for (Player player : board.getPlayerAreas().keySet()) {
                board.drawTop(Board.RESOURCE_DECK, player.getHand());
                board.drawTop(Board.RESOURCE_DECK, player.getHand());
                board.drawTop(Board.GOLD_DECK, player.getHand());
            }
        }catch (DeckException e){
            /*TODO: handling exception*/
            System.err.println("ERROR WHILE DEALING FIRST CARDS FOR THE FIRST TIME");
        }
    }
    private void giveSecretObjectives(){
        for(Player player : board.getPlayerAreas().keySet()) {
            board.deal(Board.OBJECTIVE_DECK, player.getHand());
        }
    }
    private void giveStartingCard(){
        for(Player player : board.getPlayerAreas().keySet()) {
            board.deal(Board.STARTING_DECK, player.getHand());
        }
    }

    @Override
    public void restartGame(String nickname, int numOfPlayers) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO START GAME DURING SETUP STATE");
    }
}
