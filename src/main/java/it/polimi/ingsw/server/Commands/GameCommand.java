package it.polimi.ingsw.server.Commands;

import it.polimi.ingsw.controller.BoardController;

abstract public class GameCommand implements ServerCommand{
    public final BoardController gameController;
    protected final String nickname;
    protected GameCommand(BoardController gameController, String nickname){
        this.gameController = gameController;
        this.nickname = nickname;
    }

    abstract public void execute();
}
