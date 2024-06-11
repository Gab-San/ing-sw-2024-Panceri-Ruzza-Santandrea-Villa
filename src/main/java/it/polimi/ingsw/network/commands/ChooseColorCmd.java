package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.controller.BoardController;
import it.polimi.ingsw.PlayerColor;

/**
 * The GameCommand associated with a player's color choice.
 */
public class ChooseColorCmd extends GameCommand{
    /**
     * The player's color choice
     */
    private final PlayerColor color;

    /**
     * Constructs the command and sets the command parameters.
     * @param gameController controller of the game for which game commands are issued
     * @param nickname unique identifier of the user issuing the command
     * @param color the player's color choice
     */
    public ChooseColorCmd(BoardController gameController, String nickname, PlayerColor color){
        super(gameController, nickname);
        this.color = color;
    }
    /**
     * @throws IllegalStateException if the color choice can't be performed due to an illegal game state.
     *                  (e.g. the color is not available, or the player had already chosen a color,
     *                        or the gamePhase is not "Choose Your Color").
     */
    @Override
    public void execute() throws IllegalStateException{
        this.gameController.chooseYourColor(nickname, color);
    }
}
