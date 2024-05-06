package it.polimi.ingsw.server.Commands;

import it.polimi.ingsw.controller.BoardController;
import it.polimi.ingsw.model.enums.PlayerColor;

public class ChooseColorCmd extends GameCommand{
    private final PlayerColor color;
    public ChooseColorCmd(BoardController gameController, String nickname, PlayerColor color){
        super(gameController, nickname);
        this.color = color;
    }
    @Override
    public void execute() throws IllegalStateException {
        this.gameController.chooseYourColor(nickname, color);
    }
}
