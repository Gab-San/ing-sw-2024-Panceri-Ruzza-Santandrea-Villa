package it.polimi.ingsw.server;

import it.polimi.ingsw.server.rmi.RMIClient;
import it.polimi.ingsw.server.rmi.RMIServer;
import it.polimi.ingsw.server.tcp.client.TCPClientSocket;
import it.polimi.ingsw.server.tcp.server.TCPServerSocket;
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
        rmiServer.closeServer();
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
}
