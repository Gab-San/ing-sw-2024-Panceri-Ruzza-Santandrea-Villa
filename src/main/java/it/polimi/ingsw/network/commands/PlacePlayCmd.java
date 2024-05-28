package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.controller.BoardController;
import it.polimi.ingsw.CornerDirection;

public class PlacePlayCmd extends GameCommand{

    private final String cardID;
    private final Point placePos;
    private final CornerDirection cornerDir;
    private final boolean placeOnFront;

    public PlacePlayCmd(BoardController gameController, String nickname, String cardID, Point placePos, CornerDirection cornerDir, boolean placeOnFront){
        super(gameController, nickname);
        this.cardID = cardID;
        this.placePos = placePos;
        this.cornerDir = cornerDir;
        this.placeOnFront = placeOnFront;
    }

    @Override
    public void execute() throws IllegalStateException {
        this.gameController.placeCard(nickname,cardID,placePos,cornerDir,placeOnFront);
    }
}
