package it.polimi.ingsw.server.tcp;

import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.tcp.message.SendMessage;
import it.polimi.ingsw.server.tcp.message.TCPServerErrorMessage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;

public class ClientProxy implements VirtualClient {

    private final ObjectOutputStream outputStream;
    private final ClientHandler clientHandler;
    private String nickname;
    public ClientProxy(ClientHandler clientHandler,ObjectOutputStream outputStream) {
        this.clientHandler = clientHandler;
        this.outputStream = outputStream;
    }


    @Override
    public void update(String msg) throws RemoteException {
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
        //TODO implement ping
    }

    void setUsername(String nickname){
        this.nickname = nickname;
    }

    void updateError(TCPServerErrorMessage message){
        try{
            System.out.println("UPDATING ERROR");
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException e) {
            clientHandler.closeSocket();
        }
    }
}
