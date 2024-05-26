package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.network.CommandPassthrough;
import it.polimi.ingsw.network.Parser;
import it.polimi.ingsw.view.SceneID;
import it.polimi.ingsw.view.model.ViewBoard;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import static it.polimi.ingsw.CornerDirection.*;

public class TUIParser {
    final Parser serverParser;
    final TUI view;
    final ViewBoard board;

    public TUIParser(CommandPassthrough serverProxy, TUI view, ViewBoard board) {
        this.serverParser = new Parser(serverProxy, board);
        this.view = view;
        this.board = board;
    }

    public void parseCommand(String command) throws IllegalArgumentException, IllegalStateException, RemoteException {
        List<String> commandComponents = Arrays.stream(command.trim().split("\\s+")).distinct().toList();
        String keyCommand = "";
        if(!commandComponents.isEmpty())
            keyCommand = commandComponents.get(0).toLowerCase();

        List<String> cmdArgs = commandComponents.stream().distinct().skip(1).toList();

        switch (keyCommand) {
            case "move":
                parseMoveCommand(cmdArgs);
                break;
            case "view":
            case "goto":
                parseViewCommand(cmdArgs);
                break;
            case "flip":
            case "turn":
            case "turnover":
                parseFlipCommand(cmdArgs);
                break;
            default:
                serverParser.parseCommand(command);
        }
        if(command.toLowerCase().matches("quit|disconnect")){
            throw new RemoteException("DISCONNECTED");
        }
    }

    private void parseMoveCommand(List<String> cmdArgs) throws IllegalArgumentException {
        if(cmdArgs.isEmpty()) throw new IllegalArgumentException("Must provide a direction to move towards!");
        List<CornerDirection> directions = new LinkedList<>();
        for(String arg : cmdArgs) {
            switch (arg.toLowerCase()){
                case "top", "up", "north", "above":
                    directions.add(TL);
                    directions.add(TR);
                    break;
                case "bottom", "down", "south", "below":
                    directions.add(BL);
                    directions.add(BR);
                    break;
                case "left", "leftward", "west":
                    directions.add(TL);
                    directions.add(BL);
                    break;
                case "right", "rightward", "east":
                    directions.add(TR);
                    directions.add(BR);
                    break;
                case "center", "zero":
                    view.setCenter(0,0);
                    break;
                default: throw new IllegalArgumentException("Invalid direction!");
            }
        }
        view.moveView(directions);
    }
    private void parseViewCommand(List<String> cmdArgs) {
        if(cmdArgs.isEmpty()) throw new IllegalArgumentException("Must provide an index or cardID to flip!");

        SceneID selectedScene =
            switch (cmdArgs.get(0).toLowerCase()) {
                case "me" -> SceneID.getMyAreaSceneID();
                case "board" -> SceneID.getBoardSceneID();
                default -> SceneID.getOpponentAreaSceneID(cmdArgs.get(0));
            };
        view.setScene(selectedScene);
    }
    private void parseFlipCommand(List<String> cmdArgs) {
        if(cmdArgs.isEmpty()) throw new IllegalArgumentException("Must provide an index or cardID to flip!");

        String arg = cmdArgs.get(0);
            if(arg.matches("\\d")) {
                try {
                    int index = Integer.parseInt(cmdArgs.get(0));
                    board.getPlayerHand().flipCard(index);
                }catch (IndexOutOfBoundsException | NumberFormatException e){
                    throw new IllegalArgumentException("Invalid flip index provided.");
                }
            }
            else board.getPlayerHand().flipCard(arg); //throws if card not in hand
        view.update(SceneID.getMyAreaSceneID(), "Flipped card " + arg);
    }
}
