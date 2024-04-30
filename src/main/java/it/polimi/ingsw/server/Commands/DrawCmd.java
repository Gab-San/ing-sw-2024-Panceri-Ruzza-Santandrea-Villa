package it.polimi.ingsw.server.Commands;

import it.polimi.ingsw.controller.BoardController;

public class DrawCmd extends GameCommand {
    private final char deck;
    private final int card;
    public DrawCmd(BoardController gamecontroller, String nickname, char deck, int card){
        super(gamecontroller, nickname);
        this.deck = deck;
        this.card = card;
    }
    @Override
    public void execute() {
        this.gameController.draw(nickname,deck,card);
    }
//
//    @Override
//    public void execute(){
//        switch (card){
//            case 1:
//                this.gameController.drawFirst(nickname, deck);
//                break;
//            case 2:
//                this.gameController.drawSecond(nickname, deck);
//                break;
//            default:
//                this.gameController.drawTop(nickname, deck);
//                break;
//        }
//    }
}
