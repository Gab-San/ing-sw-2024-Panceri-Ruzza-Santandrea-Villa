package it.polimi.ingsw.view.tui.scenes;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.Point;
import it.polimi.ingsw.view.model.ViewHand;
import it.polimi.ingsw.view.model.ViewPlayArea;
import it.polimi.ingsw.view.tui.TUI_Scene;
import it.polimi.ingsw.view.tui.printers.PrintHand;
import it.polimi.ingsw.view.tui.printers.PrintPlayArea;

import java.io.PrintWriter;

import static it.polimi.ingsw.view.tui.ConsoleTextColors.RED_TEXT;
import static it.polimi.ingsw.view.tui.ConsoleTextColors.RESET;

public abstract class PrintGameUI extends TUI_Scene {
    protected final PrintPlayArea printPlayArea;
    protected Point printCenter;
    protected final PrintHand printHand;
    protected final String nickname;

    public PrintGameUI(ViewHand hand, ViewPlayArea playArea){
        super();
        printPlayArea = new PrintPlayArea(playArea);
        printHand = new PrintHand(hand);
        printCenter = new Point(0,0);
        this.nickname = hand.getNickname();
    }
    @Override
    abstract public void print();

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
