package it.polimi.ingsw.server;

import it.polimi.ingsw.server.rmi.RMIClient;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;

//FIXME parser will call a sendCmd function onto the virtual client, passing a string
// Decide if clients will act as VirtualServer then parser can directly call functions
public class Parser {

    private final VirtualClient virtualClient;
    public Parser(){
        //TODO Decide whether to handle exception or remove constructor
        try {
            this.virtualClient = new RMIClient();
        } catch (NotBoundException | RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public Parser(VirtualClient virtualClient){
        this.virtualClient = virtualClient;
    }


    public void parseCommand(String command){
        String[] commandComponents = command.trim().split("\\s+");
        String keyCommand = Arrays.stream(commandComponents).distinct().filter(
                e -> e.equalsIgnoreCase("place") ||
                        e.equalsIgnoreCase("draw") ||
                        e.equalsIgnoreCase("disconnect") ||
                        e.equalsIgnoreCase("choose") ||
                        e.equalsIgnoreCase("play") ||
                        e.equalsIgnoreCase("restart") ||
                        e.equalsIgnoreCase("join") ||
                        e.equalsIgnoreCase("connect")
                        // TODO eliminate this part
                        || e.equalsIgnoreCase("send")
        ).findAny().orElse("").toLowerCase();

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
            case "connect":
                parseConnectCmd(commandComponents);
            case "disconnect":
                parseDisconnectCmd(commandComponents);
            case "join":
                parseJoinCmd(commandComponents);
            case "restart":
                parseRestartCmd(commandComponents);
            case "send":
                parseSendCmd(commandComponents);
            default:
                throw new RuntimeException("Message not recognised");
        }

    }

    private void parseSendCmd(String[] commandComponents) {
        String[] msgNoCmd = (String[]) Arrays.stream(commandComponents).filter(
                e-> !e.equalsIgnoreCase("send")
        ).toArray();

        StringBuilder msg = new StringBuilder("send").append(" ");
        for(String cmp : msgNoCmd){
            msg.append(cmp.toLowerCase()).append(" ");
        }

        virtualClient.sendCmd(msg.toString());
    }

    private void parseRestartCmd(String[] commandComponents) {
    }

    private void parseJoinCmd(String[] commandComponents) {
    }

    private void parseDisconnectCmd(String[] commandComponents) {
    }

    private void parseConnectCmd(String[] commandComponents) {
    }

    private void parseChooseCmd(String[] commandComponents) {
    }

    private void parseDrawCmd(String[] commandComponents) {
    }

    private void parsePlayCmd(String[] commandComponents){

    }




}
