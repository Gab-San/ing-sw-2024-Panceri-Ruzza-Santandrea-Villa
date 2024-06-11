package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.controller.BoardController;

/**
 * The GameCommand associated with the "restart game" command.
 */
public class RestartGameCmd extends GameCommand{
    /**
     * The new number of players required to start the game
     */
    private final int numOfPlayers;

    /**
     * Constructs the command and sets the command parameters.
     * @param gameController controller of the game for which game commands are issued
     * @param nickname unique identifier of the user issuing the command
     * @param numOfPlayers the new number of players required to start the game
     */
    public RestartGameCmd(BoardController gameController, String nickname, int numOfPlayers){
        super(gameController, nickname);
        this.numOfPlayers = numOfPlayers;
    }

    /**
     * @throws IllegalStateException if the restart game action can't be performed due to an illegal game state.
     *                           (e.g. the gamePhase is not "Show Winners phase").
     * @throws IllegalArgumentException if the new numOfPlayers is
     *                                  smaller than the current number of connected players or
     *                                  greater than the max number of allowed players
     */
    @Override
    public void execute() throws IllegalStateException, IllegalArgumentException {
        this.gameController.restartGame(nickname, numOfPlayers);
    }
}
