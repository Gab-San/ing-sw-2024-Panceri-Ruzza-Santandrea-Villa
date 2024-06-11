package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.controller.BoardController;
import it.polimi.ingsw.CornerDirection;

/**
 * The GameCommand associated with the "place a playCard" command.
 */
public class PlacePlayCmd extends GameCommand{
    /**
     * The ID of the card to placed
     */
    private final String cardID;
    /**
     * The position of the placed card on which the card will be placed.
     */
    private final GamePoint placePos;
    /**
     * The direction of the corner of the placed card on which the card will be placed.
     */
    private final CornerDirection cornerDir;
    /**
     * True if the card will be placed front-side up
     */
    private final boolean placeOnFront;

    /**
     * Constructs the command and sets the command parameters.
     * @param gameController controller of the game for which game commands are issued
     * @param nickname unique identifier of the user issuing the command
     * @param placePos the position of the placed card on which the card will be placed.
     * @param cornerDir the direction of the corner of the placed card on which the card will be placed.
     * @param placeOnFront true if the card will be placed front-side up
     */
    public PlacePlayCmd(BoardController gameController, String nickname, String cardID, GamePoint placePos, CornerDirection cornerDir, boolean placeOnFront){
        super(gameController, nickname);
        this.cardID = cardID;
        this.placePos = placePos;
        this.cornerDir = cornerDir;
        this.placeOnFront = placeOnFront;
    }

    /**
     * @throws IllegalStateException if the place action can't be performed due to an illegal game state.
     *                           (e.g. the card can't be placed, or the gamePhase is not "Place Card phase").
     * @throws IllegalArgumentException if any command argument is invalid.
     */
    @Override
    public void execute() throws IllegalStateException, IllegalArgumentException {
        this.gameController.placeCard(nickname,cardID,placePos,cornerDir,placeOnFront);
    }
}
