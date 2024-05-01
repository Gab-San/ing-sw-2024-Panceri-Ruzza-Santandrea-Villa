package it.polimi.ingsw.server;

//TODO: create the actual VirtualClient
public abstract class VirtualClient {
    public abstract void update() throws ConnectionLostException;
    public abstract void ping() throws ConnectionLostException;
}
