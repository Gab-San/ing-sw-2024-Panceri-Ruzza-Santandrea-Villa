package it.polimi.ingsw.server.testingStub;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.server.CommandPassthrough;

import java.rmi.RemoteException;

public class PuppetClient implements CommandPassthrough {

    @Override
    public void sendMsg(String msg) throws RemoteException {

    }

    @Override
    public void testCmd(String text) throws RemoteException {

    }

    @Override
    public void connect(String nickname) throws IllegalStateException, RemoteException {

    }

    @Override
    public void setNumOfPlayers(int num) throws IllegalStateException, RemoteException {

    }

    @Override
    public void disconnect() throws IllegalStateException, RemoteException {

    }

    @Override
    public void placeStartCard(boolean placeOnFront) throws IllegalStateException, RemoteException {

    }

    @Override
    public void chooseColor(char color) throws IllegalStateException, RemoteException {

    }

    @Override
    public void chooseObjective(int choice) throws IllegalStateException, RemoteException {

    }

    @Override
    public void placeCard(String cardID, Point placePos, String cornerDir, boolean placeOnFront) throws IllegalStateException, RemoteException {

    }

    @Override
    public void draw(char deck, int card) throws IllegalStateException, RemoteException {

    }

    @Override
    public void startGame(int numOfPlayers) throws IllegalStateException, RemoteException {

    }
}
