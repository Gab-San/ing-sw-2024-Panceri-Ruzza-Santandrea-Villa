package it.polimi.ingsw.controller;

import it.polimi.ingsw.Point;
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

public class EndgameState extends GameState{

    public EndgameState(Board board, BoardController controller, List<String> disconnectingPlayers) {
        super(board, controller,disconnectingPlayers);
        board.setGamePhase(GamePhase.EVALOBJ);
        evaluateSecretObjectives();
        board.setGamePhase(GamePhase.SHOWWIN);
    }

    @Override
    public void join(String nickname, VirtualClient client) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO JOIN A GAME DURING ENDGAME STATE");
    }

    @Override
    public void disconnect(String nickname) throws IllegalStateException, IllegalArgumentException {
        disconnectingPlayers.remove(nickname);

        board.disconnectPlayer(nickname);

        int numOfConnectedPlayers = (int) board.getPlayerAreas().keySet().stream()
                .filter(Player::isConnected)
                .count();

        if(numOfConnectedPlayers == 0){
            transition(new CreationState(new Board(), controller, new ArrayList<>()));
        }
    }

    @Override
    public void setNumOfPlayers(String nickname, int num) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname,"IMPOSSIBLE TO CHANGE THE NUMBER OF PLAYERS DURING ENDGAME STATE" ));
        throw new IllegalStateException("IMPOSSIBLE TO CHANGE THE NUMBER OF PLAYERS DURING ENDGAME STATE");
    }



    @Override
    public void placeStartingCard(String nickname, boolean placeOnFront) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO PLACE STARTING CARD DURING ENDGAME STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO PLACE STARTING CARD DURING ENDGAME STATE");
    }

    public void chooseYourColor(String nickname, PlayerColor color) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO CHOOSE YOUR COLOR DURING ENDGAME STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO CHOOSE YOUR COLOR DURING ENDGAME STATE");
    }

    @Override
    public void chooseSecretObjective(String nickname, int choice) throws IllegalStateException {
        board.notifyAllListeners( new IllegalActionError(nickname, "IMPOSSIBLE TO CHOOSE SECRET OBJECTIVE DURING ENDGAME STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO CHOOSE SECRET OBJECTIVE DURING ENDGAME STATE");
    }

    @Override
    public void draw(String nickname, char deckFrom, int cardPos) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname,"IMPOSSIBLE TO DRAW DURING ENDGAME STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO DRAW DURING ENDGAME STATE");
    }

    @Override
    public void placeCard(String nickname, String cardID, Point cardPos, CornerDirection cornerDir, boolean placeOnFront) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO PLACE CARD DURING ENDGAME STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO PLACE CARD DURING ENDGAME STATE");
    }

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

        Board newBoard = new Board(this.board.getPlayerAreas().keySet().stream().toList());

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
