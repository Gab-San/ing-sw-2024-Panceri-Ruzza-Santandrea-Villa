package it.polimi.ingsw.view.tui.scenes;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.view.model.ViewHand;
import it.polimi.ingsw.view.model.ViewPlayArea;
import it.polimi.ingsw.view.tui.TUI_Scene;
import it.polimi.ingsw.view.tui.printers.PrintHand;
import it.polimi.ingsw.view.tui.printers.PrintPlayArea;

import java.util.List;

/**
 * The generic PlayerArea UI scene printer for the TUI.
 * Implements the common methods of myAreaUI and opponentAreaUI
 * such as moveView and setCenter.
 */
public abstract class PrintGameUI extends TUI_Scene {
    //DOCS add attribute doc
    protected final PrintPlayArea printPlayArea;
    protected GamePoint printCenter;
    protected final PrintHand printHand;
    protected final String nickname;

    /**
     * Constructs the PlayerArea UI scene
     * @param hand hand of the player associated to this scene
     * @param playArea playArea of the player associated to this scene
     */
    protected PrintGameUI(ViewHand hand, ViewPlayArea playArea){
        super();
        printPlayArea = new PrintPlayArea(playArea);
        printHand = new PrintHand(hand);
        printCenter = new GamePoint(0,0);
        this.nickname = hand.getNickname();
    }
    @Override
    abstract public void print();

    @Override
    public void moveView(List<CornerDirection> directions){
        printCenter = printCenter.move(directions);
        display();
    }
    @Override
    public void setCenter(int row, int col) {
        setCenter(new GamePoint(row, col));
    }
    @Override
    public void setCenter(GamePoint center) {
        printCenter = center;
        display();
    }
}
