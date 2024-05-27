package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.network.CentralServer;
import it.polimi.ingsw.network.Commands.*;
import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.VirtualServer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIServer implements VirtualServer {
    public static final String CANONICAL_NAME = "CODEX_RMIServer";
    public final int REGISTRY_PORT;
    private final CentralServer serverRef;
    private final Registry registry;

    /**
     * Instantiates the RMIServer and binds it to the registry
     * @throws RemoteException on registry binding failure
     */
    public RMIServer(int connectionPort) throws RemoteException {
        serverRef = CentralServer.getSingleton();
        this.REGISTRY_PORT = connectionPort;
        VirtualServer stub = (VirtualServer) UnicastRemoteObject.exportObject(this, 0);
        this.registry = LocateRegistry.createRegistry(REGISTRY_PORT);
        registry.rebind(CANONICAL_NAME, stub);
        System.out.println("RMI server waiting for client on port "+ connectionPort +"...");
    }


    private void validateClient(String nickname, VirtualClient client) throws IllegalStateException{
        if(!client.equals(serverRef.getClientFromNickname(nickname)))
            throw new IllegalStateException("Illegal request, wrong client!");
    }

    @Override
    public void connect(String nickname, VirtualClient client) throws IllegalStateException, RemoteException{
        serverRef.connect(nickname, client);
        serverRef.updateMsg(nickname + " has connected");
    }

    @Override
    public void setNumOfPlayers(String nickname, VirtualClient client, int num) throws RemoteException {
        validateClient(nickname, client);
        SetNumOfPlayersCmd command = new SetNumOfPlayersCmd(serverRef.getGameRef(), nickname, num);
        serverRef.issueGameCommand(command);
    }

    @Override
    public void disconnect(String nickname, VirtualClient client) throws IllegalStateException, IllegalArgumentException, RemoteException {
        serverRef.disconnect(nickname, client);
        serverRef.updateMsg(nickname + " has disconnected");
    }

    @Override
    public void placeStartCard(String nickname, VirtualClient client, boolean placeOnFront) throws RemoteException {
        validateClient(nickname, client);
        PlaceStartingCmd command = new PlaceStartingCmd(serverRef.getGameRef(), nickname, placeOnFront);
        serverRef.issueGameCommand(command);
    }
    @Override
    public void chooseColor(String nickname, VirtualClient client, char colour) throws RemoteException{
        validateClient(nickname, client);
        ChooseColorCmd command = new ChooseColorCmd(serverRef.getGameRef(), nickname, PlayerColor.parseColour(colour) );
        serverRef.issueGameCommand(command);
    }
    @Override
    public void chooseObjective(String nickname, VirtualClient client, int choice) throws RemoteException {
        validateClient(nickname, client);
        ChooseObjCmd command = new ChooseObjCmd(serverRef.getGameRef(), nickname, choice);
        serverRef.issueGameCommand(command);
    }

    @Override
    public void placeCard(String nickname, VirtualClient client, String cardID,int row,
                          int col, String cornerDir, boolean placeOnFront) throws RemoteException {
        validateClient(nickname, client);
        PlacePlayCmd command = new PlacePlayCmd(serverRef.getGameRef(), nickname, cardID,
                new Point(row,col), CornerDirection.getDirectionFromString(cornerDir), placeOnFront);
        serverRef.issueGameCommand(command);
    }

    @Override
    public void draw(String nickname, VirtualClient client, char deck, int card) throws RemoteException {
        validateClient(nickname, client);
        DrawCmd command = new DrawCmd(serverRef.getGameRef(), nickname, deck, card);
        serverRef.issueGameCommand(command);
    }

    @Override
    public void restartGame(String nickname, VirtualClient client, int numOfPlayers) throws RemoteException {
        validateClient(nickname, client);
        RestartGameCmd command = new RestartGameCmd(serverRef.getGameRef(), nickname, numOfPlayers);
        serverRef.issueGameCommand(command);
    }


    @Override
    public void sendMsg(String nickname, VirtualClient client, String message) throws RemoteException{
        validateClient(nickname, client);
        String fullMessage = nickname + ": " + message;
        System.out.println(fullMessage);
        serverRef.updateMsg(fullMessage);
    }

    @Override
    public void ping() throws RemoteException{
        return;
    }


    public void closeServer() throws RemoteException, NotBoundException {
        registry.unbind(CANONICAL_NAME);
        UnicastRemoteObject.unexportObject(registry, true);
    }
}
