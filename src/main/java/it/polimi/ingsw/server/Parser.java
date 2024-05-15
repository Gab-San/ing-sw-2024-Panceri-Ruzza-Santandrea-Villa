package it.polimi.ingsw.server;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.server.rmi.RMIClient;
import it.polimi.ingsw.server.tcp.client.TCPClientSocket;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private CommandPassthrough virtualServer;
    // TODO: DELETE MODEL VIEW
    private final ModelView view;
    public Parser(CommandPassthrough virtualServer, ModelView view){
        this.virtualServer = virtualServer;
        this.view = view;
    }

    public void parseCommand(String command) throws RemoteException, IllegalArgumentException, IllegalStateException {
        List<String> commandComponents = Arrays.stream(command.trim().split("\\s+")).distinct().toList();
        String keyCommand = "";
        if(!commandComponents.isEmpty())
            keyCommand = commandComponents.get(0).toLowerCase();

        List<String> cmdArgs = commandComponents.stream().distinct().skip(1).toList();

        switch (keyCommand){
            case "place":
            case "play":
                parsePlayCmd(cmdArgs);
                break;
            case "draw":
                parseDrawCmd(cmdArgs);
                break;
            case "choose":
                parseChooseCmd(cmdArgs);
                break;
            case "join":
            case "connect":
                parseConnectCmd(cmdArgs);
                break;
            case "disconnect":
            case "quit":
                parseDisconnectCmd();
                break;
            case "restart":
                parseRestartCmd(cmdArgs);
                break;
            case "send":
                parseSendCmd(cmdArgs);
                break;
            case "set":
            case "players":
            case "start":
                parseSetNumPlayers(cmdArgs);
                break;
            case "reconnect":
                parseReconnectCmd(cmdArgs);
                break;
            default:
                throw new IllegalArgumentException("Command not recognised");
        }

    }

    private void parseReconnectCmd(List<String> cmdArgs) throws IllegalArgumentException {
        if (cmdArgs.size() < 3) {
            throw new IllegalArgumentException("Too few arguments.\n" +
                    "Format as such: reconnect TCP/RMI hostname port");
        }

        String hostAddr = cmdArgs.get(1);
        int port;
        try {
            port = Integer.parseInt(cmdArgs.get(2));
        } catch (NumberFormatException formatException) {
            throw new IllegalArgumentException(formatException.getMessage());
        }


        switch (cmdArgs.get(0).toLowerCase()) {
            case "tcp":
            case "socket":
                try {
                    TCPClientSocket socket = new TCPClientSocket(hostAddr, port);
                    virtualServer = socket.getProxy();
                    break;
                }  catch (IOException e) {
                    throw new IllegalArgumentException(e.getMessage());
                }
            case "rmi":
                try {
                    virtualServer = new RMIClient(hostAddr, port);
                    break;
                } catch (RemoteException | NotBoundException e) {
                    throw new IllegalArgumentException(e.getMessage());
                }
            default:
                throw new IllegalArgumentException("Command not recognised");
        }
    }

    private void parseSetNumPlayers(List<String> cmdArgs) throws IllegalArgumentException, RemoteException, IllegalStateException {
        int numOfPlayers;
        try {
            numOfPlayers = searchForNumber(cmdArgs, "[2-4]");
        } catch (IndexOutOfBoundsException exception){
            throw new IllegalArgumentException("Command wrongly formatted:\n"
                    + exception.getMessage() + "2 and 4");
        }

        virtualServer.setNumOfPlayers(numOfPlayers);
    }

    private int searchForNumber(List<String> numCmdComp, String numberBounds) throws IndexOutOfBoundsException{
        for(String cmp : numCmdComp){
            if(Pattern.compile(numberBounds).matcher(cmp).matches()){
                return Integer.parseInt(cmp);
            }
        }
        throw new IndexOutOfBoundsException("The number of players selected must be between: ");
    }

    private void parseSendCmd(List<String> cmdArgs) throws RemoteException {

        StringBuilder msg = new StringBuilder();
        cmdArgs.forEach(
                (cmp) -> msg.append(cmp).append(" ")
        );

        virtualServer.sendMsg(msg.toString().trim());
    }

    private void parseRestartCmd(List<String> cmdArgs) throws RemoteException, IllegalStateException {
        if(cmdArgs.isEmpty())
            throw new IllegalArgumentException("Restart must provide the number of players with which to restart the game (2-4)\n"+
                "For example: Restart with 4 players");
        int numOfPlayers;
        try {
             numOfPlayers = searchForNumber(cmdArgs, "[2-4]");
        } catch (IndexOutOfBoundsException outOfBoundsException){
            throw new IllegalArgumentException("Invalid number of players." + outOfBoundsException.getMessage() + "2 and 4");
        }
        virtualServer.startGame(numOfPlayers);
    }

    private void parseDisconnectCmd() throws RemoteException, IllegalStateException {
        virtualServer.disconnect();
    }

    private void parseConnectCmd(List<String> cmdArgs) throws IllegalArgumentException, RemoteException, IllegalStateException {
        if(cmdArgs.isEmpty()) throw new IllegalArgumentException("Connect command must provide a nickname.");

        StringBuilder nickname = new StringBuilder();
        for(String cmp : cmdArgs){
            nickname.append(cmp).append(" ");
        }

        virtualServer.connect(nickname.toString().trim());
    }

    private void parseChooseCmd(List<String> cmdArgs) throws RemoteException,IllegalArgumentException, IllegalStateException {
        if(cmdArgs.size() < 2) throw new IllegalArgumentException("""
                Choose command must provide what the player is choosing and the relevant information
                To choose an objective card: choose objective 1|2
                To choose a color: choose color Red|Blue|Yellow|Green
                """);
        if(cmdArgs.parallelStream().anyMatch(e -> e.equalsIgnoreCase("color"))) {
            parseChooseColorCommand(cmdArgs);
            return;
        }

        if(cmdArgs.parallelStream().anyMatch(e -> e.equalsIgnoreCase("objective") || e.equalsIgnoreCase("obj"))) {
            parseChooseObjCmd(cmdArgs);
            return;
        }

        throw new IllegalArgumentException("""
                Command not recognised. Maybe you meant:
                choose color ...
                choose objective ...
                """);
    }

    private void parseChooseColorCommand(List<String> cmdArgs) throws RemoteException, IllegalArgumentException, IllegalStateException {
        for (String arg : cmdArgs) {
            if (Pattern.compile("[Bb]lue|[Rr]ed|[Yy]ellow|[Gg]reen").matcher(arg).matches()) {
                virtualServer.chooseColor(arg.toUpperCase().charAt(0));
                return;
            }
        }
        throw new IllegalArgumentException("""
                Command was wrongly formatted: no color found
                Command example: choose color Red
                Choosable colors: Red, Blue, Yellow, Green (check availables!)
                """);
    }


    private void parseChooseObjCmd(List<String> cmdArgs) throws RemoteException, IllegalArgumentException, IllegalStateException {
        try{
            int choice = searchForNumber(cmdArgs, "[1-2]");
            virtualServer.chooseObjective(choice);
        }catch (IndexOutOfBoundsException e){
            throw new IllegalArgumentException(
                            """
                            Command wrongly formatted:
                            Choose objective command must provide a valid number as choice (1 or 2)
                            For example: choose objective 1
                            """
                            );
            //TODO ask because this way this exception should not be called ever
        }catch (NumberFormatException e){
            throw new IllegalArgumentException("Choose command must provide a valid number as choice (1 or 2).");
        }
    }


    private void parseDrawCmd(List<String> cmdArgs) throws IllegalArgumentException, RemoteException, IllegalStateException {
        if(cmdArgs.isEmpty()) throw new IllegalArgumentException("""
                Draw must only provide a deck choice and the card position
                For example: "R1" is the first revealed card of the Resource Deck
                """);
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

        if(!deckFound){
            StringBuilder stringBuilder = new StringBuilder("draw").append(" ");
            for (String arg : cmdArgs){
                stringBuilder.append(arg).append(" ");
            }

            throw new IllegalArgumentException("Command wrongly formatted: " + "\"" + stringBuilder + "\"\n"+
                    "Draw must only provide a deck choice and the card position\n" +
                    "For example: \"R1\" is the first revealed card of the Resource Deck");
        }

        virtualServer.draw(deck, position);
    }

    private void parsePlayCmd(List<String> cmdArgs) throws IllegalArgumentException, RemoteException, IllegalStateException {
        if(cmdArgs.contains("Starting") || cmdArgs.contains("starting")) {
            parsePlaceStartingCard(cmdArgs);
            return;
        }

        try {
            parsePlaceCard(cmdArgs);
        } catch (IllegalArgumentException exception){
            throw new IllegalArgumentException("Command wrongly formatted: " + exception.getMessage() +"\n"
                    + "Command must provide the cardId of the placing card the card on which to place and the corner direction\n"
                    + "For example: place card G0 on G4 TL|TR|BL|BR");
        }
    }

    private void parsePlaceCard(List<String> cmdArg) throws IllegalArgumentException, RemoteException, IllegalStateException{
        if( cmdArg.size() < 3 ) throw new IllegalArgumentException("Too few command arguments");

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
            throw new IllegalArgumentException("missing placed card or on which to place");
        }

        if(cardMatcher.find()){
            String cardID = cardMatcher.group();
            placementPos = view.getPosition(cardID);
        } else{
            throw new IllegalArgumentException("missing card to place or on which to place");
        }

        Matcher dirMatcher = Pattern.compile("TL|BL|TR|BR").matcher(argsString);
        if(dirMatcher.find()){
            cornDir = dirMatcher.group();
        } else {
            throw new IllegalArgumentException("missing direction");
        }
        //TODO get player card flip state
        virtualServer.placeCard(cardToPlace, placementPos, cornDir, view.getPlayerHand().isFaceUp(cardToPlace));
    }


    private void parsePlaceStartingCard(List<String> cmdArgs) throws RemoteException, IllegalStateException {
        if(cmdArgs.size() > 2) throw new IllegalArgumentException("""
                Maybe you meant: Place starting card
                """);
        //TODO Add getting the player card flip state
        virtualServer.placeStartCard(view.getPlayerStartingCard().isFaceUp());
    }

}


