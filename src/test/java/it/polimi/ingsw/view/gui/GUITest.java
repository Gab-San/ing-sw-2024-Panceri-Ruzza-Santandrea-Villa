package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.network.CommandPassthrough;
import it.polimi.ingsw.stub.StubView;
import it.polimi.ingsw.view.ModelUpdater;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.exceptions.DisconnectException;
import it.polimi.ingsw.view.exceptions.TimeoutException;
import it.polimi.ingsw.view.model.ViewBoard;

import java.rmi.RemoteException;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.*;

class GUITest {
    public static void main(String[] args) {
        View view = new GUI(new CommandPassthrough() {
            @Override
            public void sendMsg(String addressee, String message) throws RemoteException {

            }

            @Override
            public void connect(String nickname) throws IllegalStateException, RemoteException {
                System.out.println("CONNECTING...");
            }

            @Override
            public void setNumOfPlayers(int num) throws RemoteException {

            }

            @Override
            public void disconnect() throws IllegalStateException, IllegalArgumentException, RemoteException {

            }

            @Override
            public void placeStartCard(boolean placeOnFront) throws RemoteException {

            }

            @Override
            public void chooseColor(char colour) throws RemoteException {

            }

            @Override
            public void chooseObjective(int choice) throws RemoteException {

            }

            @Override
            public void placeCard(String cardID, GamePoint placePos, String cornerDir, boolean placeOnFront) throws RemoteException {

            }

            @Override
            public void draw(char deck, int cardPosition) throws RemoteException {

            }

            @Override
            public void restartGame(int numOfPlayers) throws RemoteException {

            }

            @Override
            public void ping() throws RemoteException {

            }
        }, (modelUpdater) -> {return;} , new LinkedBlockingQueue<>());

        try {
            view.run();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        } catch (DisconnectException e) {
            throw new RuntimeException(e);
        }
    }
}