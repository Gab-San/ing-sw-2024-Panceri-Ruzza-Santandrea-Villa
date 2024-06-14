package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.network.CommandPassthrough;
import it.polimi.ingsw.view.SceneID;
import it.polimi.ingsw.view.SceneManager;
import it.polimi.ingsw.view.ViewController;
import it.polimi.ingsw.view.events.commands.DisplayFlippedCard;
import it.polimi.ingsw.view.exceptions.DisconnectException;
import it.polimi.ingsw.view.model.ViewBoard;
import it.polimi.ingsw.view.model.cards.ViewPlayCard;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static it.polimi.ingsw.CornerDirection.*;

/**
 * Parser for all command inputs on the TUI. <br>
 * Delegates all server-related commands to Parser.
 */
public class TUIParser {
    private final Parser serverParser;
    private final TUI view;
    private final ViewBoard board;

    /**
     * Builds the TUI command parser
     * @param serverProxy proxy on which to call commands that perform an action on the server
     * @param view reference to the TUI
     * @param board reference to the ViewModel's board
     */
    public TUIParser(CommandPassthrough serverProxy, TUI view, ViewBoard board) {
        this.serverParser = new Parser(serverProxy, new ViewController(board));
        this.view = view;
        this.board = board;
    }

    /**
     * Parses the given command and executes it if it's a command only related to the view. <br>
     * Delegates parsing and execution to Parser if it's a command to perform an action on the server.
     * @param command the user input to be parsed as a command
     * @throws IllegalArgumentException if the command is invalid or formatted incorrectly
     * @throws IllegalStateException if the command cannot be executed at the time of parsing
     * @throws RemoteException if a connection error occurs while Parser communicates with the server
     * @throws DisconnectException if the command is quit/disconnect. This is to notify the TUI of the disconnection.
     *                          The disconnection is first sent to the server, then the exception is thrown.
     */
    public void parseCommand(String command) throws IllegalArgumentException, IllegalStateException, RemoteException, DisconnectException {
        List<String> commandComponents = Arrays.stream(command.trim().split("\\s+")).distinct().toList();
        String keyCommand = "";
        if(!commandComponents.isEmpty())
            keyCommand = commandComponents.get(0).toLowerCase();

        List<String> cmdArgs = commandComponents.stream().distinct().skip(1).toList();

        switch (keyCommand) {
            case "move":
                parseMoveCommand(cmdArgs);
                break;
            case "view", "goto":
                parseViewCommand(cmdArgs);
                break;
            case "flip", "turn", "turnover":
                parseFlipCommand(cmdArgs);
                break;
            case "help", "legend":
                viewHelperScene();
                break;
            default:
                serverParser.parseCommand(command);
        }

        if(command.toLowerCase().matches("quit|disconnect")){
            throw new DisconnectException();
        }
        if(keyCommand.toLowerCase().matches("restart") && board.getGamePhase() == GamePhase.SHOWWIN){
            SceneManager.getInstance().setScene(SceneID.getMyAreaSceneID());
            //go back to myAreaScene on game restart
        }
    }

    /**
     * Changes scene to the Helper scene.
     */
    private void viewHelperScene() {
        SceneManager.getInstance().setScene(SceneID.getHelperSceneID());
    }

    /**
     * Parses the move command to move the center of the playArea visualisation in a GameUI scene.
     * @param cmdArgs the command arguments
     * @throws IllegalArgumentException if nor a direction nor "center" is given as a command parameter
     */
    private void parseMoveCommand(List<String> cmdArgs) throws IllegalArgumentException {
        if(cmdArgs.isEmpty()) throw new IllegalArgumentException("Must provide a direction to move towards!");
        List<CornerDirection> directions = new LinkedList<>();
        for(String arg : cmdArgs) {
            switch (arg.toLowerCase()){
                case "top", "up", "north", "above", "upward", "northward":
                    directions.add(TL);
                    directions.add(TR);
                    break;
                case "bottom", "down", "south", "below", "downward", "southward":
                    directions.add(BL);
                    directions.add(BR);
                    break;
                case "left", "leftward", "west", "westward":
                    directions.add(TL);
                    directions.add(BL);
                    break;
                case "right", "rightward", "east", "eastward":
                    directions.add(TR);
                    directions.add(BR);
                    break;
                case "center", "zero", "middle":
                    view.setCenter(0,0);
                    break;
                default: throw new IllegalArgumentException("Invalid direction!");
            }
        }
        view.moveView(directions);
    }
    /**
     * Parses the view command to change the current scene.
     * @param cmdArgs the command arguments
     * @throws IllegalArgumentException if the command parameter is not a valid scene ID
     * @throws IllegalStateException if the Gamephase is SHOW_WINNERS (prevents leaving endgame leaderboard scene)
     */
    private void parseViewCommand(List<String> cmdArgs) throws IllegalArgumentException,IllegalStateException {
        if(cmdArgs.isEmpty()) throw new IllegalArgumentException("Must provide an index or cardID to flip!");
        if(board.getGamePhase() == GamePhase.SHOWWIN){
            throw new IllegalStateException("Cannot change scene after the game has ended.");
        }

        SceneID selectedScene =
            switch (cmdArgs.get(0).toLowerCase()) {
                case "me" -> SceneID.getMyAreaSceneID();
                case "board" -> SceneID.getBoardSceneID();
                case "help", "legend" -> SceneID.getHelperSceneID();
                default -> SceneID.getOpponentAreaSceneID(cmdArgs.get(0));
            };
        
        SceneManager.getInstance().setScene(selectedScene);
    }

    /**
     * Parses flip command to flip a card in hand
     * @param cmdArgs command arguments
     * @throws IllegalArgumentException if the arguments are empty
     * or if the argument is an invalid index or cardID (not corresponding to a card in hand)
     */
    private void parseFlipCommand(List<String> cmdArgs) throws IllegalArgumentException {
        if(cmdArgs.isEmpty()) throw new IllegalArgumentException("Must provide an index or cardID to flip!");

        String arg = cmdArgs.get(0).toUpperCase();
        ViewPlayCard flippedCard = null;
        if(arg.matches("\\d")) {
            try {
                int index = Integer.parseInt(cmdArgs.get(0));
                board.getPlayerHand().flipCard(index);
                flippedCard = board.getPlayerHand().getCard(index);
            }catch (IndexOutOfBoundsException | NumberFormatException e){
                throw new IllegalArgumentException("Invalid flip index provided.");
            }
        }
        else{
            board.getPlayerHand().flipCard(arg); //throws if card not in hand
            board.getPlayerHand().getCardByID(arg);
        }

        view.update(SceneID.getMyAreaSceneID(),
                new DisplayFlippedCard(flippedCard));
    }

    /**
     * Sets the local player's playArea in the ViewController. <br>
     * This method provides access to the TUI.
     */
    void setSelfPlayerArea() {
        serverParser.setSelfPlayerArea();
    }
}
