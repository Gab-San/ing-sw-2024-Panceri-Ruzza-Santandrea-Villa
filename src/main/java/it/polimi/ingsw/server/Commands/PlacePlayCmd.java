package it.polimi.ingsw.server.Commands;

import it.polimi.ingsw.controller.BoardController;
import it.polimi.ingsw.model.Point;
import it.polimi.ingsw.model.enums.CornerDirection;

public class PlacePlayCmd extends GameCommand{

    private final String cardID;
    private final Point placePos;
    private final CornerDirection cornerDir;

    public PlacePlayCmd(BoardController gameController, String nickname, String cardID, Point placePos, CornerDirection cornerDir){
        super(gameController, nickname);
        this.cardID = cardID;
        this.placePos = placePos;
        this.cornerDir = cornerDir;
    }

    @Override
    public void execute() {
//        this.gameController.placeCard(nickname,cardID,placePos,cornerDir);
    }
}
