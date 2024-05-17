package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.view.model.ViewPlayerHand;
import it.polimi.ingsw.view.model.ViewPlayArea;

public class PrintPlayerUI extends PrintUI {
    public PrintPlayerUI(ViewPlayerHand hand, ViewPlayArea playArea){
        super(hand, playArea);
    }

    public void printUI(){
        System.out.println("Your (" + nickname + "'s) playArea, centered on ("+ printCenter.col() + "," + printCenter.row() +"): ");
        printPlayArea.printPlayArea(printCenter);

        System.out.println("\nYour hand: ");
        printHand.printHand();

        System.out.flush();
    }
}
