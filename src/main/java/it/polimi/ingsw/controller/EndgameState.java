package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.enums.PlayerColor;
import it.polimi.ingsw.server.VirtualClient;

public class EndgameState extends GameState{
    public EndgameState(Board board) {
        super(board);
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
    public GameState startGame(String nickname) throws IllegalStateException {
        // TODO: restart game after the end on player request?
        return null;
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
    public void placeCard(String nickname, PlayCard card, Corner corner) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO PLACE CARD DURING ENDGAME STATE");
    }

    private GameState nextState() throws IllegalStateException {
        //TODO: implement, network is needed
        //to send a message to the players to see if they want to connect to a new game or not
        return null;
    }
}
