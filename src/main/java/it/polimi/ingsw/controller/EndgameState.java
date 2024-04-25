package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.server.VirtualClient;

public class EndgameState extends GameState{
    public EndgameState(Board board) {
        super(board);
    }

    @Override
    public void join(String nickname, VirtualClient client) throws Exception {
        throw new Exception("IMPOSSIBLE TO JOIN A GAME DURING ENDGAME STATE");
    }

    @Override
    public GameState startGame(String nickname) throws Exception {
        // TODO: restart game after the end on player request?
        return null;
    }

    @Override
    public void placeStartingCard(String nickname, boolean placeOnFront) throws Exception {
        throw new Exception("IMPOSSIBLE TO PLACE STARTING CARD DURING ENDGAME STATE");
    }

    @Override
    public GameState chooseSecretObjective(String nickname, int choice) throws Exception {
        throw new Exception("IMPOSSIBLE TO CHOOSE SECRET OBJECTIVE DURING ENDGAME STATE");
    }

    @Override
    public GameState draw(String nickname, int deck, int card) throws Exception {
        throw new Exception("IMPOSSIBLE TO DRAW DURING ENDGAME STATE");
    }

    @Override
    public void placeCard(String nickname, PlayCard card, Corner corner) throws Exception {
        throw new Exception("IMPOSSIBLE TO PLACE CARD DURING ENDGAME STATE");
    }

    private GameState nextState() throws Exception {
        //TODO: implement, network is needed
        //to send a message to the players to see if they want to connect to a new game or not
        return null;
    }
}
