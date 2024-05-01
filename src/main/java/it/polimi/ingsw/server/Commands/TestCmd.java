package it.polimi.ingsw.server.Commands;

import it.polimi.ingsw.controller.BoardController;

public class TestCmd extends GameCommand {
    private final String msg;
    protected TestCmd(BoardController gameController, String nickname, String msg) {
        super(gameController, nickname);
        this.msg = msg;
    }

    @Override
    public void execute() {
        gameController.testPrint(msg);
    }
}
