package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.controller.BoardController;

/**
 * The GameCommand associated with the "draw a card" command.
 */
public class DrawCmd extends GameCommand {
    /**
     * Initial of the deck from which to draw.
     */
    private final char deck;
    /**
     * Position of the card to draw <br>
     * (0 = top , 1/2 = first/second revealed)
     */
    private final int card;
    /**
     * Constructs the command and sets the command parameters.
     * @param gameController controller of the game for which game commands are issued
     * @param nickname unique identifier of the user issuing the command
     * @param deck initial of the deck from which to draw.
     * @param card position of the card to draw (0 = top , 1/2 = first/second revealed)
     */
    public DrawCmd(BoardController gameController, String nickname, char deck, int card){
        super(gameController, nickname);
        this.deck = deck;
        this.card = card;
    }

    /**
     * @throws IllegalStateException if the draw action can't be performed due to an illegal game state.
     *                       (e.g. the gamePhase is not "Draw phase").
     * @throws IllegalArgumentException if the deck or card index are invalid,
     *                                  or the deck is empty, or the player's hand is full,
     */
    @Override
    public void execute() throws IllegalStateException, IllegalArgumentException {
        this.gameController.draw(nickname,deck,card);
    }
}
