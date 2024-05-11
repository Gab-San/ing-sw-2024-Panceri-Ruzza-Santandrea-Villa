package it.polimi.ingsw.controller;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;

import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.PlayerColor;
import it.polimi.ingsw.server.VirtualClient;

public class EndgameState extends GameState{
    public EndgameState(Board board) {
        super(board);
        board.setGamePhase(GamePhase.ESOCP);
        evaluateSecretObjectives();
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
    public GameState draw(String nickname, char deckFrom, int cardPos) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO DRAW DURING ENDGAME STATE");
    }

    @Override
    public GameState placeCard(String nickname, String cardID, Point cardPos, CornerDirection cornerDir, boolean placeOnFront) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO PLACE CARD DURING ENDGAME STATE");
    }

    private void evaluateSecretObjectives() throws IllegalStateException{
        if(board.getGamePhase()!=GamePhase.ESOCP)
            throw new IllegalStateException("IMPOSSIBLE EVALUATE SECRET OBJECTIVES IN THIS PHASE");
        for(Player player : board.getPlayerAreas().keySet()){
            ObjectiveCard objCard = player.getHand().getSecretObjective();
            objCard.turnFaceUp(); // reveal secret objective
            int points = objCard.calculatePoints(board.getPlayerAreas().get(player));
            board.addScore(player, points);
        }
    }
    @Override
    public GameState startGame(String nickname, int numOfPlayers) throws IllegalStateException, IllegalArgumentException {
        if(board.getGamePhase()!=GamePhase.STWP)
            throw new IllegalStateException("IMPOSSIBLE TO START A NEW GAME IN THIS PHASE");
        board.getPlayerByNickname(nickname); // throws IllegalArgumentException if player isn't in game
        if(numOfPlayers < board.getPlayerAreas().size()){
            throw new IllegalArgumentException("Can't reduce number of players on game restart.");
        }

        Board newBoard = new Board(this.board.getGameInfo().getGameID() , this.board.getPlayerAreas().keySet().stream().toList());

        if(board.getPlayerAreas().size() < numOfPlayers) {
            // if not enough players are connected for the new game, go to Join State
            // the setPhase is done in the constructor
            return new JoinState(newBoard, numOfPlayers);
        }
        else{ // numOfPlayers == board.getPlayerAreas().size() , skip join state
            // the setPhase is done in the constructor
            return new SetupState(newBoard);
        }

    }
}
