package it.polimi.ingsw.server.Commands;

import it.polimi.ingsw.controller.BoardController;
import it.polimi.ingsw.model.cards.StartingCard;

public class StartGameCmd extends GameCommand{

    public StartGameCmd(BoardController gameController, String nickname){
        super(gameController, nickname);
    }

    @Override
    public void execute() {
        this.gameController.startGame(nickname);
    }
}
