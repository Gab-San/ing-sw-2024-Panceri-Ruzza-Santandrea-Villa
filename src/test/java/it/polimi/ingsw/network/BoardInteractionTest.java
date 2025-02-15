package it.polimi.ingsw.network;

import it.polimi.ingsw.network.rmi.RMIClient;
import it.polimi.ingsw.network.rmi.RMIServer;
import it.polimi.ingsw.network.tcp.client.TCPClientSocket;
import it.polimi.ingsw.network.tcp.server.TCPServerSocket;
import it.polimi.ingsw.stub.PuppetModelUpdater;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BoardInteractionTest {
    // These tests evaluate whether the calls from the client correctly arrive to the server and
    // are parsed accordingly. There can't always be ways to assert the correct behaviour, so the
    // test result is based solely on the fact that no errors are triggered (Dijkstra theorem).
    private final ExecutorService pool = Executors.newCachedThreadPool();
    private CentralServer centralServer;
    private TCPServerSocket tcpServer;
    private RMIServer rmiServer;
    @BeforeEach
    void resetMySingleton() throws NoSuchFieldException, IllegalAccessException, IOException {
        Field singleton = CentralServer.class.getDeclaredField("singleton");
        singleton.setAccessible(true);
        singleton.set(null, null);
        centralServer = CentralServer.getSingleton();
        tcpServer = new TCPServerSocket(8888);
        rmiServer = new RMIServer(1234);
    }

    @AfterEach
    void close() throws RemoteException, NotBoundException {
        tcpServer.closeServer();
        rmiServer.unbindServer();
    }
    

    private void waitExecution(TCPClientSocket client, int time){
        pool.execute(
                ()->{
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        client.getProxy().disconnect();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
        while (!client.isClosed());
    }

    @Test
    void testSetNumOfPlayers() throws RemoteException, IOException {
        TCPClientSocket client;
        client = new TCPClientSocket("localhost", 8888);

        client.getProxy().connect("Gamba");
        // Cannot check whether the client receives an error
        client.getProxy().setNumOfPlayers(5);
        client.getProxy().setNumOfPlayers(1);
        client.getProxy().setNumOfPlayers(4);
        waitExecution(client,  1000);
    }

    @Test
    @DisplayName("Connecting multiple clients on creation state")
    void testJoinCreationState() throws IOException, NotBoundException {
        TCPClientSocket cli1 = new TCPClientSocket(8888);
        cli1.getProxy().connect("Gamba");
        RMIClient cli2 = new RMIClient(1234);
        assertThrows(
                IllegalStateException.class,
                () -> cli2.getProxy().connect("Giovanni")
        );
    }

    @Test
    void testMultipleJoin() throws IOException, NotBoundException {
        TCPClientSocket cli1 = new TCPClientSocket("localhost", 8888);

        cli1.getProxy().connect("Gamba");
        assertNotNull(centralServer.getClientFromNickname("Gamba"));
        cli1.getProxy().setNumOfPlayers(5);
        cli1.getProxy().setNumOfPlayers(4);

        RMIClient cli2 = new RMIClient(1234);
        cli2.getProxy().connect("Alessandro");
        assertNotNull(centralServer.getClientFromNickname("Alessandro"));

        TCPClientSocket cli3 = new TCPClientSocket("localhost", 8888);
        cli3.getProxy().connect("Flavio");
        assertNotNull(centralServer.getClientFromNickname("Flavio"));

        RMIClient cli4 = new RMIClient( 1234);
        cli4.getProxy().connect("Maddi");
        assertNotNull(centralServer.getClientFromNickname("Maddi"));
        cli1.getProxy().disconnect();
        cli2.getProxy().disconnect();
        cli3.getProxy().disconnect();
        cli4.getProxy().disconnect();

        assertNull(centralServer.getClientFromNickname("Gamba") );
        assertNull(centralServer.getClientFromNickname("Alessandro"));
        assertNull(centralServer.getClientFromNickname("Flavio"));
        assertNull(centralServer.getClientFromNickname("Maddi"));
    }


    @Test
    @DisplayName("Connecting after reaching max players")
    void testConnect() throws IOException, NotBoundException {
        RMIClient client = new RMIClient("localhost", 1234);
        client.setModelUpdater(new PuppetModelUpdater());

        client.getProxy().connect("Salatino");
        client.getProxy().setNumOfPlayers(5);
        client.getProxy().setNumOfPlayers(2);

        TCPClientSocket cli2 = new TCPClientSocket(8888);
        cli2.getProxy().connect("Asdf");

        RMIClient cli3 = new RMIClient(1234);
        assertThrows(
                IllegalStateException.class,
                () -> cli3.getProxy().connect("SUSI")
        );
    }



    @Test
    void testSetNumOfPlayers3() throws RemoteException {
        TCPClientSocket client;
        try {
            client = new TCPClientSocket("localhost", 8888);
        } catch (IOException e){
            throw new RuntimeException(e);
        }


        try {
            client.getProxy().connect("Fuffy");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        client.getProxy().setNumOfPlayers(4);
        waitExecution(client,  1000);
    }

}
