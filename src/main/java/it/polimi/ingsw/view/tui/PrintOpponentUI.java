package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.view.model.ViewOpponentHand;
import it.polimi.ingsw.view.model.ViewPlayArea;

import static it.polimi.ingsw.view.tui.ConsoleTextColors.RED_TEXT;
import static it.polimi.ingsw.view.tui.ConsoleTextColors.RESET;

public class PrintOpponentUI extends PrintUI{
    private ViewOpponentHand hand;
    public PrintOpponentUI(ViewOpponentHand hand, ViewPlayArea playArea){
        super(hand, playArea);
        this.hand = hand;
    }

    @Override
    public void printUI(){
        System.out.println(nickname + "'s playArea, centered on ("+ printCenter.col() + "," + printCenter.row() +"): ");
        printPlayArea.printPlayArea(printCenter);

        System.out.println("\nOpponent's hand: " + (hand.isConnected() ? "" : RED_TEXT + "[DISCONNECTED]" + RESET));
        printHand.printHand(true);

        System.out.flush();
    }

}
