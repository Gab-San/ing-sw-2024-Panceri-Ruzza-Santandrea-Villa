package it.polimi.ingsw.network.Commands;

import it.polimi.ingsw.controller.BoardController;

public class PlaceStartingCmd extends GameCommand{

    private final boolean faceUp;

    public PlaceStartingCmd(BoardController gameController, String nickname, boolean faceUp){
        super(gameController, nickname);
        this.faceUp = faceUp;
    }
    @Override
    public void execute() throws IllegalStateException {
        this.gameController.placeStartingCard(nickname, faceUp);
    }
}
