package it.polimi.ingsw.view.tui.scenes;

import it.polimi.ingsw.view.model.ViewOpponentHand;
import it.polimi.ingsw.view.model.ViewPlayArea;

import java.io.PrintWriter;

import static it.polimi.ingsw.view.tui.ConsoleTextColors.*;

/**
 * The opponentArea UI scene printer for the TUI.
 */
public class PrintOpponentUI extends PrintGameUI {
    private final ViewOpponentHand hand;

    /**
     * Constructs the Opponent UI scene
     * @param hand opponent's hand
     * @param playArea opponent's playArea
     */
    public PrintOpponentUI(ViewOpponentHand hand, ViewPlayArea playArea){
        super(hand, playArea);
        this.hand = hand;
    }

    @Override
    public void print(){
        out.println(nickname + "'s playArea, centered on ("+ printCenter.col() + "," + printCenter.row() +"): ");
        printPlayArea.printPlayArea(printCenter);

        String statusText = hand.isConnected() ? GREEN_TEXT + "[CONNECTED]" + RESET
                                                : RED_TEXT + "[DISCONNECTED]" + RESET;

        if(hand.isDeadlocked())
            statusText += RED_TEXT + " [DEADLOCKED]" + RESET;
        out.println("\nOpponent's hand: " + statusText);
        printHand.printHand(true);
    }
}
