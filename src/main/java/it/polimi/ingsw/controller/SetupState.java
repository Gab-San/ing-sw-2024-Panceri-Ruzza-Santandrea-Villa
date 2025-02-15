package it.polimi.ingsw.controller;

import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.model.exceptions.DeckException;
import it.polimi.ingsw.model.exceptions.PlayerHandException;
import it.polimi.ingsw.model.listener.remote.errors.CrashStateError;
import it.polimi.ingsw.model.listener.remote.errors.IllegalActionError;
import it.polimi.ingsw.network.VirtualClient;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The SetupState class represents the state of the game during the setup phase,
 *      where the board and the players' hands are initialized
 */
public class SetupState extends GameState{
    private final Set<String> playersWhoPlacedStartingCard;
    private final Set<String> playersWhoChoseColor;
    private final Set<String> playersWhoChoseSecretObjective;
    private static final int TURN_TIME = 122;

    /**
     * Construct a SetupState instance:
     *      set phase to SETUP and give starting cards to each player
     *      then set phase to PLACESTARTING and start the timers for the turn
     * @param board the game board.
     * @param controller the board controller.
     * @param disconnectingPlayers the list of players who are disconnecting.
     */
    public SetupState(Board board, BoardController controller, List<String> disconnectingPlayers) {
        super(board, controller, disconnectingPlayers);
        board.setGamePhase(GamePhase.SETUP);
        board.squashHistory();
        playersWhoPlacedStartingCard = new HashSet<>();
        playersWhoChoseColor = new HashSet<>();
        playersWhoChoseSecretObjective = new HashSet<>();
        try {
            giveStartingCard();
        } catch (IllegalStateException e){
            board.notifyAllListeners(new CrashStateError("all", "An error occured " +
                    "while dealing starting cards"));
            System.exit(-1);
            throw e;
        }
        board.setGamePhase(GamePhase.PLACESTARTING);
        timers.startAll(board.getPlayerAreas().keySet().stream().toList(), TURN_TIME);
    }

    /**
     * Handle players reconnecting during SetupState:
     *      if the player is already part of the game he can reconnect;
     *      if the player isn't part of the game throws IllegalStateException.
     * @throws IllegalStateException if the nickname isn't already part of the game
     */
    @Override
    public void join(String nickname, VirtualClient client) throws IllegalStateException {
        try{
            board.reconnectPlayer(nickname);
            board.subscribeClientToUpdates(nickname, client);
        }catch (IllegalStateException | IllegalArgumentException e){
            throw new IllegalStateException("IMPOSSIBLE TO JOIN A GAME DURING SETUP STATE", e);
        }
    }

    /**
     * Handle the disconnecting players during SetupState:
     *      if the player is part of the game, he disconnects and his phase action is done randomly;
     *      if the player isn't part of the game throws IllegalArgumentException.
     * @param nickname the nickname of the disconnecting player
     * @throws IllegalArgumentException if the player is not part of the game
     */

    @Override
    public void disconnect(String nickname) throws  IllegalArgumentException {

        disconnectingPlayers.remove(nickname);

        board.getPlayerByNickname(nickname); // throws IllegalArgumentException if player not in game

        board.unsubscribeClientFromUpdates(nickname);
        board.disconnectPlayer(nickname);

        timers.stopTimer(board.getPlayerByNickname(nickname));

        if(board.getNumOfConnectedPlayers() == 0) {
            //if all players disconnected, go back to creation state
            transition(new CreationState(new Board(), controller, new ArrayList<>()));
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


    /**
     * Throws an exception because setting the number of players is not allowed during SetupState
     * @param nickname the nickname of the player initiating the action
     * @param num the number of players to set
     * @throws IllegalStateException always, as this action is not allowed in this state.
     */
    @Override
    public void setNumOfPlayers(String nickname, int num) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO CHANGE THE NUMBER OF PLAYERS DURING SETUP STATE" ));
        throw new IllegalStateException("IMPOSSIBLE TO CHANGE THE NUMBER OF PLAYERS DURING SETUP STATE");
    }

    /**
     * Handles the placing of the starting card for each player:
     *      if the player has not placed his starting card yet, place it on the side specified by placeOnFront and stop his timer
     *      if all connected player has placed their starting card, place disconnected players' starting card on random side
     *          then go to CHOOSECOLOR phase and restart timers
     *      if the player is not part of the game, throws IllegalArgumentException
     *      if the phase is not PLACESTARTING, throws IllegalStateException
     * @param nickname the nickname of the player placing the card
     * @param placeOnFront true if the card should be placed on the front side, false otherwise
     * @throws IllegalStateException if placing the starting card is not allowed at this stage
     *      or the player has already placed his starting card
     * @throws IllegalArgumentException if the player is not part of the game
     */
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
            board.notifyAllListeners(new IllegalActionError(nickname, "YOU HAVE ALREADY PLACED YOUR STARTING CARD"));
            throw new IllegalStateException(nickname + " has already placed their starting card");
        }
        Player player;
        player = board.getPlayerByNickname(nickname);

        board.placeStartingCard(player, placeOnFront);

        timers.stopTimer(player);
        playersWhoPlacedStartingCard.add(nickname);
        if(playersWhoPlacedStartingCard.size() == board.getPlayerAreas().size()){
            board.setGamePhase(GamePhase.CHOOSECOLOR);
            timers.startAll(board.getPlayerAreas().keySet().stream().toList(), TURN_TIME);
            for(Player p: board.getPlayerAreas().keySet().stream().filter((p)->!p.isConnected()).collect(Collectors.toSet()))
                controller.chooseYourColor(p.getNickname(), board.getRandomAvailableColor());
        }
    }

    /**
     * Handles the choosing of the color for each player:
     *      if the player has not chosen his color yet, assign the color specified by "color" to the player and stop his timer
     *      if all connected player has chosen their color, choose disconnected players' color at random
     *          then go to DEALCARDS phase and draw the first hand,
     *          then give the secret objectives to choose from, go to CHOOSEOBJECTIVE phase and restart the timers
     *      if the player is not part of the game, throws IllegalArgumentException
     *      if the phase is not CHOOSECOLOR, throws IllegalStateException
     * @param nickname the nickname of the player choosing the color
     * @param color the color chosen by the player
     * @throws IllegalStateException if choosing color is not allowed at this stage
     *      or the player has already chosen his color
     * @throws IllegalArgumentException if the player is not part of the game
     */
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
            board.notifyAllListeners(new IllegalActionError(nickname,("You have already chosen your color").toUpperCase() ));
            throw new IllegalStateException(nickname + " has already chosen his color.");
        }

        if(!board.getAvailableColors().contains(color)) {
            board.notifyAllListeners(new IllegalActionError(nickname, (color + " not available").toUpperCase()));
            throw new IllegalStateException(color + " not available");
        }

        Player player;
        player = board.getPlayerByNickname(nickname);
        player.setColor(color);
        timers.stopTimer(player);
        playersWhoChoseColor.add(nickname);

        if(playersWhoChoseColor.size()==board.getPlayerAreas().size()) {
            board.setGamePhase(GamePhase.DEALCARDS);
            try {
                drawFirstHand();
            } catch ( DeckException e ) {
                board.notifyAllListeners(new CrashStateError("all", "An error occured " +
                        "while dealing cards at the start of the game"));
                System.exit(-1);
                throw new IllegalStateException(e);
            } catch (PlayerHandException | IllegalStateException exc){
                throw new IllegalStateException(exc);
            }

            board.revealObjectives();

            try {
                giveSecretObjectives();
            } catch (IllegalStateException e){
                board.notifyAllListeners(new CrashStateError("all", "An error occured " +
                        "while dealing objective cards"));
                System.exit(-1);
                throw e;
            }

            board.setGamePhase(GamePhase.CHOOSEOBJECTIVE);
            timers.startAll(board.getPlayerAreas().keySet().stream().toList(), TURN_TIME);
            for(Player p: board.getPlayerAreas().keySet().stream().filter((p)->!p.isConnected()).collect(Collectors.toSet()))
                controller.chooseSecretObjective(p.getNickname(), new Random().nextInt(2)+1);
        }
    }

    /**
     * Handles the choosing of the secret objective for each player:
     *      if the player has not chosen his secret objective yet, assign the secret objective specified by choice to the player and stop his timer
     *      if all connected player has chosen their secret objective, choose disconnected players' secret objective at random
     *          then go to next state
     *      if the player is not part of the game, throws IllegalArgumentException
     *      if the phase is not CHOOSEOBJECTIVE, throws IllegalStateException
     * @param nickname the nickname of the player choosing the objective
     * @param choice the choice made by the player
     * @throws IllegalStateException if choosing secret objective is not allowed at this stage
     *      or the player has already chosen his secret objective
     * @throws IllegalArgumentException if the player is not part of the game
     */
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
            board.notifyAllListeners(new IllegalActionError(nickname, ("You have already chosen your secret objective card.").toUpperCase()));
            throw new IllegalStateException(nickname + " has already chosen his secret objective card.");
        }

        Player player;
        player = board.getPlayerByNickname(nickname);

        try{
            player.getHand().chooseObjective(choice);
        }catch (PlayerHandException e){
            throw new IllegalStateException(e.getMessage());
        }catch (IndexOutOfBoundsException e){
            throw new IllegalArgumentException("Invalid choice. Must be 1 or 2, entered " + choice + " instead.");
        }
        timers.stopTimer(player);
        playersWhoChoseSecretObjective.add(nickname);

        if(playersWhoChoseSecretObjective.size() == board.getPlayerAreas().size()) {
            nextState();
        }
    }

    /**
     * Throws an exception because drawing is not allowed during SetupState
     * @param nickname the nickname of the player drawing the card
     * @param deckFrom the deck from which the card is drawn
     * @param cardPos the position on the card
     * @throws IllegalStateException always, as this action is not allowed in this state.
     */
    @Override
    public void draw(String nickname, char deckFrom, int cardPos) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO DRAW DURING SETUP STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO DRAW DURING SETUP STATE");
    }

    /**
     * Throws an exception because placing cards is not allowed during SetupState
     * @param nickname the nickname of the player placing the card
     * @param cardID the ID of the card to place
     * @param cardPos the position on the game board where the card will be placed
     * @param cornerDir the direction in which to place the card's corner
     * @param placeOnFront true if the card should be placed on the front side, false otherwise
     * @throws IllegalStateException always, as this action is not allowed in this state.
     */
    @Override
    public void placeCard(String nickname, String cardID, GamePoint cardPos, CornerDirection cornerDir, boolean placeOnFront) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO PLACE CARDS OTHER THAN THE STARTING CARD DURING SETUP STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO PLACE CARDS OTHER THAN THE STARTING CARD DURING SETUP STATE");
    }

    /**
     * Moves the controller to the next state:
     *      sets phase to CHOOSE_FIRST_PLAYER and chooses the first player randomly
     *      then changes the game state to PlayState
     */
    private void nextState() {
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

    /**
     * Assign a first hand to each player in the game composed of: 2 resource cards + 1 gold card
     * @throws PlayerHandException if drawTop fails and throws the exception
     * @throws DeckException if drawTop fails and throws the exception
     */
    private void drawFirstHand() throws PlayerHandException, DeckException {
        for (Player player : board.getPlayerAreas().keySet()) {
            board.drawTop(Board.RESOURCE_DECK, player.getHand());
            board.drawTop(Board.RESOURCE_DECK, player.getHand());
            board.drawTop(Board.GOLD_DECK, player.getHand());
        }
    }

    /**
     * Assign two secret objectives to choose from to each player
     * @throws IllegalStateException if deal fails and throws the exception
     */
    private void giveSecretObjectives() throws IllegalStateException {
        for(Player player : board.getPlayerAreas().keySet()) {
            board.deal(Board.OBJECTIVE_DECK, player.getHand());
        }
    }

    /**
     * Assign a starting card to each player
     * @throws IllegalStateException if deal fails and throws the exception
     */
    private void giveStartingCard() throws IllegalStateException{
        for(Player player : board.getPlayerAreas().keySet()) {
            board.deal(Board.STARTING_DECK, player.getHand());
        }
    }

    /**
     * Throws an exception because restarting the game is not allowed during SetupState
     * @param nickname the nickname of the player initiating the restart
     * @param numOfPlayers the number of players for the restarted game
     * @throws IllegalStateException always, as this action is not allowed in this state.
     */
    @Override
    public void restartGame(String nickname, int numOfPlayers) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO START GAME DURING SETUP STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO START GAME DURING SETUP STATE");
    }
}
