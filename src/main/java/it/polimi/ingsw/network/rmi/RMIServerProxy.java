package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.network.CommandPassthrough;
import it.polimi.ingsw.network.VirtualServer;

import java.rmi.RemoteException;

/**
 * This class implements the command pass through interface using rmi connection protocol and functions as a server proxy.
 *
 * <p>
 *     This class effectively hides the network calls structure and gives more space
 *     to add logic on the client-side.
 * </p>
 */
public class RMIServerProxy implements CommandPassthrough {
    private final VirtualServer server;
    private final RMIClient client;
    private String nickname;

    /**
     * Constructs the rmi proxy server.
     * @param client rmi client bound to this proxy
     * @param server rmi server endpoint of rmi calls
     */
    public RMIServerProxy(RMIClient client, VirtualServer server){
        this.server = server;
        this.client = client;
    }


    @Override
    public void sendMsg(String addressee, String message) throws RemoteException {
        //System.out.println("Sending Message: " + msg);
        validateConnection();
        server.sendMsg(nickname, client, addressee, message);
    }

    /**
     * Validates proxy connection by checking if the client has ever subscribed to the server or by pinging.
     * @throws IllegalStateException if the client has never subscribed to the server
     * @throws RemoteException if an error occurs during connection
     */
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
        client.close();
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
    public void chooseColor(char colour) throws IllegalStateException, RemoteException{
        validateConnection();
        server.chooseColor(nickname, client, colour);
    }
    @Override
    public void chooseObjective(int choice) throws IllegalStateException, RemoteException {
        validateConnection();
        server.chooseObjective(nickname, client, choice);
    }

    @Override
    public void placeCard(String cardID, GamePoint placePos, String cornerDir, boolean placeOnFront) throws IllegalStateException, RemoteException {
        validateConnection();
        server.placeCard(nickname, client, cardID, placePos.row(),
                placePos.col(), cornerDir, placeOnFront);
    }
    @Override
    public void draw(char deck, int cardPosition) throws IllegalStateException, RemoteException {
        validateConnection();
        server.draw(nickname, client, deck, cardPosition);
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
