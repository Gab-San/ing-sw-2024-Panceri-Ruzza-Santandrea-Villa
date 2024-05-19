package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.view.model.ViewOpponentHand;
import it.polimi.ingsw.view.model.ViewPlayArea;

import static it.polimi.ingsw.view.tui.ConsoleTextColors.*;

public class PrintOpponentUI extends PrintUI{
    private ViewOpponentHand hand;
    public PrintOpponentUI(ViewOpponentHand hand, ViewPlayArea playArea){
        super(hand, playArea);
        this.hand = hand;
    }

    @Override
    public void display(){
        System.out.println(nickname + "'s playArea, centered on ("+ printCenter.col() + "," + printCenter.row() +"): ");
        printPlayArea.printPlayArea(printCenter);

        String disconnectedText = RED_TEXT + "[DISCONNECTED]" + RESET;
        String connectedText = GREEN_TEXT + "[CONNECTED]" + RESET;
        System.out.println("\nOpponent's hand: " + (hand.isConnected() ? connectedText : disconnectedText));
        printHand.printHand(true);

        System.out.flush();
    }

}
