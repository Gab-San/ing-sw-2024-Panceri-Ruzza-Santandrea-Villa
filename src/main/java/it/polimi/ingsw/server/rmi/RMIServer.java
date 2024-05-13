package it.polimi.ingsw.server.rmi;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.PlayerColor;
import it.polimi.ingsw.server.CentralServer;
import it.polimi.ingsw.server.Commands.*;
import it.polimi.ingsw.server.VirtualServer;
import it.polimi.ingsw.server.VirtualClient;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIServer implements VirtualServer {
    public static final String CANONICAL_NAME = "CODEX_RMIServer";
    public static final int REGISTRY_PORT = 1234;
    CentralServer serverRef;

    /**
     * Instantiates the RMIServer and binds it to the registry
     * @throws RemoteException on registry binding failure
     */
    public RMIServer() throws RemoteException {
        serverRef = CentralServer.getSingleton();

        VirtualServer stub = (VirtualServer) UnicastRemoteObject.exportObject(this, 0);
        Registry registry = LocateRegistry.createRegistry(REGISTRY_PORT);
        registry.rebind(CANONICAL_NAME, stub);
    }

    private void validateClient(String nickname, VirtualClient client) throws IllegalStateException{
        if(!client.equals(serverRef.getClientFromNickname(nickname)))
            throw new IllegalStateException("Illegal request, wrong client!");
    }
    private void issueCommand(GameCommand command) throws IllegalStateException{
        try {
            serverRef.issueGameCommand(command);
        }catch (InterruptedException e) {
            throw new IllegalStateException("Couldn't issue command");
        }
    }

    @Override
    public void connect(String nickname, VirtualClient client) throws IllegalStateException {
        serverRef.connect(nickname, client);
        serverRef.updateMsg(nickname + " has connected");
    }

    @Override
    public void setNumOfPlayers(String nickname, VirtualClient client, int num) throws IllegalStateException {
        validateClient(nickname, client);
        SetNumOfPlayersCmd command = new SetNumOfPlayersCmd(serverRef.getGameRef(), nickname, num);
        issueCommand(command);
    }

    @Override
    public void disconnect(String nickname, VirtualClient client) throws IllegalStateException {
        serverRef.disconnect(nickname, client);
        serverRef.updateMsg(nickname + " has disconnected");
    }

    @Override
    public void placeStartCard(String nickname, VirtualClient client, boolean placeOnFront) throws IllegalStateException {
        validateClient(nickname, client);
        PlaceStartingCmd command = new PlaceStartingCmd(serverRef.getGameRef(), nickname, placeOnFront);
        issueCommand(command);
    }
    @Override
    public void chooseColor(String nickname, VirtualClient client, char colour) throws IllegalStateException{
        validateClient(nickname, client);
        ChooseColorCmd command = new ChooseColorCmd(serverRef.getGameRef(), nickname, PlayerColor.parseColour(colour) );
        issueCommand(command);
    }
    @Override
    public void chooseObjective(String nickname, VirtualClient client, int choice) throws IllegalStateException {
        validateClient(nickname, client);
        ChooseObjCmd command = new ChooseObjCmd(serverRef.getGameRef(), nickname, choice);
        issueCommand(command);
    }

    @Override
    public void placeCard(String nickname, VirtualClient client, String cardID, Point placePos, CornerDirection cornerDir, boolean placeOnFront) throws IllegalStateException {
        validateClient(nickname, client);
        PlacePlayCmd command = new PlacePlayCmd(serverRef.getGameRef(), nickname, cardID, placePos, cornerDir, placeOnFront);
        issueCommand(command);
    }

    @Override
    public void draw(String nickname, VirtualClient client, char deck, int card) throws IllegalStateException {
        validateClient(nickname, client);
        DrawCmd command = new DrawCmd(serverRef.getGameRef(), nickname, deck, card);
        issueCommand(command);
    }

    @Override
    public void startGame(String nickname, VirtualClient client, int numPlayers) throws IllegalStateException{
        validateClient(nickname, client);
        StartGameCmd command = new StartGameCmd(serverRef.getGameRef(), nickname, numPlayers);
        issueCommand(command);
    }

    @Override
    public void sendMsg(String nickname, VirtualClient client, String message) {
        validateClient(nickname, client);
        String fullMessage = nickname + "> " + message;
        System.out.println(fullMessage);
        serverRef.updateMsg(fullMessage);
    }
    @Override
    public void testCmd(String nickname, VirtualClient client, String text){
        validateClient(nickname, client);
        TestCmd command = new TestCmd(serverRef.getGameRef(), nickname, text);
        issueCommand(command);
    }
    @Override
    public void ping(){
        return;
    }
}
