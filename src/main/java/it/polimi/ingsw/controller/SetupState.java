package it.polimi.ingsw.controller;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.controller.timer.TurnTimerController;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.PlayerColor;
import it.polimi.ingsw.model.exceptions.DeckException;
import it.polimi.ingsw.model.exceptions.PlayerHandException;
import it.polimi.ingsw.model.listener.remote.errors.IllegalActionError;
import it.polimi.ingsw.model.listener.remote.errors.IllegalGameAccessError;
import it.polimi.ingsw.model.listener.remote.errors.IllegalParameterError;
import it.polimi.ingsw.model.listener.remote.errors.IllegalStateError;
import it.polimi.ingsw.network.VirtualClient;

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
        timers = new TurnTimerController(controller);
        try {
            giveStartingCard();
        } catch (IllegalStateException e){
            //TODO add crash event
            throw e;
        }
        board.setGamePhase(GamePhase.PLACESTARTING);
        timers.startAll(board.getPlayerAreas().keySet().stream().toList(), 62);
    }

    @Override
    public void join(String nickname, VirtualClient client) throws IllegalStateException {
        try{
            board.reconnectPlayer(nickname);
            board.subscribeClientToUpdates(nickname, client);
        }catch (IllegalStateException | IllegalArgumentException e){
            throw new IllegalStateException("IMPOSSIBLE TO JOIN A GAME DURING SETUP STATE", e);
        }
    }

    @Override
    public void disconnect(String nickname)
            throws IllegalStateException, IllegalArgumentException {

        if(!board.getPlayerAreas().keySet().stream().map(Player::getNickname).toList().contains(nickname))
            throw  new IllegalArgumentException(nickname+" non fa parte della partita");

        board.disconnectPlayer(nickname);
        board.unsubscribeClientFromUpdates(nickname);
        Set<Player> connectedPlayers =board.getPlayerAreas().keySet().stream()
                // only look at players that are connected
                .filter(Player:: isConnected)
                .collect(Collectors.toSet());
        System.err.println(connectedPlayers +" è vuoto? "+connectedPlayers .isEmpty());
        if(connectedPlayers .isEmpty()) {
            //se si sono disconnessi tutti i giocatori => torno a creation state
            for(Player p : new HashSet<>(board.getPlayerAreas().keySet()))
                board.removePlayer(p.getNickname());
            transition(new CreationState(board, controller, new ArrayList<>()));
            return;
        }

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
    public void setNumOfPlayers(String nickname, int num) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO CHANGE THE NUMBER OF PLAYERS DURING SETUP STATE" ));
        throw new IllegalStateException("IMPOSSIBLE TO CHANGE THE NUMBER OF PLAYERS DURING SETUP STATE");
    }

    @Override
    public void placeStartingCard(String nickname, boolean placeOnFront)
            throws IllegalStateException, IllegalArgumentException {
        if (board.getGamePhase() != GamePhase.PLACESTARTING){
            switch (board.getGamePhase()) {
                case SETUP:
                    board.notifyAllListeners(new IllegalActionError(nickname, "MUST WAIT FOR ALL PLAYERS TO RECEIVE THEIR STARTING CARD"));
                    throw new IllegalStateException("Must wait for all players to receive their starting card");
                case CHOOSECOLOR, DEALCARDS, CHOOSEOBJECTIVE, CHOOSEFIRSTPLAYER:
                    board.notifyAllListeners(new IllegalActionError(nickname, "STARTING CARDS WERE ALREADY PLACED!"));
                    throw new IllegalStateException("Starting cards were already placed!");
                default:
                    board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO CHANGE THE NUMBER OF PLAYERS DURING SETUP STATE"));
                    throw new IllegalStateException("IMPOSSIBLE TO PLACE A STARTING CARD IN THIS PHASE"); // default should never be thrown
            }
        }

        if(playersWhoPlacedStartingCard.contains(nickname)) {
            board.notifyAllListeners(new IllegalActionError(nickname, nickname + " HAS ALREADY PLACED THEIR STARTING CARD") );
            throw new IllegalStateException(nickname + " has already placed their starting card.");
        }
        Player player;
        try {
             player = board.getPlayerByNickname(nickname);
        } catch (IllegalArgumentException argumentException){
            board.notifyAllListeners(new IllegalGameAccessError(nickname, argumentException.getMessage()));
            throw argumentException;
        }
        try {
            board.placeStartingCard(player, placeOnFront);
        } catch (IllegalStateException e){
            board.notifyAllListeners(new IllegalStateError(nickname, e.getMessage().toUpperCase()));
            throw e;
        }

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

        if(board.getGamePhase() != GamePhase.CHOOSECOLOR) {
            switch (board.getGamePhase()) {

                case SETUP, PLACESTARTING:
                    board.notifyAllListeners(new IllegalActionError(nickname, "Must wait for all players to place their starting card".toUpperCase()));
                    throw new IllegalStateException("Must wait for all players to place their starting card");
                case DEALCARDS, CHOOSEOBJECTIVE, CHOOSEFIRSTPLAYER:
                    board.notifyAllListeners(new IllegalActionError(nickname, "Colors were already chosen!".toUpperCase()));
                    throw new IllegalStateException("Colors were already chosen!");
                default:
                    board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO CHOOSE A COLOR IN THIS PHASE"));
                    throw new IllegalStateException("IMPOSSIBLE TO CHOOSE A COLOR IN THIS PHASE");
            }
        }

        if(playersWhoChoseColor.contains(nickname)) {
            board.notifyAllListeners(new IllegalActionError(nickname,(nickname + " has already chosen his color").toUpperCase() ));
            throw new IllegalStateException(nickname + " has already chosen his color.");
        }
        if(!board.getAvailableColors().contains(color)) {
            board.notifyAllListeners(new IllegalActionError(nickname, (color + " not available").toUpperCase()));
            throw new IllegalStateException(color + " not available.");
        }
        Player player;
        try {
             player = board.getPlayerByNickname(nickname);
        } catch (IllegalArgumentException argumentException){
            board.notifyAllListeners(new IllegalGameAccessError(nickname, argumentException.getMessage()));
            throw argumentException;
        }

        player.setColor(color);
        timers.stopTimer(player);

        playersWhoChoseColor.add(nickname);
        if(playersWhoChoseColor.size()==board.getPlayerAreas().size()) {
            board.setGamePhase(GamePhase.DEALCARDS);
            try {
                drawFirstHand();
            } catch ( DeckException e ) {
                //TODO: add crash event
                board.notifyAllListeners(new IllegalStateError(nickname, e.getMessage().toUpperCase()));
                throw new IllegalStateException(e);
            } catch (PlayerHandException | IllegalStateException exc){
                board.notifyAllListeners(new IllegalActionError(nickname, exc.getMessage().toUpperCase()));
                throw new IllegalStateException(exc);
            }

            board.revealObjectives();
            try {
                giveSecretObjectives();
            } catch (IllegalStateException e){
                //TODO add crash event
                throw e;
            }

            board.setGamePhase(GamePhase.CHOOSEOBJECTIVE);
            timers.startAll(board.getPlayerAreas().keySet().stream().toList(), 62);
            for(Player p: board.getPlayerAreas().keySet().stream().filter((p)->!p.isConnected()).collect(Collectors.toSet()))
                controller.chooseSecretObjective(p.getNickname(), new Random().nextInt(2)+1);
        }
    }

    @Override
    public void chooseSecretObjective(String nickname, int choice) throws IllegalStateException, IllegalArgumentException {
        if(board.getGamePhase() != GamePhase.CHOOSEOBJECTIVE)
            switch (board.getGamePhase()){
                case SETUP, PLACESTARTING, CHOOSECOLOR, DEALCARDS:
                    board.notifyAllListeners(new IllegalActionError(nickname, "Must wait until all players have received their first hand".toUpperCase()));
                    throw new IllegalStateException("Must wait until all players have received their first hand");
                case CHOOSEFIRSTPLAYER:
                    board.notifyAllListeners(new IllegalActionError(nickname, "Secret objectives were already chosen!".toUpperCase()));
                    throw new  IllegalStateException("Secret objectives were already chosen!");
                default:
                    board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO CHOOSE A SECRET OBJECTIVE CARD IN THIS PHASE"));
                    throw new  IllegalStateException("IMPOSSIBLE TO CHOOSE A SECRET OBJECTIVE CARD IN THIS PHASE");
        }

        if(playersWhoChoseSecretObjective.contains(nickname)) {
            board.notifyAllListeners(new IllegalActionError(nickname, (nickname + " has already chosen his secret objective card.").toUpperCase()));
            throw new IllegalStateException(nickname + " has already chosen his secret objective card.");
        }

        Player player;
        try {
            player = board.getPlayerByNickname(nickname);
        } catch (IllegalArgumentException argumentException){
            board.notifyAllListeners(new IllegalGameAccessError(nickname, argumentException.getMessage()));
            throw new IllegalArgumentException(argumentException.getMessage());
        }

        try{
            player.getHand().chooseObjective(choice);
        }catch (PlayerHandException e){
            //TODO check if add crash event
            board.notifyAllListeners(new IllegalStateError(nickname, e.getMessage()));
            throw new IllegalStateException(e.getMessage());
        }catch (IndexOutOfBoundsException e){
            board.notifyAllListeners(new IllegalParameterError(nickname, e.getMessage()));
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
        board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO DRAW DURING SETUP STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO DRAW DURING SETUP STATE");
    }

    @Override
    public void placeCard(String nickname, String cardID, Point cardPos, CornerDirection cornerDir, boolean placeOnFront) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO PLACE CARDS OTHER THAN THE STARTING CARD DURING SETUP STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO PLACE CARDS OTHER THAN THE STARTING CARD DURING SETUP STATE");
    }


    private void nextState() throws IllegalStateException {
        board.setGamePhase(GamePhase.CHOOSEFIRSTPLAYER);
        List<Player> players = new LinkedList<>(board.getPlayerAreas().keySet());
        final int numOfPlayers = players.size();
        Random random = new Random();
        for (int i = 1; i <= numOfPlayers; i++) {
            int randomIndex = random.nextInt(players.size());
            players.remove(randomIndex).setTurn(i); // removes from list and sets player turn
        }

        transition(new PlayState(board, controller, disconnectingPlayers));
    }


    private void drawFirstHand() throws PlayerHandException, DeckException {
        for (Player player : board.getPlayerAreas().keySet()) {
            board.drawTop(Board.RESOURCE_DECK, player.getHand());
            board.drawTop(Board.RESOURCE_DECK, player.getHand());
            board.drawTop(Board.GOLD_DECK, player.getHand());
        }
    }
    private void giveSecretObjectives() throws IllegalStateException {
        for(Player player : board.getPlayerAreas().keySet()) {
            board.deal(Board.OBJECTIVE_DECK, player.getHand());
        }
    }
    private void giveStartingCard() throws IllegalStateException{
        for(Player player : board.getPlayerAreas().keySet()) {
            board.deal(Board.STARTING_DECK, player.getHand());
        }
    }

    @Override
    public void restartGame(String nickname, int numOfPlayers) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO START GAME DURING SETUP STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO START GAME DURING SETUP STATE");
    }
}
