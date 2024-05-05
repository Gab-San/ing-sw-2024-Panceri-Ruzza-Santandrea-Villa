package it.polimi.ingsw.server;

import it.polimi.ingsw.server.rmi.RMIClient;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;

//FIXME parser will call a sendCmd function onto the virtual client, passing a string
// Decide if clients will act as VirtualServer then parser can directly call functions
public class Parser {

    private final CommandPassthrough virtualClient;
    public Parser(){
        //TODO Decide whether to handle exception or remove constructor
        try {
            this.virtualClient = new RMIClient();
        } catch (NotBoundException | RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public Parser(CommandPassthrough virtualClient){
        this.virtualClient = virtualClient;
    }


    public void parseCommand(String command) throws ConnectionLostException, RemoteException, IllegalArgumentException {
        String[] commandComponents = command.trim().split("\\s+");
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
                parseDisconnectCmd(commandComponents);
                break;
            case "restart":
                parseRestartCmd(commandComponents);
                break;
            case "send":
                parseSendCmd(commandComponents);
                break;
            default:
                throw new IllegalArgumentException("Message not recognised");
        }

    }

    private void parseSendCmd(String[] commandComponents) throws ConnectionLostException, RemoteException {
        List<String> msgNoCmd =  Arrays.stream(commandComponents).skip(1).toList();

        StringBuilder msg = new StringBuilder();
        for(String cmp : msgNoCmd){
            msg.append(cmp).append(" ");
        }

        virtualClient.sendMsg(msg.toString().trim());
    }

    private void parseRestartCmd(String[] commandComponents) throws RemoteException {
        virtualClient.startGame();
    }

    private void parseDisconnectCmd(String[] commandComponents) throws RemoteException {
        virtualClient.disconnect();
    }

    private void parseConnectCmd(String[] commandComponents) throws IllegalArgumentException, RemoteException {
        if(commandComponents.length < 2) throw new IllegalArgumentException("Connect command must provide a nickname.");
        List<String> nickNoCmd =  Arrays.stream(commandComponents).skip(1).toList();

        StringBuilder nickname = new StringBuilder();
        for(String cmp : nickNoCmd){
            nickname.append(cmp).append(" ");
        }

        virtualClient.connect(nickname.toString().trim());
    }

    private void parseChooseCmd(String[] commandComponents) throws RemoteException {
        try{
            int choice = Integer.parseInt(commandComponents[1]);
            virtualClient.chooseObjective(choice);
        }catch (IndexOutOfBoundsException e){
            throw new IllegalArgumentException("Choose command must provide a choice number (1 or 2).");
        }catch (NumberFormatException e){
            throw new IllegalArgumentException("Choose command must provide a valid number as choice (1 or 2).");
        }
    }

    private void parseDrawCmd(String[] commandComponents) throws RemoteException {

    }

    private void parsePlayCmd(String[] commandComponents) throws RemoteException {

    }


}
