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
        client.getProxy().connect("Ale");
        client.getProxy().sendMsg("Sono Ale sto mandando un messaggio");
    }

    @Test
    void connect() throws NotBoundException, RemoteException {
        RMIClient client = new RMIClient(1234);
        client.getProxy().connect("Gamba");
        assertDoesNotThrow(
                client.getProxy()::disconnect
        );
    }

    @Test
    void connectError() throws NotBoundException, RemoteException {
        RMIClient client = new RMIClient(1234);
        client.getProxy().connect("Gamba");
        assertThrows(
                IllegalStateException.class,
                () -> client.getProxy().connect("Giovanni")
        );
    }
    @Test
    void connectError2() throws NotBoundException, RemoteException {
        RMIClient client = new RMIClient(1234);
        client.getProxy().connect("Ale");
        RMIClient cli2 = new RMIClient(1234);
        assertThrows(
                IllegalStateException.class,
                () -> cli2.getProxy().connect("Ale")
        );
    }

    @Test
    void connectError3() throws NotBoundException, RemoteException {
        RMIClient client = new RMIClient(1234);
        client.getProxy().connect("Ale");
        RMIClient cli2 = new RMIClient(1234);
        assertThrows(
                IllegalStateException.class,
                () -> cli2.getProxy().connect("Gamba")
        );
    }

    @Test
    void disconnect() throws NotBoundException, RemoteException {
        RMIClient client = new RMIClient(1234);
        assertThrows(
                IllegalStateException.class,
                client.getProxy()::disconnect
        );
        client.getProxy().connect("Giovanni");
        client.getProxy().disconnect();
        assertDoesNotThrow(
                () -> client.getProxy().connect("Giovanni")
        );
    }

    @Test
    void setNumOfPlayers() throws NotBoundException, RemoteException {
        RMIClient client = new RMIClient(1234);
        client.getProxy().connect("Ale");
        client.getProxy().setNumOfPlayers(2);
    }

    @Test
    void placeStartCard() throws NotBoundException, RemoteException {
        RMIClient client = new RMIClient(1234);
        client.getProxy().connect("Ale");
        client.getProxy().setNumOfPlayers(2);
        RMIClient cli2 = new RMIClient(1234);
        cli2.getProxy().connect("Gamba");
        client.getProxy().placeStartCard(true);
        cli2.getProxy().placeStartCard(true);
    }

    @Test
    void chooseColor() throws NotBoundException, RemoteException {
        RMIClient client = new RMIClient(1234);
        client.getProxy().connect("Ale");
        client.getProxy().setNumOfPlayers(2);
        RMIClient cli2 = new RMIClient(1234);
        cli2.getProxy().connect("Gamba");
        client.getProxy().placeStartCard(true);
        cli2.getProxy().placeStartCard(true);
        client.getProxy().chooseColor('B');
        cli2.getProxy().chooseColor('B');
        cli2.getProxy().chooseColor('R');
    }

    @Test
    void chooseObjective() throws NotBoundException, RemoteException {
        RMIClient client = new RMIClient(1234);
        client.getProxy().connect("Ale");
        client.getProxy().setNumOfPlayers(2);
        RMIClient cli2 = new RMIClient(1234);
        cli2.getProxy().connect("Gamba");
        client.getProxy().placeStartCard(true);
        cli2.getProxy().placeStartCard(true);
        client.getProxy().chooseColor('B');
        cli2.getProxy().chooseColor('B');
        cli2.getProxy().chooseColor('R');
        client.getProxy().chooseObjective(0);
        client.getProxy().chooseObjective(2);
        cli2.getProxy().chooseObjective(1);
    }

    @Test
    void placeCard() throws NotBoundException, RemoteException {
        RMIClient client = new RMIClient(1234);
        client.getProxy().connect("Claudio");
        client.getProxy().placeCard("S0", new Point(1,1), "TL", true);
    }

    @Test
    void draw() throws NotBoundException, RemoteException {
        RMIClient client = new RMIClient(1234);
        client.getProxy().connect("Davide");
        client.getProxy().draw('R', 0);
    }

    @Test
    void startGame() throws NotBoundException, RemoteException {
        RMIClient client = new RMIClient(1234);
        client.getProxy().connect("Stupido");
        client.getProxy().restartGame(3);
    }
}