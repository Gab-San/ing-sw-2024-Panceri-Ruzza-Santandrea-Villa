package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.controller.BoardController;
import it.polimi.ingsw.PlayerColor;

public class ChooseColorCmd extends GameCommand{
    private final PlayerColor color;
    public ChooseColorCmd(BoardController gameController, String nickname, PlayerColor color){
        super(gameController, nickname);
        this.color = color;
    }
    @Override
    public void execute() throws IllegalStateException, IllegalArgumentException{

        this.gameController.chooseYourColor(nickname, color);
    }
}
