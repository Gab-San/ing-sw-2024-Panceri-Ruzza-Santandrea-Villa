package it.polimi.ingsw.server.tcp.message;

import java.util.Arrays;
import java.util.List;

public class BaseMessage implements TCPMessage{

    private final String msg;
    private static final String cmd = "send";

    public BaseMessage(String msg){
        this.msg = msg;
    }
    /**
     * Serializing the message in order to create a string
     * to be sent to the TCP Server;
     *
     * @return the serialized string to send to the server;
     */
    @Override
    public String serialize() {
        String[] msgComponents = msg.split("\\s+");
        List<String> msgNoCmd = Arrays.stream(msgComponents).filter(e -> !e.equalsIgnoreCase("send")).toList();
        StringBuilder remainingMsg = new StringBuilder(cmd).append("|");
        for(String cmp : msgNoCmd){
            remainingMsg.append(cmp.toLowerCase()).append("-");
        }
        return remainingMsg.toString().trim();
    }

    /**
     * Deserializing the message in order to create
     * a command to be operated in the server
     *
     * @return the deserialized command to operate in the server;
     */
    @Override
    public String deserialize() {
        String[] msgComponents = msg.split("-");
        StringBuilder remainingMsg = new StringBuilder();
        for (String msgComponent : msgComponents) {
            remainingMsg.append(msgComponent).append(" ");
        }
        return remainingMsg.toString().trim();
    }
}
