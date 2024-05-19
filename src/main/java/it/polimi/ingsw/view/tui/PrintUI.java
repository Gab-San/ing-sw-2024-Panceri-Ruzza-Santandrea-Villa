package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.Point;
import it.polimi.ingsw.view.model.ViewHand;
import it.polimi.ingsw.view.model.ViewPlayArea;
import it.polimi.ingsw.view.tui.printers.PrintHand;
import it.polimi.ingsw.view.tui.printers.PrintPlayArea;

public abstract class PrintUI implements Scene {
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
    abstract public void display();

    @Override
    public void moveView(CornerDirection...directions){
        printCenter = printCenter.move(directions);
        display();
    }
    @Override
    public void setCenter(int row, int col) {
        setCenter(new Point(row, col));
    }
    @Override
    public void setCenter(Point center) {
        printCenter = center;
        display();
    }
}
