package it.polimi.ingsw.server.Commands;

import it.polimi.ingsw.controller.BoardController;

public class DrawCmd extends GameCommand {
    private final char deck;
    private final int card;
    public DrawCmd(BoardController gameController, String nickname, char deck, int card){
        super(gameController, nickname);
        this.deck = deck;
        this.card = card;
    }
    @Override
    public void execute() throws IllegalStateException {
        this.gameController.draw(nickname,deck,card);
    }
}
