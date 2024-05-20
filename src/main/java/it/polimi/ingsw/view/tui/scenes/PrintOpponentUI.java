package it.polimi.ingsw.view.tui.scenes;

import it.polimi.ingsw.view.model.ViewOpponentHand;
import it.polimi.ingsw.view.model.ViewPlayArea;

import java.io.PrintWriter;

import static it.polimi.ingsw.view.tui.ConsoleTextColors.*;

public class PrintOpponentUI extends PrintGameUI {
    private final ViewOpponentHand hand;
    public PrintOpponentUI(ViewOpponentHand hand, ViewPlayArea playArea){
        super(hand, playArea);
        this.hand = hand;
    }
    @Override
    public void displayError(String msg) {
        out.println(RED_TEXT + msg + RESET);
    }
    @Override
    public void display(){
        out.println(nickname + "'s playArea, centered on ("+ printCenter.col() + "," + printCenter.row() +"): ");
        printPlayArea.printPlayArea(printCenter);

        String disconnectedText = RED_TEXT + "[DISCONNECTED]" + RESET;
        String connectedText = GREEN_TEXT + "[CONNECTED]" + RESET;
        out.println("\nOpponent's hand: " + (hand.isConnected() ? connectedText : disconnectedText));
        printHand.printHand(true);
    }
}
