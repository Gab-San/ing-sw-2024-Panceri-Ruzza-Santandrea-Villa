package it.polimi.ingsw.server.tcp.message;

import it.polimi.ingsw.server.Commands.GameCommand;

public interface TCPMessage {
    /**
     * Serializing the message in order to create a string
     * to be sent to the TCP Server;
     * @return the serialized string to send to the server;
     */
    public String serialize();

    /**
     * Deserializing the message in order to create
     * a command to be operated in the server
     * @return the deserialized command to operate in the server;
     */
    public String deserialize();
}
