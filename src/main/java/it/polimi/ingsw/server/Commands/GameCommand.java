package it.polimi.ingsw.server.Commands;

import it.polimi.ingsw.controller.BoardController;
import it.polimi.ingsw.model.exceptions.DeckException;

abstract public class GameCommand{
    public final BoardController gameController;
    protected final String nickname;
    protected GameCommand(BoardController gameController, String nickname){
        this.gameController = gameController;
        this.nickname = nickname;
    }

    abstract public void execute() throws IllegalStateException, DeckException;
    public String getNickname(){
        return nickname;
    }
}
