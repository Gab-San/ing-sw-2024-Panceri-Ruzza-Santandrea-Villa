package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Point;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.PlayerColor;
import it.polimi.ingsw.server.VirtualClient;

public class EndgameState extends GameState{
    public EndgameState(Board board) {
        super(board);
        board.setGamePhase(GamePhase.ESOCP);
        evaluateSecreteObjectives();
        board.setGamePhase(GamePhase.STWP);
    }

    @Override
    public GameState join(String nickname, VirtualClient client) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO JOIN A GAME DURING ENDGAME STATE");
    }

    @Override
    public GameState setNumOfPlayers(String nickname, int num) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO CHANGE THE NUMBER OF PLAYERS DURING ENDGAME STATE");
    }

    @Override
    public void placeStartingCard(String nickname, boolean placeOnFront) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO PLACE STARTING CARD DURING ENDGAME STATE");
    }
    public void chooseYourColor(String nickname, PlayerColor color) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO CHOOSE YOUR COLOR DURING ENDGAME STATE");
    }

    @Override
    public GameState chooseSecretObjective(String nickname, int choice) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO CHOOSE SECRET OBJECTIVE DURING ENDGAME STATE");
    }

    @Override
    public GameState draw(String nickname, String cardToDraw) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO DRAW DURING ENDGAME STATE");
    }

    @Override
    public void placeCard(String nickname, String cardID, Point cardPos, CornerDirection cornerDir) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO PLACE CARD DURING ENDGAME STATE");
    }

    private void evaluateSecreteObjectives(){
        for(Player player : board.getPlayerAreas().keySet()){
            ObjectiveCard objCard = player.getHand().getSecretObjective();
            objCard.flip();
            int points = objCard.calculatePoints(board.getPlayerAreas().get(player));
            board.addScore(player, points);
        }
    }
    @Override
    public GameState startGame(String gameID, int numOfPlayers) throws IllegalStateException {
        //FIXME: it's maybe needed to add a control if the player who calls startGame is in the game
        // TODO: restart game after the end on player request?
        Board newBoard = new Board(gameID, this.board.getPlayerAreas().keySet().stream().toList());

        if(numOfPlayers>board.getPlayerAreas().size()) {
            // the setPhase is done in the constructor
            return new JoinState(newBoard, numOfPlayers);
        }
        if(numOfPlayers==board.getPlayerAreas().size()) {
            // the setPhase is done in the constructor
            return new SetupState(newBoard);
        }
        throw new IllegalArgumentException();
    }
}
