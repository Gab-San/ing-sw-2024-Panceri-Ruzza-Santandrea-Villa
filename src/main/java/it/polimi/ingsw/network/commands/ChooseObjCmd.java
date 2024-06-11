package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.controller.BoardController;

/**
 * The GameCommand associated with a player's secret objective choice.
 */
public class ChooseObjCmd extends GameCommand{
    /**
     * The index of the player's secret objective choice
     */
    private final int choice;

    /**
     * Constructs the command and sets the command parameters.
     * @param gameController controller of the game for which game commands are issued
     * @param nickname unique identifier of the user issuing the command
     * @param choice the index of the player's secret objective choice
     */
    public ChooseObjCmd(BoardController gameController, String nickname, int choice){
        super(gameController, nickname);
        this.choice = choice;
    }
    /**
     * @throws IllegalStateException if the objective choice can't be performed due to an illegal game state.
     *                  (e.g. the choice is an invalid number, or the player had already chosen an objective,
     *                        or the gamePhase is not "Choose Your Objective").
     *
     */
    @Override
    public void execute() throws IllegalStateException {
        this.gameController.chooseSecretObjective(nickname, choice);
    }
}
