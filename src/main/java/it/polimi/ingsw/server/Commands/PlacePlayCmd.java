package it.polimi.ingsw.server.Commands;

import it.polimi.ingsw.controller.BoardController;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.PlayCard;

public class PlacePlayCmd extends GameCommand{

    private final PlayCard card;
    private final Corner corner;

    public PlacePlayCmd(BoardController gameController, String nickname, PlayCard card, Corner corner){
        super(gameController, nickname);
        this.card = card;
        this.corner = corner;
    }

    @Override
    public void execute() {
        this.gameController.placeCard(nickname,card,corner);
    }
}
