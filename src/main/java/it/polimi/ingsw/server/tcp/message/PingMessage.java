package it.polimi.ingsw.server.tcp.message;

import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.VirtualServer;

import java.io.Serial;
import java.rmi.RemoteException;

public class PingMessage implements TCPClientMessage, TCPServerMessage {
    @Serial
    private static final long serialVersionUID = 0L;
    private final boolean isResponse;
    public PingMessage(){
        this.isResponse = false;
    }

    public PingMessage(boolean isResponse){
        this.isResponse = isResponse;
    }


    @Override
    public void execute(VirtualServer virtualServer, VirtualClient virtualClient) throws RemoteException {
        if(isResponse){
            System.out.println("PONG");
            return;
        }
        virtualServer.ping();
    }

    @Override
    public void execute(VirtualClient virtualClient) throws RemoteException {
        if(isResponse){
            System.out.println("PONG");
            return;
        }
        virtualClient.ping();
    }

    @Override
    public String toString() {
        return "PING";
    }

    @Override
    public boolean isError() {
        return false;
    }
}
