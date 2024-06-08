package it.polimi.ingsw.view.tui.scenes;

import it.polimi.ingsw.view.model.ViewPlayerHand;
import it.polimi.ingsw.view.model.ViewPlayArea;

import static it.polimi.ingsw.view.tui.ConsoleTextColors.RED_TEXT;
import static it.polimi.ingsw.view.tui.ConsoleTextColors.RESET;

/**
 * The playerArea UI scene printer for the TUI.
 */
public class PrintPlayerUI extends PrintGameUI {
    /**
     * Constructs the playerArea UI printer (for 'myArea' scene)
     * @param hand local player's hand
     * @param playArea local player's playArea
     */
    public PrintPlayerUI(ViewPlayerHand hand, ViewPlayArea playArea){
        super(hand, playArea);
    }

    @Override
    public void print(){
        out.println("Your (" + nickname + "'s) playArea, centered on ("+ printCenter.col() + "," + printCenter.row() +"): ");
        printPlayArea.printPlayArea(printCenter);

        out.println("\nYour hand: ");
        printHand.printHand();
    }
}
