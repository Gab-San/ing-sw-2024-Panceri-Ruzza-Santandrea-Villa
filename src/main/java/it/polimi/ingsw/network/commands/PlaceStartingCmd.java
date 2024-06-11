package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.controller.BoardController;

/**
 * The GameCommand associated with the "place a starting card" command.
 */
public class PlaceStartingCmd extends GameCommand{
    /**
     * True if the start card will be placed front-side up
     */
    private final boolean faceUp;

    /**
     * Constructs the command and sets the command parameters.
     * @param gameController controller of the game for which game commands are issued
     * @param nickname unique identifier of the user issuing the command
     * @param faceUp true if the start card will be placed front-side up
     */
    public PlaceStartingCmd(BoardController gameController, String nickname, boolean faceUp){
        super(gameController, nickname);
        this.faceUp = faceUp;
    }

    /**
     * @throws IllegalStateException if the place action can't be performed due to an illegal game state.
     *                           (e.g. the gamePhase is not "Place Starting Card phase").
     */
    @Override
    public void execute() throws IllegalStateException {
        this.gameController.placeStartingCard(nickname, faceUp);
    }
}
