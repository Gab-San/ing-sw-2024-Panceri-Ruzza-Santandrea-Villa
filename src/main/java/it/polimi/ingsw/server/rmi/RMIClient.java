package it.polimi.ingsw.server.rmi;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.server.*;
import it.polimi.ingsw.server.VirtualClient;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class RMIClient extends UnicastRemoteObject implements VirtualClient, CommandPassthrough {
    private String nickname = null;
    private final VirtualServer server;

    public RMIClient() throws RemoteException, NotBoundException{
        this("localhost");
    }
    public RMIClient(String registryIP) throws RemoteException, NotBoundException {
        super();
        Registry registry = LocateRegistry.getRegistry(registryIP, RMIServer.REGISTRY_PORT);
        server = (VirtualServer) registry.lookup(RMIServer.CANONICAL_NAME);
    }

    @Override
    public void update(String msg) throws RemoteException {
        System.out.println("\n"+msg); // temp function
        System.out.print("> "); // temp function
    }

    /**
     * If a call to this function successfully returns, then this client is still connected
     * @throws RemoteException on connection loss
     */
    @Override
    public void ping() throws RemoteException {
        return;
    }

    //TODO In case remove this
    @Override
    public void sendMsg(String msg) throws RemoteException {
        //System.out.println("Sending Message: " + msg);
        validateConnection();
        server.sendMsg(nickname, this, msg);
    }
    @Override
    public void testCmd(String text) throws RemoteException{
        validateConnection();
        server.testCmd(nickname, this, text);
    }

    private void validateConnection() throws IllegalStateException, RemoteException{
        if(nickname == null){
            throw new IllegalStateException("Please connect to server before sending commands other than 'connect'.");
        }
        else server.ping(); // throws RemoteException on connection lost.
    }

    @Override
    public void connect(String nickname) throws IllegalStateException, RemoteException {
        server.connect(nickname, this);
        this.nickname = nickname;
    }
    @Override
    public void disconnect() throws IllegalStateException, RemoteException {
        validateConnection();
        server.disconnect(nickname, this);
        this.nickname = null;
    }

    @Override
    public void setNumOfPlayers(int num) throws IllegalStateException, RemoteException {
        validateConnection();
        server.setNumOfPlayers(nickname, this, num);
    }
    @Override
    public void placeStartCard(boolean placeOnFront) throws IllegalStateException, RemoteException {
        validateConnection();
        server.placeStartCard(nickname, this, placeOnFront);
    }
    @Override
    public void chooseColor(char color) throws IllegalStateException, RemoteException{
        validateConnection();
        server.chooseColor(nickname, this, color);
    }
    @Override
    public void chooseObjective(int choice) throws IllegalStateException, RemoteException {
        validateConnection();
        server.chooseObjective(nickname, this, choice);
    }

    @Override
    public void placeCard(String cardID, Point placePos, CornerDirection cornerDir, boolean placeOnFront) throws IllegalStateException, RemoteException {
        validateConnection();
        server.placeCard(nickname, this, cardID, placePos, cornerDir, placeOnFront);
    }
    @Override
    public void draw(char deck, int card) throws IllegalStateException, RemoteException {
        validateConnection();
        server.draw(nickname, this, deck, card);
    }

    @Override
    public void startGame() throws IllegalStateException, RemoteException {
        validateConnection();
        server.startGame(nickname, this);
    }

    public static void main(String[] args) throws RemoteException, NotBoundException {
        System.err.println("CLIENT TO BE RUN ONLY FOR TEST PURPOSES");
        if(args.length < 1) {
            args = new String[1];
            args[0] = "localhost";
        }

        RMIClient client = new RMIClient(args[0]);
        // TODO correct
        Parser parser = new Parser(client, new ModelView());

        String input = "";
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter command\n> ");
        while (!input.split(" ")[0].equalsIgnoreCase("quit")){
            try{
                input = scanner.nextLine();
                parser.parseCommand(input);
            }catch (RemoteException e){
                System.err.println("Connection lost. Message: " + e.getMessage());
            }catch (IndexOutOfBoundsException e){
                System.err.println("Too few arguments.");
            }catch (IllegalStateException e){
                System.err.println("Cannot execute command. Message: " + e.getMessage());
            }catch (IllegalArgumentException e){
                System.err.println("Command invalid. Message: " + e.getMessage());
            }
        }
        System.exit(0);
    }
}
