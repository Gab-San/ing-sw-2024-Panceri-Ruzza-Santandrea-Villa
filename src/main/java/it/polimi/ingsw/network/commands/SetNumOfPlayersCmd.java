package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.controller.BoardController;

/**
 * The GameCommand associated with the "set num of players" command.
 */
public class SetNumOfPlayersCmd extends GameCommand{
    /**
     * The number of players required to start the game
     */
    private final int numOfPlayers;
    /**
     * Constructs the command and sets the command parameters.
     * @param gameController controller of the game for which game commands are issued
     * @param nickname unique identifier of the user issuing the command
     * @param num the number of players required to start the game
     */
    public SetNumOfPlayersCmd(BoardController gameController, String nickname, int num){
        super(gameController, nickname);
        numOfPlayers = num;
    }
    /**
     * @throws IllegalStateException if the set num of players action can't be performed due to an illegal game state.
     *                           (e.g. the gamePhase is not "Set Num of Players phase").
     * @throws IllegalArgumentException if the number of players is invalid (not 2, 3 or 4)
     */
    @Override
    public void execute() throws IllegalArgumentException{
        this.gameController.setNumOfPlayers(nickname, numOfPlayers);
    }
}
