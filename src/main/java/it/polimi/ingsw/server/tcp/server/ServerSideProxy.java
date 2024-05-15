package it.polimi.ingsw.server.tcp.server;

import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.tcp.message.SendMessage;
import it.polimi.ingsw.server.tcp.message.TCPServerCheckMessage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;

public class ServerSideProxy implements VirtualClient {

    private final ObjectOutputStream outputStream;
    private final ClientHandler clientHandler;
    private String nickname;
    public ServerSideProxy(ClientHandler clientHandler, ObjectOutputStream outputStream) {
        this.clientHandler = clientHandler;
        this.outputStream = outputStream;
    }


    @Override
    public void update(String msg) throws RemoteException {
        ping();
        try{
            outputStream.writeObject(new SendMessage(nickname, msg));
            outputStream.flush();
        } catch (IOException e) {
            clientHandler.closeSocket();
            throw new RemoteException("Connection Lost " + e.getMessage());
        }
    }

    @Override
    public void ping() throws RemoteException {
        clientHandler.ping();
    }

    void setUsername(String nickname){
        this.nickname = nickname;
    }

    public void sendCheck(TCPServerCheckMessage message){
        try{
            ping();
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException e) {
            clientHandler.closeSocket();
        }
    }
}
