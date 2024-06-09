package it.polimi.ingsw.controller;

import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.model.exceptions.PlayerHandException;
import it.polimi.ingsw.model.listener.remote.errors.IllegalActionError;
import it.polimi.ingsw.model.listener.remote.errors.IllegalParameterError;
import it.polimi.ingsw.model.listener.remote.errors.IllegalStateError;
import it.polimi.ingsw.network.VirtualClient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The EndgameState class represents the state of the game during the endgame phase,
 * where the game is evaluated and scores are calculated.
 */
public class EndgameState extends GameState{
    private static final int ENDGAME_TIME = 60*60; //1 hour

    /**
     * Constructs an EndgameState instance.
     * @param board the game board.
     * @param controller the board controller.
     * @param disconnectingPlayers the list of players who are disconnecting.
     */
    public EndgameState(Board board, BoardController controller, List<String> disconnectingPlayers) {
        super(board, controller,disconnectingPlayers);
        board.setGamePhase(GamePhase.EVALOBJ);
        board.squashHistory();
        evaluateSecretObjectives();
        board.setGamePhase(GamePhase.SHOWWIN);
        //prevent softlock if all connected players crash without explicit disconnect()
        //also prevents players from blocking the server by never leaving endgame state
        List<Player> connectedPlayers = board.getPlayerAreas().keySet().stream()
                .filter(Player::isConnected).toList();
        timers.startAll(connectedPlayers, ENDGAME_TIME);
    }

    /**
     * Throws an exception because joining a game is not allowed during the endgame state.
     * @throws IllegalStateException always, as this action is not allowed in this state.
     */
    @Override
    public void join(String nickname, VirtualClient client) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO JOIN A GAME DURING ENDGAME STATE");
    }

    /**
     * Handles player disconnection.
     * @throws IllegalStateException if disconnection cannot be handled at this stage.
     * @throws IllegalArgumentException if the nickname is invalid.
     */
    @Override
    public void disconnect(String nickname) throws IllegalStateException, IllegalArgumentException {
        disconnectingPlayers.remove(nickname);

        board.disconnectPlayer(nickname);
        board.unsubscribeClientFromUpdates(nickname);

        if(board.getNumOfConnectedPlayers() == 0){
            transition(new CreationState(new Board(), controller, new ArrayList<>()));
        }
    }

    /**
     * Throws an exception because setting the number of players is not allowed during the endgame state.
     * @throws IllegalStateException always, as this action is not allowed in this state.
     */
    @Override
    public void setNumOfPlayers(String nickname, int num) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname,"IMPOSSIBLE TO CHANGE THE NUMBER OF PLAYERS DURING ENDGAME STATE" ));
        throw new IllegalStateException("IMPOSSIBLE TO CHANGE THE NUMBER OF PLAYERS DURING ENDGAME STATE");
    }

    /**
     * Throws an exception because placing a starting card is not allowed during the endgame state.
     * @throws IllegalStateException always, as this action is not allowed in this state.
     */
    @Override
    public void placeStartingCard(String nickname, boolean placeOnFront) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO PLACE STARTING CARD DURING ENDGAME STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO PLACE STARTING CARD DURING ENDGAME STATE");
    }

    /**
     * Throws an exception because choosing a color is not allowed during the endgame state.
     * @param nickname the nickname of the player.
     * @param color the chosen color.
     * @throws IllegalStateException always, as this action is not allowed in this state.
     */
    public void chooseYourColor(String nickname, PlayerColor color) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO CHOOSE YOUR COLOR DURING ENDGAME STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO CHOOSE YOUR COLOR DURING ENDGAME STATE");
    }

    /**
     * Throws an exception because choosing a secret objective is not allowed during the endgame state.
     * @throws IllegalStateException always, as this action is not allowed in this state.
     */
    @Override
    public void chooseSecretObjective(String nickname, int choice) throws IllegalStateException {
        board.notifyAllListeners( new IllegalActionError(nickname, "IMPOSSIBLE TO CHOOSE SECRET OBJECTIVE DURING ENDGAME STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO CHOOSE SECRET OBJECTIVE DURING ENDGAME STATE");
    }

    /**
     * Throws an exception because drawing is not allowed during the endgame state.
     * @throws IllegalStateException always, as this action is not allowed in this state.
     */
    @Override
    public void draw(String nickname, char deckFrom, int cardPos) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname,"IMPOSSIBLE TO DRAW DURING ENDGAME STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO DRAW DURING ENDGAME STATE");
    }

    /**
     * Throws an exception because placing a card is not allowed during the endgame state.
     * @throws IllegalStateException always, as this action is not allowed in this state.
     */
    @Override
    public void placeCard(String nickname, String cardID, GamePoint cardPos, CornerDirection cornerDir, boolean placeOnFront) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO PLACE CARD DURING ENDGAME STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO PLACE CARD DURING ENDGAME STATE");
    }

    /**
     * Restarts the game if the current phase allows it.
     * @throws IllegalStateException if the game cannot be restarted in the current phase.
     * @throws IllegalArgumentException if the number of players is invalid.
     */
    @Override
    public void restartGame(String nickname, int numOfPlayers) throws IllegalStateException, IllegalArgumentException {
        if(board.getGamePhase()!=GamePhase.SHOWWIN) {
            board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO START A NEW GAME IN THIS PHASE"));
            throw new IllegalStateException("IMPOSSIBLE TO START A NEW GAME IN THIS PHASE");
        }

        board.getPlayerByNickname(nickname); // throws IllegalArgumentException if player isn't in game

        if(numOfPlayers < board.getPlayerAreas().size()){
            board.notifyAllListeners(new IllegalParameterError(nickname,"Can't reduce number of players on game restart.".toUpperCase()));
            throw new IllegalArgumentException("Can't reduce number of players on game restart.");
        }

        //This check returns if some players want to disconnect so that if the lock
        // on this was taken by the start game the disconnect can take the lock and compute
        if(!disconnectingPlayers.isEmpty()){
            board.notifyAllListeners(new IllegalStateError(nickname, "WAIT FOR PLAYERS TO DISCONNECT"));
            return;
        }

        for(Player p : board.getPlayerAreas().keySet().stream().filter((p)->!p.isConnected()).collect(Collectors.toSet()))
            board.removePlayer(p.getNickname());

        Board newBoard = new Board(this.board, this.board.getPlayerAreas().keySet().stream().toList());

        if(board.getPlayerAreas().size() < numOfPlayers) {
            // if not enough players are connected for the new game, go to Join State
            // the setPhase is done in the constructor
            transition( new JoinState(newBoard, controller, new ArrayList<>(), numOfPlayers) );
        }
        else{ // numOfPlayers == board.getPlayerAreas().size() , skip join state
            // the setPhase is done in the constructor
            transition( new SetupState(newBoard, controller, new ArrayList<>()) );
        }
    }

//region AUXILIARY FUNCTIONS
    /**
     * Evaluates the secret objectives of all players and updates their scores.
     * @throws IllegalStateException if there is an issue with evaluating secret objectives.
     */
    private void evaluateSecretObjectives() throws IllegalStateException{
        for(Player player : board.getPlayerAreas().keySet()){
            ObjectiveCard objCard;
            try {
                objCard = player.getHand().getSecretObjective();
            } catch (PlayerHandException e){
                //FIXME if at this point in the game the secret objective has to be set
                // if this error occurs than the application should crash
                throw new IllegalStateException(e);
            }
            objCard.turnFaceUp(); // reveal secret objective
            int points = objCard.calculatePoints(board.getPlayerAreas().get(player));
            board.addScore(player, points);
        }
    }
//endregion
}
