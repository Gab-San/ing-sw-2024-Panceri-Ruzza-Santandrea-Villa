package it.polimi.ingsw.view.tui.scenes;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.Point;
import it.polimi.ingsw.view.model.ViewHand;
import it.polimi.ingsw.view.model.ViewPlayArea;
import it.polimi.ingsw.view.Scene;
import it.polimi.ingsw.view.tui.printers.PrintHand;
import it.polimi.ingsw.view.tui.printers.PrintPlayArea;

import java.io.PrintWriter;

public abstract class PrintGameUI implements Scene {
    protected final PrintPlayArea printPlayArea;
    protected Point printCenter;
    protected final PrintHand printHand;
    protected final String nickname;
    protected final PrintWriter out;

    public PrintGameUI(ViewHand hand, ViewPlayArea playArea){
        printPlayArea = new PrintPlayArea(playArea);
        printHand = new PrintHand(hand);
        printCenter = new Point(0,0);
        this.nickname = hand.getNickname();
        out = new PrintWriter(System.out);
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
