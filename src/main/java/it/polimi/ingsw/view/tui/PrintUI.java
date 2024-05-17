package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.Point;
import it.polimi.ingsw.view.model.ViewHand;
import it.polimi.ingsw.view.model.ViewPlayArea;

public abstract class PrintUI implements UI_Printer {
    protected final PrintPlayArea printPlayArea;
    protected Point printCenter;
    protected final PrintHand printHand;
    protected final String nickname;

    public PrintUI(ViewHand hand, ViewPlayArea playArea){
        printPlayArea = new PrintPlayArea(playArea);
        printHand = new PrintHand(hand);
        printCenter = new Point(0,0);
        this.nickname = hand.getNickname();
    }
    @Override
    abstract public void printUI();

    @Override
    public void moveView(CornerDirection...directions){
        printCenter = printCenter.move(directions);
        printUI();
    }
    @Override
    public void setCenter(int row, int col) {
        setCenter(new Point(row, col));
    }
    public void setCenter(Point center) {
        printCenter = center;
        printUI();
    }
}
