package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.Point;
import it.polimi.ingsw.view.model.ViewPlayArea;
import it.polimi.ingsw.view.model.ViewPlayerHand;

public class PrintPlayerUI {
    private final PrintPlayArea printPlayArea;
    private Point printCenter;
    private final PrintPlayerHand printPlayerHand;
    private final String nickname;
    public PrintPlayerUI(ViewPlayerHand hand, ViewPlayArea playArea){
        printPlayArea = new PrintPlayArea(playArea);
        printPlayerHand = new PrintPlayerHand(hand);
        printCenter = new Point(0,0);
        this.nickname = hand.getNickname();
    }

    public void printUI(){
        System.out.println("Your (" + nickname + "'s) playArea, centered on ("+ printCenter.row() + "," + printCenter.col() +"): ");
        printPlayArea.printPlayArea(printCenter);

        System.out.println("\nYour (" + nickname + "'s) hand: ");
        printPlayerHand.printHand();

        System.out.flush();
    }

    public void moveView(CornerDirection ...directions){
        printCenter = printCenter.move(directions);
        printUI();
    }
    public void setCenter(int row, int col) {
        setCenter(new Point(row, col));
    }
    public void setCenter(Point center) {
        printCenter = center;
        printUI();
    }
}
