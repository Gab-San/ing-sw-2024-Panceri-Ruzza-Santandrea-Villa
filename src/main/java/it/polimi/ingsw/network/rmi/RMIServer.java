package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.network.CentralServer;
import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.VirtualServer;
import it.polimi.ingsw.network.commands.*;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * This class implements virtual server interface using rmi connection protocol
 */
public class RMIServer implements VirtualServer {
    /**
     * The unique name of the server located on the registry
     */
    public static final String CANONICAL_NAME = "CODEX_RMIServer";
    private final CentralServer serverRef;
    private final Registry registry;

    /**
     * Instantiates the RMIServer and binds it to the registry
     * @param connectionPort port on which rmi connections are processed
     * @throws RemoteException on registry binding failure
     */
    public RMIServer(int connectionPort) throws RemoteException {
        serverRef = CentralServer.getSingleton();
        VirtualServer stub = (VirtualServer) UnicastRemoteObject.exportObject(this, 0);
        this.registry = LocateRegistry.createRegistry(connectionPort);
        registry.rebind(CANONICAL_NAME, stub);
        System.out.println("RMI server waiting for client on port "+ connectionPort +"...");
    }

    /**
     * Client validation server-side.
     * @param nickname unique id of the user
     * @param client instance of virtual client
     * @throws IllegalStateException if client instance is different from the one of the connected client (security check)
     */
    private void validateClient(String nickname, VirtualClient client) throws IllegalStateException{
        if(!client.equals(serverRef.getClientFromNickname(nickname)))
            throw new IllegalStateException("Illegal request, wrong client!");
    }

    @Override
    public void connect(String nickname, VirtualClient client) throws IllegalStateException, RemoteException{
        serverRef.connect(nickname, client);
    }

    @Override
    public void setNumOfPlayers(String nickname, VirtualClient client, int num) throws RemoteException {
        validateClient(nickname, client);
        SetNumOfPlayersCmd command = new SetNumOfPlayersCmd(serverRef.getGameController(), nickname, num);
        serverRef.issueGameCommand(command);
    }

    @Override
    public void disconnect(String nickname, VirtualClient client) throws IllegalStateException, IllegalArgumentException, RemoteException {
        serverRef.disconnect(nickname, client);
    }

    @Override
    public void placeStartCard(String nickname, VirtualClient client, boolean placeOnFront) throws RemoteException {
        validateClient(nickname, client);
        PlaceStartingCmd command = new PlaceStartingCmd(serverRef.getGameController(), nickname, placeOnFront);
        serverRef.issueGameCommand(command);
    }
    @Override
    public void chooseColor(String nickname, VirtualClient client, char colour) throws RemoteException{
        validateClient(nickname, client);
        ChooseColorCmd command = new ChooseColorCmd(serverRef.getGameController(), nickname, PlayerColor.parseColour(colour) );
        serverRef.issueGameCommand(command);
    }
    @Override
    public void chooseObjective(String nickname, VirtualClient client, int choice) throws RemoteException {
        validateClient(nickname, client);
        ChooseObjCmd command = new ChooseObjCmd(serverRef.getGameController(), nickname, choice);
        serverRef.issueGameCommand(command);
    }

    @Override
    public void placeCard(String nickname, VirtualClient client, String cardID,int row,
                          int col, String cornerDir, boolean placeOnFront) throws RemoteException {
        validateClient(nickname, client);
        PlacePlayCmd command = new PlacePlayCmd(serverRef.getGameController(), nickname, cardID,
                new GamePoint(row,col), CornerDirection.getDirectionFromString(cornerDir), placeOnFront);
        serverRef.issueGameCommand(command);
    }

    @Override
    public void draw(String nickname, VirtualClient client, char deck, int cardPosition) throws RemoteException {
        validateClient(nickname, client);
        DrawCmd command = new DrawCmd(serverRef.getGameController(), nickname, deck, cardPosition);
        serverRef.issueGameCommand(command);
    }

    @Override
    public void restartGame(String nickname, VirtualClient client, int numOfPlayers) throws RemoteException {
        validateClient(nickname, client);
        RestartGameCmd command = new RestartGameCmd(serverRef.getGameController(), nickname, numOfPlayers);
        serverRef.issueGameCommand(command);
    }


    @Override
    public void sendMsg(String nickname, VirtualClient client, String addressee, String message) throws RemoteException{
        validateClient(nickname, client);
        serverRef.sendMessage(nickname, addressee, message);
    }

    @Override
    public void ping() throws RemoteException{
        return;
    }

    /**
     * Unbinds the server if possible.
     * @throws RemoteException f remote communication with the registry failed; if exception is a
     * ServerException containing an AccessException, then the registry denies the caller access
     * to perform this operation (if originating from a non-local host, for example)
     * @throws NotBoundException if name is not currently bound
     */
    public void unbindServer() throws RemoteException, NotBoundException {
        registry.unbind(CANONICAL_NAME);
        UnicastRemoteObject.unexportObject(registry, true);
    }
}
