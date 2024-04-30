package it.polimi.ingsw.server.Commands;

import it.polimi.ingsw.controller.BoardController;

public class ChooseObjCmd extends GameCommand{
    private final int choice;
    public ChooseObjCmd(BoardController gameController, String nickname, int choice){
        super(gameController, nickname);
        this.choice = choice;
    }

    @Override
    public void execute() {
        this.gameController.chooseSecretObjective(nickname, choice);
    }
}
