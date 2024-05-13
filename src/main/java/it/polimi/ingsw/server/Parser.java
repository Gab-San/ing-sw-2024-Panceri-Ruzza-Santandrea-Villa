package it.polimi.ingsw.server;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.server.rmi.RMIClient;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private final CommandPassthrough virtualClient;
    // TODO: DELETE MODEL VIEW
    private final ModelView view;
    public Parser(CommandPassthrough virtualClient, ModelView view){
        this.virtualClient = virtualClient;
        this.view = view;
    }


    public void parseCommand(String command) throws RemoteException, IllegalArgumentException, IllegalStateException {
        String[] commandComponents = (String[]) Arrays.stream(command.trim().split("\\s+")).distinct().toArray();
        String keyCommand = "";
        if(commandComponents.length > 0)
            keyCommand = commandComponents[0].toLowerCase();

        switch (keyCommand){
            case "place":
            case "play":
                parsePlayCmd(commandComponents);
                break;
            case "draw":
                parseDrawCmd(commandComponents);
                break;
            case "choose":
                parseChooseCmd(commandComponents);
                break;
            case "join":
            case "connect":
                parseConnectCmd(commandComponents);
                break;
            case "disconnect":
                parseDisconnectCmd();
                break;
            case "restart":
                parseRestartCmd(commandComponents);
                break;
            case "send":
                parseSendCmd(commandComponents);
                break;
            case "set":
            case "players":
            case "start":
                parseSetNumPlayers(commandComponents);
                break;
            case "test":
                parseTestCmd(commandComponents);
                break;
            default:
                throw new IllegalArgumentException("Command not recognised");
        }

    }

    private void parseSetNumPlayers(String[] commandComponents) throws IllegalArgumentException, RemoteException, IllegalStateException {
        List<String> noCmd = Arrays.stream(commandComponents).skip(1).toList();
        int numOfPlayers;
        try {
            numOfPlayers = searchForNumber(noCmd);
        } catch (IndexOutOfBoundsException exception){
            throw new IllegalArgumentException("Command wrongly formatted", exception);
        }

        virtualClient.setNumOfPlayers(numOfPlayers);
    }

    private int searchForNumber(List<String> numCmdComp) throws IndexOutOfBoundsException{
        for(String cmp : numCmdComp){
            if(Pattern.compile("[2-4]").matcher(cmp).matches()){
                return Integer.parseInt(cmp);
            }
        }
        throw new IndexOutOfBoundsException("The number of players selected must be between 2 and 4");
    }

    private void parseTestCmd(String[] commandComponents) throws RemoteException {
        List<String> textNoCmd =  Arrays.stream(commandComponents).skip(1).toList();

        StringBuilder text = new StringBuilder();
        for(String cmp : textNoCmd){
            text.append(cmp).append(" ");
        }

        virtualClient.testCmd(text.toString().trim());
    }

    private void parseSendCmd(String[] commandComponents) throws RemoteException {
        List<String> msgNoCmd =  Arrays.stream(commandComponents).skip(1).toList();

        StringBuilder msg = new StringBuilder();
        for(String cmp : msgNoCmd){
            msg.append(cmp).append(" ");
        }

        virtualClient.sendMsg(msg.toString().trim());
    }

    private void parseRestartCmd(String[] commandComponents) throws RemoteException, IllegalStateException {
        if(commandComponents.length < 2) throw new IllegalArgumentException("Restart command must provide a valid number of players.");
        List<String> numNoCmd =  Arrays.stream(commandComponents).skip(1).toList();

        int numPlayers = -1;
        for(String n : numNoCmd){
            try{
                numPlayers = Integer.parseInt(n);
                if(numPlayers > 4) throw new IllegalArgumentException("Restart command must provide a number of players < 4");
                if(numPlayers < view.getPlayerCount()) throw new IllegalArgumentException("Restart command must provide a number of players equal or greater than the current player count");
            }catch (NumberFormatException ignored){}
        }

        virtualClient.startGame(numPlayers);
    }

    private void parseDisconnectCmd() throws RemoteException, IllegalStateException {
        virtualClient.disconnect();
    }

    private void parseConnectCmd(String[] commandComponents) throws IllegalArgumentException, RemoteException, IllegalStateException {
        if(commandComponents.length < 2) throw new IllegalArgumentException("Connect command must provide a nickname.");
        List<String> nickNoCmd =  Arrays.stream(commandComponents).skip(1).toList();

        StringBuilder nickname = new StringBuilder();
        for(String cmp : nickNoCmd){
            nickname.append(cmp).append(" ");
        }
        virtualClient.connect(nickname.toString().trim());
    }

    private void parseChooseCmd(String[] commandComponents) throws RemoteException,IllegalArgumentException, IllegalStateException {
        List<String> cmdArgs = Arrays.stream(commandComponents).skip(1).toList();

        if(cmdArgs.stream().anyMatch(e -> e.equalsIgnoreCase("color"))) {
            parseChooseColorCommand(cmdArgs);
            return;
        }

        // Parsing and recognising ChooseObjective command
        try {
            parseChooseObjCmd(commandComponents);
        } catch (IllegalArgumentException exc){
            throw new IllegalArgumentException("Command was wrongly formatted:\n" + exc.getMessage());
        }
    }

    private void parseChooseColorCommand(List<String> cmdArgs) throws RemoteException, IllegalArgumentException, IllegalStateException {
        for (String arg : cmdArgs) {
            if (Pattern.compile("[Bb]lue|[Rr]ed|[Yy]ellow|[Gg]reen").matcher(arg).matches()) {
                virtualClient.chooseColor(arg.toUpperCase().charAt(0));
                return;
            }
        }
        throw new IllegalArgumentException("Command was wrongly formatted: no color found");
    }


    private void parseChooseObjCmd(String[] commandComponents) throws RemoteException, IllegalArgumentException, IllegalStateException {
        try{
            int choice = Integer.parseInt(commandComponents[1]);
            virtualClient.chooseObjective(choice);
        }catch (IndexOutOfBoundsException e){
            throw new IllegalArgumentException("Choose command must provide a choice number (1 or 2).");
        }catch (NumberFormatException e){
            throw new IllegalArgumentException("Choose command must provide a valid number as choice (1 or 2).");
        }
    }

    private void parseDrawCmd(String[] commandComponents) throws IllegalArgumentException, RemoteException, IllegalStateException {
        List<String> cmdArgs = Arrays.stream(commandComponents).skip(1).toList();
        boolean deckFound = false;
        char deck = '\0';
        int position = 0;
        for(int i = 0; i < cmdArgs.size() && !deckFound; i++){
            if(Pattern.compile("[RGrg][0-2]").matcher(cmdArgs.get(i)).matches()){
                String arg = cmdArgs.get(i).trim();
                deck = arg.charAt(0);
                position = Integer.parseInt(String.valueOf(arg.charAt(1)));
                deckFound = true;
            }
        }

        if(!deckFound) throw new IllegalArgumentException("Command wrongly formatted");

        virtualClient.draw(deck, position);
    }

    private void parsePlayCmd(String[] commandComponents) throws IllegalArgumentException, RemoteException, IllegalStateException {
        List<String> cmdArg = Arrays.stream(commandComponents).skip(1).toList();
        for(String arg : cmdArg){
            if(Pattern.compile("[Ss]tarting|[Ss][0-5]").matcher(arg).matches()){
                parsePlaceStartingCard();
                return;
            }
        }

        try {
            parsePlaceCard(cmdArg);
        } catch (IllegalArgumentException exception){
            throw new IllegalArgumentException("Command wrongly formatted: ", exception);
        } catch (NoSuchMethodException methodException){
            throw new IllegalArgumentException("Command wrongly formatted: missing \"starting\" keyword or cardId", methodException);
        }
    }

    private void parsePlaceCard(List<String> cmdArg) throws NoSuchMethodException, IllegalArgumentException, RemoteException, IllegalStateException{
        if( cmdArg.size() < 3 ) throw new NoSuchMethodException("Too few command arguments");
        StringBuilder argsStr = new StringBuilder();
        for(String arg : cmdArg){
            argsStr.append(arg).append(" ");
        }

        String cardToPlace;
        Point placementPos;
        CornerDirection cornDir;

        String argsString =  argsStr.toString().trim();
        Matcher cardMatcher = Pattern.compile("[RGrg][0-39]").matcher(argsString);
        if(cardMatcher.find()){
            cardToPlace = cardMatcher.group();
        } else {
            throw new IllegalArgumentException("missing card to place");
        }

        if(cardMatcher.find()){
            String cardID = cardMatcher.group();
            placementPos = view.getPosition(cardID);
        } else{
            throw new IllegalArgumentException("missing card on which to place");
        }

        Matcher dirMatcher = Pattern.compile("TL|BL|TR|BR").matcher(argsString);
        if(dirMatcher.find()){
            cornDir = CornerDirection.getDirectionFromString(dirMatcher.group());
        } else {
            throw new IllegalArgumentException("missing direction");
        }

        virtualClient.placeCard(cardToPlace, placementPos, cornDir, view.getPlayerHand().isFaceUp(cardToPlace));
    }

    private void parsePlaceStartingCard() throws RemoteException, IllegalStateException {
        virtualClient.placeStartCard(view.getPlayerStartingCard().isFaceUp());
    }

}


