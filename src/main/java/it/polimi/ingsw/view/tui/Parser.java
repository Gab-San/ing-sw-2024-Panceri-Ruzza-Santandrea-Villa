package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.network.CommandPassthrough;
import it.polimi.ingsw.network.rmi.RMIClient;
import it.polimi.ingsw.network.tcp.client.TCPClientSocket;
import it.polimi.ingsw.view.ViewController;
import it.polimi.ingsw.view.exceptions.DisconnectException;
import it.polimi.ingsw.view.model.ViewPlayArea;
import it.polimi.ingsw.view.model.cards.ViewStartCard;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser for all  server-related commands on the TUI.
 */
public class Parser {

    private CommandPassthrough virtualServer;
    private final ViewController viewController;

    /**
     * Builds the Parser and sets references to ViewController and VirtualServer
     * @param virtualServer reference to the server proxy
     * @param viewController reference to the ViewController
     */
    public Parser(CommandPassthrough virtualServer, ViewController viewController){
        this.virtualServer = virtualServer;
        this.viewController = viewController;
    }
    /**
     * Parses the given command and executes it if it's a command to perform an action on the server.
     * @param command the user input to be parsed as a command
     * @throws IllegalArgumentException if the command is invalid or formatted incorrectly.
     *                              The exception message generally specifies the proper formatting
     * @throws IllegalStateException if the command cannot be executed at the time of parsing
     * @throws RemoteException if a connection error occurs while communicating with the server
     */
    public void parseCommand(String command) throws RemoteException, IllegalArgumentException, IllegalStateException {
        List<String> commandComponents = Arrays.stream(command.trim().split("\\s+")).distinct().toList();
        String keyCommand = "";
        if(!commandComponents.isEmpty())
            keyCommand = commandComponents.get(0).toLowerCase();

        List<String> cmdArgs = commandComponents.stream().distinct().skip(1).toList();

        switch (keyCommand){
            case "place", "play":
                parsePlayCmd(cmdArgs);
                break;
            case "draw":
                parseDrawCmd(cmdArgs);
                break;
            case "choose":
                parseChooseCmd(cmdArgs);
                break;
            case "join", "connect":
                parseConnectCmd(cmdArgs);
                break;
            case "disconnect", "quit":
                parseDisconnectCmd();
                break;
            case "restart":
                parseRestartCmd(cmdArgs);
                break;
            case "send":
                parseSendCmd(cmdArgs);
                break;
            case "set", "players":
                parseSetNumPlayers(cmdArgs);
                break;
//            case "reconnect":
//                parseReconnectCmd(cmdArgs);
//                break;
            default:
                throw new IllegalArgumentException("Command not recognised");
        }

    }

//region RECONNECT (unused)
//    //FIXME: as per current View implementation, this is useless (may be useful for GUI or for future reworks though?)
//    private void parseReconnectCmd(List<String> cmdArgs) throws IllegalArgumentException {
//        if (cmdArgs.size() < 3) {
//            throw new IllegalArgumentException("Too few arguments.\n" +
//                    "Format as such: reconnect TCP/RMI hostname port");
//        }
//
//        String hostAddr = cmdArgs.get(1);
//        int port;
//        try {
//            port = Integer.parseInt(cmdArgs.get(2));
//        } catch (NumberFormatException formatException) {
//            throw new IllegalArgumentException(formatException.getMessage());
//        }
//
//
//        switch (cmdArgs.get(0).toLowerCase()) {
//            case "tcp":
//            case "socket":
//                try {
//                    TCPClientSocket socket = new TCPClientSocket(hostAddr, port);
//                    virtualServer = socket.getProxy();
//                    break;
//                }  catch (IOException e) {
//                    throw new IllegalArgumentException(e.getMessage());
//                }
//            case "rmi":
//                try {
//                    RMIClient rmiClient = new RMIClient(hostAddr, port);
//                    virtualServer = rmiClient.getProxy();
//                    break;
//                } catch (RemoteException | NotBoundException e) {
//                    throw new IllegalArgumentException(e.getMessage());
//                }
//            default:
//                throw new IllegalArgumentException("Command not recognised");
//        }
//    }
//endregion

    /**
     * Parses set < num players > command and calls the related function on the server proxy.
     * @param cmdArgs command arguments
     * @throws IllegalArgumentException if the command is invalid or formatted incorrectly.
     *                              The exception message specifies the proper formatting
     * @throws IllegalStateException if the command cannot be executed at the time of parsing
     * @throws RemoteException if a connection error occurs while communicating with the server
     */
    private void parseSetNumPlayers(List<String> cmdArgs) throws IllegalArgumentException, IllegalStateException, RemoteException {
        int numOfPlayers;
        try {
            numOfPlayers = searchForNumber(cmdArgs, "[2-4]");
        } catch (IndexOutOfBoundsException exception){
            throw new IllegalArgumentException("Command wrongly formatted:\n"
                    + exception.getMessage() + "2 and 4");
        }

        viewController.validatePhase(GamePhase.SETNUMPLAYERS);
        virtualServer.setNumOfPlayers(numOfPlayers);
    }

    /**
     * Finds the first number within bounds in the given list of arguments
     * @param numCmdComp list of command arguments
     * @param numberBounds regex matching the number that should be returned
     * @return the first number in the list matching the bounds
     * @throws IndexOutOfBoundsException if there are no numbers in the list
     *                                or if all numbers do not match the bounds
     */
    private int searchForNumber(List<String> numCmdComp, String numberBounds) throws IndexOutOfBoundsException{
        for(String cmp : numCmdComp){
            if(Pattern.compile(numberBounds).matcher(cmp).matches()){
                return Integer.parseInt(cmp);
            }
        }
        throw new IndexOutOfBoundsException("The number of players selected must be between: ");
    }

//region MESSAGING
    /**
     * Parses the send command to send a chat message.
     * @param cmdArgs command arguments
     * @throws IllegalArgumentException if the command is invalid or formatted incorrectly
     * @throws RemoteException if a connection error occurs while communicating with the server
     */
    private void parseSendCmd(List<String> cmdArgs) throws IllegalArgumentException, RemoteException {
        if(cmdArgs.size() < 2) throw new IllegalArgumentException("""
                Command wrongly formatted: missing either message or addressee.
                For example: "all" hi or "Player 2" hi
                """);

        String addresseeNickExtract = parseAddressee(cmdArgs);
        List<String> partsToRemove = Arrays.stream(addresseeNickExtract.split("\\s+")).toList();
        StringBuilder msg = new StringBuilder();
        cmdArgs.forEach(
                (cmp) ->{
                            if(partsToRemove.contains(cmp)) return;
                            msg.append(cmp).append(" ");
                        }
        );

        //removing quotes
        String addressee = addresseeNickExtract.substring(1, addresseeNickExtract.length() - 1);

        viewController.validateMsg(addressee);
        virtualServer.sendMsg(addressee, msg.toString().trim());
    }

    /**
     * Parses the Addressee for a chat send message command according to the nickname regex. <br>
     * "[^\n ].*[a-zA-Z].*[^\n ]" (including the "")
     * @param cmdArgs send command arguments
     * @return the first argument that matches the addressee regex
     */
    private String parseAddressee(List<String> cmdArgs){
        StringBuilder command = new StringBuilder();
        cmdArgs.forEach(
                (arg) -> command.append(arg).append(" ")
        );

        Matcher matcher = Pattern.compile("\"[^\n ].*[a-zA-Z].*[^\n ]\"").matcher(command.toString().trim());

        if(matcher.find()){
            return matcher.group();
        }

        throw new IllegalArgumentException("Missing addressee nick");
    }
//endregion

    /**
     * Parses the restart command and calls the related function on the server proxy.
     * @param cmdArgs the command arguments
     * @throws IllegalArgumentException if the command is invalid or formatted incorrectly.
     *                              The exception message specifies the proper formatting
     * @throws RemoteException if a connection error occurs while communicating with the server
     */
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
        viewController.validatePhase(GamePhase.SHOWWIN);
        virtualServer.restartGame(numOfPlayers);
    }

//region CONNECTION COMMANDS

    /**
     * Calls the disconnect() function on the server proxy.
     * @throws RemoteException if a connection error occurs while communicating with the server
     * @throws IllegalStateException if the command cannot be executed at the time of parsing
     */
    private void parseDisconnectCmd() throws RemoteException, IllegalStateException {
        virtualServer.disconnect();
    }

    /**
     * Parses the connect command and calls the related function on the server proxy.
     * @param cmdArgs command arguments
     * @throws IllegalArgumentException if the command is invalid or formatted incorrectly.
     *                              The exception message specifies the proper formatting
     * @throws IllegalStateException if the command cannot be executed at the time of parsing
     * @throws RemoteException if a connection error occurs while communicating with the server
     */
    private void parseConnectCmd(List<String> cmdArgs) throws IllegalArgumentException, RemoteException, IllegalStateException {
        if(cmdArgs.isEmpty()) throw new IllegalArgumentException("Connect command must provide a nickname.");

        StringBuilder nickname = new StringBuilder();
        for(String cmp : cmdArgs){
            nickname.append(cmp).append(" ");
        }

        virtualServer.connect(nickname.toString().trim());
    }
//endregion
    /**
     * Parses the generic choose command and delegates parsing to parseChooseColor or parseChooseObjective.
     * @param cmdArgs command arguments
     * @throws IllegalArgumentException if the command is invalid or formatted incorrectly.
     *                              The exception message specifies the proper formatting
     * @throws IllegalStateException if the command cannot be executed at the time of parsing
     * @throws RemoteException if a connection error occurs while communicating with the server
     */
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

        if(cmdArgs.parallelStream().anyMatch(e -> e.toLowerCase().matches("objectives?|obj"))){
            parseChooseObjCmd(cmdArgs);
            return;
        }

        throw new IllegalArgumentException("""
                Command not recognised. Maybe you meant:
                choose color Red|Blue|Yellow|Green
                choose objective 1|2
                """);
    }

    /**
     * Parses the choose color command and calls the related function on the server proxy.
     * @param cmdArgs command arguments
     * @throws IllegalArgumentException if the command is invalid or formatted incorrectly.
     *                              The exception message specifies the proper formatting
     * @throws IllegalStateException if the command cannot be executed at the time of parsing
     * @throws RemoteException if a connection error occurs while communicating with the server
     */
    private void parseChooseColorCommand(List<String> cmdArgs) throws RemoteException, IllegalArgumentException, IllegalStateException {
        for (String arg : cmdArgs) {
            if (Pattern.compile("[Bb]lue|[Rr]ed|[Yy]ellow|[Gg]reen").matcher(arg).matches()) {
                viewController.validateChooseColor();
                virtualServer.chooseColor(arg.toUpperCase().charAt(0));
                return;
            }
        }

        throw new IllegalArgumentException("""
                Command was wrongly formatted: no color found
                Command example: choose color Red
                Player colors: Red, Blue, Yellow, Green (check availability in Board scene!)
                """);
    }

    /**
     * Parses the choose objective command and calls the related function on the server proxy.
     * @param cmdArgs command arguments
     * @throws IllegalArgumentException if the command is invalid or formatted incorrectly.
     *                              The exception message specifies the proper formatting
     * @throws IllegalStateException if the command cannot be executed at the time of parsing
     * @throws RemoteException if a connection error occurs while communicating with the server
     */
    private void parseChooseObjCmd(List<String> cmdArgs) throws RemoteException, IllegalArgumentException, IllegalStateException {
        try{
            int choice = searchForNumber(cmdArgs, "[1-2]");
            viewController.validateChooseObjective();
            virtualServer.chooseObjective(choice);
        }catch (IndexOutOfBoundsException e){
            throw new IllegalArgumentException(
                            """
                            Command wrongly formatted:
                            Choose objective command must provide a valid number as choice (1 or 2)
                            For example: choose objective 1
                            """
                            );
        }
    }
    /**
     * Parses the draw command and calls the related function on the server proxy.
     * @param cmdArgs command arguments
     * @throws IllegalArgumentException if the command is invalid or formatted incorrectly.
     *                              The exception message specifies the proper formatting
     * @throws IllegalStateException if the command cannot be executed at the time of parsing
     * @throws RemoteException if a connection error occurs while communicating with the server
     */
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
                String arg = cmdArgs.get(i).trim().toUpperCase();
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
        viewController.validateDraw(deck, position);
        virtualServer.draw(deck, position);
    }
    /**
     * Parses the place command and delegates parsing to either parsePlaceStartingCard or parsePlaceCard.
     * @param cmdArgs command arguments
     * @throws IllegalArgumentException if the command is invalid or formatted incorrectly.
     *                              The exception message specifies the proper formatting
     * @throws IllegalStateException if the command cannot be executed at the time of parsing
     * @throws RemoteException if a connection error occurs while communicating with the server
     */
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
    /**
     * Parses the place card command and calls the related function on the server proxy
     * @param cmdArgs command arguments
     * @throws IllegalArgumentException if the command is invalid or formatted incorrectly.
     *                              The exception message specifies the proper formatting
     * @throws IllegalStateException if the command cannot be executed at the time of parsing
     * @throws RemoteException if a connection error occurs while communicating with the server
     */
    private void parsePlaceCard(List<String> cmdArgs) throws IllegalArgumentException, RemoteException, IllegalStateException{
        if( cmdArgs.size() < 3 ) throw new IllegalArgumentException("Too few command arguments");

        StringBuilder argsStr = new StringBuilder();
        for(String arg : cmdArgs){
            argsStr.append(arg).append(" ");
        }

        String cardToPlace;
        GamePoint placementPos;
        String cornDir;

        String argsString =  argsStr.toString().trim();
        Matcher cardMatcher = Pattern.compile("[RGSrgs][1-3]?[0-9]").matcher(argsString);
        if(cardMatcher.find()){
            cardToPlace = cardMatcher.group().trim().toUpperCase();
            if(cardToPlace.charAt(0) == 'S')
                throw new IllegalArgumentException("given ID of card to place is invalid.");
        } else {
            throw new IllegalArgumentException("missing ID of card to place or given ID is invalid");
        }

        if(cardMatcher.find()){
            String cardID = cardMatcher.group().trim().toUpperCase();
            ViewPlayArea playArea = viewController.getSelfPlayArea();
            placementPos = playArea.getPositionByID(cardID); // throws IllegalArgument if card isn't in playArea
        } else{
            throw new IllegalArgumentException("missing ID of card on which to place or given ID is invalid");
        }

        Matcher dirMatcher = Pattern.compile("TL|BL|TR|BR").matcher(argsString.toUpperCase());
        if(dirMatcher.find()){
            cornDir = dirMatcher.group();
        } else {
            throw new IllegalArgumentException("missing direction");
        }
        viewController.validatePlaceCard(cardToPlace, placementPos, cornDir);
        virtualServer.placeCard(cardToPlace, placementPos, cornDir, viewController.getSelfCardById(cardToPlace).isFaceUp());
    }

    /**
     * Parses the place starting command and calls the related function on the server proxy
     * @param cmdArgs command arguments
     * @throws IllegalArgumentException if the command is invalid or formatted incorrectly.
     *                              The exception message specifies the proper formatting
     * @throws IllegalStateException if the command cannot be executed at the time of parsing
     * @throws RemoteException if a connection error occurs while communicating with the server
     */
    private void parsePlaceStartingCard(List<String> cmdArgs) throws RemoteException, IllegalArgumentException, IllegalStateException {
        if(cmdArgs.size() > 2) throw new IllegalArgumentException("""
                Maybe you meant: Place starting card
                """);
        viewController.validatePlaceStartCard();
        ViewStartCard startCard = viewController.getSelfStartingCard();
        virtualServer.placeStartCard(startCard.isFaceUp());
    }

    /**
     * Sets the local player's playArea in the ViewController. <br>
     * This method provides access to the TUI.
     */
    public void setSelfPlayerArea() {
        viewController.setSelfPlayerArea();
    }
}


