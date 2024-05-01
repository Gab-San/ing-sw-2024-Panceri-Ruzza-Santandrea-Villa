package it.polimi.ingsw.server.tcp.message;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class PlaceMessage implements TCPMessage{

    private final String input;
    private static final String cmd = "place";

    public PlaceMessage(String input){
        this.input = input;
    }

    /**
     * Serializing the message in order to create a string
     * to be sent to the TCP Server;
     *
     * @return the serialized string to send to the server;
     */
    @Override
    public String serialize() {
        String[] msgComponents = input.split("\\s+");

        List<String> msgNoCmd = Arrays.stream(msgComponents).filter(
                e -> !e.equalsIgnoreCase("place") &&
                        !e.equalsIgnoreCase("play")
        ).toList();

        StringBuilder placeMsg = new StringBuilder(cmd);

        if(msgNoCmd.parallelStream().anyMatch( e -> e.equalsIgnoreCase("start") ||
                e.equalsIgnoreCase("starting")
                || Pattern.compile("S[0-6]").matcher(e).matches()
        )){
                placeMsg.append("|");
                msgNoCmd = msgNoCmd.parallelStream().filter(e -> !e.equalsIgnoreCase("start") &&
                        !e.equalsIgnoreCase("starting")).toList();
                placeMsg.append("start").append("|");

                if(msgNoCmd.parallelStream().anyMatch(
                        e-> e.equalsIgnoreCase("faceUp")
                )){

                }
        }


        return null;
    }

    /**
     * Deserializing the message in order to create
     * a command to be operated in the server
     *
     * @return the deserialized command to operate in the server;
     */
    @Override
    public String deserialize() {
        return null;
    }
}
