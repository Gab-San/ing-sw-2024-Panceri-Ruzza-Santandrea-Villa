package it.polimi.ingsw.controller;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.PlayerColor;
import it.polimi.ingsw.server.VirtualClient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EndgameState extends GameState{
    public EndgameState(Board board, BoardController controller, List<String> disconnectingPlayers) {
        super(board, controller,disconnectingPlayers);
        evaluateSecretObjectives();
        board.setGamePhase(GamePhase.SHOWWIN);
        //FIXME There's nothing to show the winner. Is this all implemented in the view?
        // [Ale] Yes. As a consequence of the gamephase SHOWWIN
    }

    @Override
    public void join(String nickname, VirtualClient client) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO JOIN A GAME DURING ENDGAME STATE");
    }

    @Override
    public void setNumOfPlayers(String nickname, int num) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO CHANGE THE NUMBER OF PLAYERS DURING ENDGAME STATE");
    }

    @Override
    public void disconnect(String nickname, VirtualClient client) throws IllegalStateException, IllegalArgumentException {
        board.disconnectPlayer(nickname);
        //TODO unsubscribe player's client from observers
        //   and push current state to client (possibly done in board.replaceClient())

        int numOfConnectedPlayers = (int) board.getPlayerAreas().keySet().stream()
                .filter(Player::isConnected)
                .count();
        if(numOfConnectedPlayers == 0){
            String ID = board.getGameInfo().getGameID();
            transition(new CreationState(new Board(ID), controller, new ArrayList<>()));
        }
    }

    @Override
    public void placeStartingCard(String nickname, boolean placeOnFront) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO PLACE STARTING CARD DURING ENDGAME STATE");
    }

    public void chooseYourColor(String nickname, PlayerColor color) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO CHOOSE YOUR COLOR DURING ENDGAME STATE");
    }

    @Override
    public void chooseSecretObjective(String nickname, int choice) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO CHOOSE SECRET OBJECTIVE DURING ENDGAME STATE");
    }

    @Override
    public void draw(String nickname, char deckFrom, int cardPos) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO DRAW DURING ENDGAME STATE");
    }

    @Override
    public void placeCard(String nickname, String cardID, Point cardPos, CornerDirection cornerDir, boolean placeOnFront) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO PLACE CARD DURING ENDGAME STATE");
    }

    private void evaluateSecretObjectives() throws IllegalStateException{
        for(Player player : board.getPlayerAreas().keySet()){
            ObjectiveCard objCard = player.getHand().getSecretObjective();
            objCard.turnFaceUp(); // reveal secret objective
            int points = objCard.calculatePoints(board.getPlayerAreas().get(player));
            board.addScore(player, points);
        }
    }
    @Override
    public void startGame(String nickname, int numOfPlayers) throws IllegalStateException, IllegalArgumentException {
        if(board.getGamePhase()!=GamePhase.SHOWWIN)
            throw new IllegalStateException("IMPOSSIBLE TO START A NEW GAME IN THIS PHASE");
        board.getPlayerByNickname(nickname); // throws IllegalArgumentException if player isn't in game
        if(numOfPlayers < board.getPlayerAreas().size()){
            throw new IllegalArgumentException("Can't reduce number of players on game restart.");
        }

        //TODO: must be checked
        for(Player p : board.getPlayerAreas().keySet().stream().filter((p)->!p.isConnected()).collect(Collectors.toSet()))
            board.removePlayer(p.getNickname());

        Board newBoard = new Board(this.board.getGameInfo().getGameID() , this.board.getPlayerAreas().keySet().stream().toList());

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
}
