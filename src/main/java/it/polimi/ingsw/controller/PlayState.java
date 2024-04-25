package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.server.VirtualClient;

public class PlayState extends GameState {
    private boolean currentPlayerHasPlacedCard;
    private boolean lastRound;

    public PlayState(Board board) {
        super(board);
        currentPlayerHasPlacedCard = false;
        lastRound = false;
    }

    @Override
    public void join(String nickname, VirtualClient client) throws Exception {
        throw new Exception("IMPOSSIBLE TO JOIN A GAME DURING PLAY STATE");
    }

    @Override
    public GameState startGame(String nickname) throws Exception {
        throw new Exception("IMPOSSIBLE TO START GAME DURING PLAY STATE");
    }

    @Override
    public void placeStartingCard(String nickname, boolean placeOnFront) throws Exception {
        throw new Exception("IMPOSSIBLE TO PLACE STARTING CARD DURING PLAY STATE");
    }

    @Override
    public GameState chooseSecretObjective(String nickname, int choice) throws Exception {
        throw new Exception("IMPOSSIBLE TO CHOOSE SECRET OBJECTIVE DURING PLAY STATE");
    }

    @Override
    public GameState draw(String nickname, int deck, int card) throws Exception {
        synchronized (board){
            //TODO: this.board.draw(nickname, deck, card);
            boolean isLastPlayerTurn = board.getCurrentTurn()==board.getPlayerAreas().size();
            if(lastRound && isLastPlayerTurn)
                return nextState();
            if(board.checkEndgame() && isLastPlayerTurn)
                lastRound = true;

            board.nextTurn();
            currentPlayerHasPlacedCard = false;
            return this;
        }
    }

    @Override
    public void placeCard(String nickname, PlayCard card, Corner corner) throws Exception {
        synchronized (board) {
            if (currentPlayerHasPlacedCard)
                throw new Exception("Player had already placed a card!");

            Player player = board.getPlayerByNickname(nickname);
            board.placeCard(player, card, corner);

            currentPlayerHasPlacedCard = true;
        }
    }

    private GameState nextState() throws Exception {
        return new EndgameState(board);
    }
}
