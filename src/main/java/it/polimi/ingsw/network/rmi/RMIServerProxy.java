package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.network.CommandPassthrough;
import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.VirtualServer;

import java.rmi.RemoteException;

public class RMIServerProxy implements CommandPassthrough {
    private final VirtualServer server;
    private final VirtualClient client;
    private String nickname;

    public RMIServerProxy(VirtualClient client, VirtualServer server){
        this.server = server;
        this.client = client;
    }


    @Override
    public void sendMsg(String addressee, String msg) throws RemoteException {
        //System.out.println("Sending Message: " + msg);
        validateConnection();
        server.sendMsg(nickname, client, addressee, msg);
    }

    private void validateConnection() throws IllegalStateException, RemoteException{
        if(nickname == null){
            throw new IllegalStateException("Please connect to server before sending commands other than 'connect'.");
        }
        else ping(); // throws RemoteException on connection lost.
    }

    @Override
    public void connect(String nickname) throws IllegalStateException, RemoteException {
        server.connect(nickname, client);
        this.nickname = nickname;
    }
    @Override
    public void disconnect() throws IllegalStateException, RemoteException {
        validateConnection();
        server.disconnect(nickname, client);
    }

    @Override
    public void setNumOfPlayers(int num) throws IllegalStateException, RemoteException {
        validateConnection();
        server.setNumOfPlayers(nickname, client, num);
    }
    @Override
    public void placeStartCard(boolean placeOnFront) throws IllegalStateException, RemoteException {
        validateConnection();
        server.placeStartCard(nickname, client, placeOnFront);
    }
    @Override
    public void chooseColor(char color) throws IllegalStateException, RemoteException{
        validateConnection();
        server.chooseColor(nickname, client, color);
    }
    @Override
    public void chooseObjective(int choice) throws IllegalStateException, RemoteException {
        validateConnection();
        server.chooseObjective(nickname, client, choice);
    }

    @Override
    public void placeCard(String cardID, Point placePos, String cornerDir, boolean placeOnFront) throws IllegalStateException, RemoteException {
        validateConnection();
        server.placeCard(nickname, client, cardID, placePos.row(),
                placePos.col(), cornerDir, placeOnFront);
    }
    @Override
    public void draw(char deck, int card) throws IllegalStateException, RemoteException {
        validateConnection();
        server.draw(nickname, client, deck, card);
    }

    @Override
    public void restartGame(int numOfPlayers) throws IllegalStateException, RemoteException {
        validateConnection();
        server.restartGame(nickname, client, numOfPlayers);
    }

    @Override
    public void ping() throws RemoteException {
        server.ping();
    }
}
