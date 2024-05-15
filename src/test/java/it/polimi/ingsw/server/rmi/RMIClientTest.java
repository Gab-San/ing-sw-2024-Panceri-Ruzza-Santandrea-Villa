package it.polimi.ingsw.server.rmi;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.server.CentralServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RMIClientTest {

    private RMIServer server;

    @BeforeEach
    void resetMySingleton() throws NoSuchFieldException, IllegalAccessException, IOException {
        Field singleton = CentralServer.class.getDeclaredField("singleton");
        singleton.setAccessible(true);
        singleton.set(null, null);
        server = new RMIServer(1234);
    }

    @AfterEach
    void close() throws RemoteException, NotBoundException {
        server.closeServer();
    }

    @Test
    void update() throws NotBoundException, RemoteException {
        RMIClient client = new RMIClient(1234);
        client.update("Si va a letto!");
    }

    @Test
    void sendMsg() throws NotBoundException, RemoteException {
        RMIClient client = new RMIClient(1234);
        client.connect("Ale");
        client.sendMsg("Sono Ale sto mandando un messaggio");
    }


    @Test
    void connect() throws NotBoundException, RemoteException {
        RMIClient client = new RMIClient(1234);
        client.connect("Gamba");
        assertDoesNotThrow(
                client::disconnect
        );
    }

    @Test
    void connectError() throws NotBoundException, RemoteException {
        RMIClient client = new RMIClient(1234);
        client.connect("Gamba");
        assertThrows(
                IllegalStateException.class,
                () -> client.connect("Giovanni")
        );
    }
    @Test
    void connectError2() throws NotBoundException, RemoteException {
        RMIClient client = new RMIClient(1234);
        client.connect("Ale");
        RMIClient cli2 = new RMIClient(1234);
        assertThrows(
                IllegalStateException.class,
                () -> cli2.connect("Ale")
        );
    }

    @Test
    void connectError3() throws NotBoundException, RemoteException {
        RMIClient client = new RMIClient(1234);
        client.connect("Ale");
        RMIClient cli2 = new RMIClient(1234);
        assertThrows(
                IllegalStateException.class,
                () -> cli2.connect("Gamba")
        );
    }

    @Test
    void disconnect() throws NotBoundException, RemoteException {
        RMIClient client = new RMIClient(1234);
        assertThrows(
                IllegalStateException.class,
                client::disconnect
        );
        client.connect("Giovanni");
        client.disconnect();
        assertDoesNotThrow(
                () -> client.connect("Giovanni")
        );
    }

    @Test
    void setNumOfPlayers() throws NotBoundException, RemoteException {
        RMIClient client = new RMIClient(1234);
        client.connect("Ale");
        client.setNumOfPlayers(2);
    }

    @Test
    void placeStartCard() throws NotBoundException, RemoteException {
        RMIClient client = new RMIClient(1234);
        client.connect("Ale");
        client.setNumOfPlayers(2);
        RMIClient cli2 = new RMIClient(1234);
        cli2.connect("Gamba");
        client.placeStartCard(true);
        cli2.placeStartCard(true);
    }

    @Test
    void chooseColor() throws NotBoundException, RemoteException {
        RMIClient client = new RMIClient(1234);
        client.connect("Ale");
        client.setNumOfPlayers(2);
        RMIClient cli2 = new RMIClient(1234);
        cli2.connect("Gamba");
        client.placeStartCard(true);
        cli2.placeStartCard(true);
        client.chooseColor('B');
        cli2.chooseColor('B');
        cli2.chooseColor('R');
    }

    @Test
    void chooseObjective() throws NotBoundException, RemoteException {
        RMIClient client = new RMIClient(1234);
        client.connect("Ale");
        client.setNumOfPlayers(2);
        RMIClient cli2 = new RMIClient(1234);
        cli2.connect("Gamba");
        client.placeStartCard(true);
        cli2.placeStartCard(true);
        client.chooseColor('B');
        cli2.chooseColor('B');
        cli2.chooseColor('R');
        client.chooseObjective(0);
        client.chooseObjective(2);
        cli2.chooseObjective(1);
    }

    @Test
    void placeCard() throws NotBoundException, RemoteException {
        RMIClient client = new RMIClient(1234);
        client.connect("Claudio");
        client.placeCard("S0", new Point(1,1), "TL", true);
    }

    @Test
    void draw() throws NotBoundException, RemoteException {
        RMIClient client = new RMIClient(1234);
        client.connect("Davide");
        client.draw('R', 0);
    }

    @Test
    void startGame() throws NotBoundException, RemoteException {
        RMIClient client = new RMIClient(1234);
        client.connect("Stupido");
        client.startGame(3);
    }
}