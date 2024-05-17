package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.view.model.ViewOpponentHand;
import it.polimi.ingsw.view.model.ViewPlayArea;

public class PrintOpponentUI extends PrintUI{
    public PrintOpponentUI(ViewOpponentHand hand, ViewPlayArea playArea){
        super(hand, playArea);
    }

    @Override
    public void printUI(){
        System.out.println(nickname + "'s playArea, centered on ("+ printCenter.col() + "," + printCenter.row() +"): ");
        printPlayArea.printPlayArea(printCenter);

        System.out.println("\nOpponent's hand: ");
        printHand.printHand(true);

        System.out.flush();
    }

}
