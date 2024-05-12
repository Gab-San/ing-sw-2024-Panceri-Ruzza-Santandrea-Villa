package it.polimi.ingsw.server;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.server.rmi.RMIClient;
import it.polimi.ingsw.server.tcp.TCPClient;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private CommandPassthrough virtualClient;
    // TODO: DELETE MODEL VIEW
    private final ModelView view;
    public Parser(CommandPassthrough virtualClient, ModelView view){
        this.virtualClient = virtualClient;
        this.view = view;
    }

    public void parseCommand(String command) throws RemoteException, IllegalArgumentException, IllegalStateException {
        List<String> commandComponents = Arrays.stream(command.trim().split("\\s+")).distinct().toList();
        String keyCommand = "";
        if(!commandComponents.isEmpty())
            keyCommand = commandComponents.get(0).toLowerCase();

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
            case "quit":
                parseDisconnectCmd();
                break;
            case "restart":
                parseRestartCmd();
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
            case "reconnect":
                parseReconnectCmd(commandComponents);
                break;
            default:
                throw new IllegalArgumentException("Command not recognised");
        }

    }

    private void parseReconnectCmd(List<String> commandComponents) throws IllegalArgumentException{
        List<String> cmdArgs = commandComponents.stream().skip(1).toList();
        if(cmdArgs.size() < 3){
            throw new IllegalArgumentException("Too few arguments.\n" +
                    "Format as such: reconnect TCP/RMI hostname port");
        }

        String hostAddr = cmdArgs.get(1);
        int port;
        try {
            port = Integer.parseInt(cmdArgs.get(2));
        } catch (NumberFormatException formatException){
            throw new IllegalArgumentException(formatException.getMessage());
        }


        if(cmdArgs.get(0).equalsIgnoreCase("TCP") || cmdArgs.get(0).equalsIgnoreCase("SOCKET")){

            try {
                virtualClient = new TCPClient(hostAddr, port);
                return;
            } catch (UnknownHostException exc){
                throw new IllegalArgumentException("Wrong host: " + exc.getMessage());
            } catch (IOException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }

        try {
            virtualClient = new RMIClient(hostAddr, port);
        } catch (RemoteException | NotBoundException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

    }

    private void parseSetNumPlayers(List<String> commandComponents) throws IllegalArgumentException, RemoteException, IllegalStateException {
        List<String> noCmd = commandComponents.stream().skip(1).toList();
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

    private void parseTestCmd(List<String> commandComponents) throws RemoteException {
        StringBuilder text = new StringBuilder();

        commandComponents.stream().skip(1).forEachOrdered(
                (cmp) -> text.append(cmp).append(" ")
        );

        virtualClient.testCmd(text.toString().trim());
    }

    private void parseSendCmd(List<String> commandComponents) throws RemoteException {

        StringBuilder msg = new StringBuilder();
        commandComponents.stream().skip(1).forEachOrdered(
                (cmp) -> msg.append(cmp).append(" ")
        );

        virtualClient.sendMsg(msg.toString().trim());
    }

    private void parseRestartCmd() throws RemoteException, IllegalStateException {
        virtualClient.startGame();
    }

    private void parseDisconnectCmd() throws RemoteException, IllegalStateException {
        virtualClient.disconnect();
    }

    private void parseConnectCmd(List<String> commandComponents) throws IllegalArgumentException, RemoteException, IllegalStateException {
        if(commandComponents.size() < 2) throw new IllegalArgumentException("Connect command must provide a nickname.");
        List<String> nickNoCmd =  commandComponents.stream().skip(1).toList();

        StringBuilder nickname = new StringBuilder();
        for(String cmp : nickNoCmd){
            nickname.append(cmp).append(" ");
        }
        //TODO: Save nickname in TCP
        virtualClient.connect(nickname.toString().trim());
    }

    private void parseChooseCmd(List<String> commandComponents) throws RemoteException,IllegalArgumentException, IllegalStateException {
        List<String> cmdArgs = commandComponents.stream().skip(1).toList();

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


    private void parseChooseObjCmd(List<String> commandComponents) throws RemoteException, IllegalArgumentException, IllegalStateException {
        try{
            //TODO fix the index out of bound exception
            int choice = Integer.parseInt(commandComponents.get(1));
            virtualClient.chooseObjective(choice);
        }catch (IndexOutOfBoundsException e){
            throw new IllegalArgumentException("Choose command must provide a choice number (1 or 2).");
        }catch (NumberFormatException e){
            throw new IllegalArgumentException("Choose command must provide a valid number as choice (1 or 2).");
        }
    }


    private void parseDrawCmd(List<String> commandComponents) throws IllegalArgumentException, RemoteException, IllegalStateException {
        List<String> cmdArgs = commandComponents.stream().skip(1).toList();
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

    private void parsePlayCmd(List<String> commandComponents) throws IllegalArgumentException, RemoteException, IllegalStateException {
        List<String> cmdArg = commandComponents.stream().skip(1).toList();
        //FIXME: fix this command
        // It can recognise something like place starting G0 on G3
        // as a valid command
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
        String cornDir;

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
            cornDir = dirMatcher.group();
        } else {
            throw new IllegalArgumentException("missing direction");
        }

        virtualClient.placeCard(cardToPlace, placementPos, cornDir, view.getPlayerHand().isFaceUp(cardToPlace));
    }


    private void parsePlaceStartingCard() throws RemoteException, IllegalStateException {
        virtualClient.placeStartCard(view.getPlayerStartingCard().isFaceUp());
    }

}


