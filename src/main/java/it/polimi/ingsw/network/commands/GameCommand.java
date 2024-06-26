package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.controller.BoardController;
import it.polimi.ingsw.model.exceptions.DeckException;


/**
 * This class provides the basic interface for game commands.
 *
 * <p>
 *     Game commands are the set of commands used
 *     to control the logic of the game.
 * </p>
 */
abstract public class GameCommand{
    /**
     * Board controller instance on which to apply command.
     */
    public final BoardController gameController;
    /**
     * Player who issued command unique identifier.
     */
    protected final String nickname;

    /**
     * Base constructor for GameCommands
     *
     * @param gameController controller of the game for which game commands are issued
     * @param nickname unique identifier of the user issuing the command
     */
    protected GameCommand(BoardController gameController, String nickname){
        this.gameController = gameController;
        this.nickname = nickname;
    }

    /**
     * This function executes the command onto the controller.
     * @throws IllegalStateException if an illegal state is reached in the execution of the game; details may vary based on the executing function
     * @throws IllegalArgumentException if an argument is not correct or is cause of an illegal execution; details may very based on the executing function
     * @throws DeckException specific of actions dealing with the decks
     */
    abstract public void execute()
            throws IllegalStateException, IllegalArgumentException, DeckException;

    /**
     * Returns the unique identifier of the user issuing the command
     * @return the issuing player's nickname
     */
    public String getNickname() {
        return nickname;
    }
}
