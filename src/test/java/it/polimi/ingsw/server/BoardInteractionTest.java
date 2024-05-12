package it.polimi.ingsw.server;

import it.polimi.ingsw.server.rmi.RMIClient;
import it.polimi.ingsw.server.rmi.RMIServer;
import it.polimi.ingsw.server.tcp.TCPClient;
import it.polimi.ingsw.server.tcp.TCPServer;
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
    private TCPServer tcpServer;
    private RMIServer rmiServer;
    @BeforeEach
    void resetMySingleton() throws NoSuchFieldException, IllegalAccessException, IOException {
        Field singleton = CentralServer.class.getDeclaredField("singleton");
        singleton.setAccessible(true);
        singleton.set(null, null);
        centralServer = CentralServer.getSingleton();
        tcpServer = new TCPServer(8888);
        rmiServer = new RMIServer(1234);
    }

    @AfterEach
    void close() throws RemoteException, NotBoundException {
        tcpServer.closeServer();
        rmiServer.closeServer();
    }

    private void validateClient(TCPClient client){
        while (client.getNickname() == null){
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void waitExecution(TCPClient client, int time){
        pool.execute(
                ()->{
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        client.disconnect();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
        while (!client.isClosed());
    }

    @Test
    void testSetNumOfPlayers() throws RemoteException, IOException {
        TCPClient client;
        client = new TCPClient("localhost", 8888);

        client.connect("Gamba");
        validateClient(client);
        client.setNumOfPlayers(5);
        client.setNumOfPlayers(1);
        client.setNumOfPlayers(4);
        waitExecution(client,  1000);
    }

    @Test
    @DisplayName("Connecting multiple clients on creation state")
    void testJoinCreationState() throws IOException, NotBoundException {
        TCPClient cli1 = new TCPClient(8888);
        cli1.connect("Gamba");
        RMIClient cli2 = new RMIClient(1234);
        assertThrows(
                IllegalStateException.class,
                () -> cli2.connect("Giovanni")
        );
    }

    @Test
    void testMultipleJoin() throws IOException, NotBoundException {
        TCPClient cli1 = new TCPClient("localhost", 8888);

        cli1.connect("Gamba");
        validateClient(cli1);
        assertNotNull(centralServer.getClientFromNickname("Gamba"));
        cli1.setNumOfPlayers(5);
        cli1.setNumOfPlayers(4);

        RMIClient cli2 = new RMIClient(1234);
        cli2.connect("Alessandro");
        assertNotNull(centralServer.getClientFromNickname("Alessandro"));

        TCPClient cli3 = new TCPClient("localhost", 8888);
        cli3.connect("Flavio");
        validateClient(cli3);
        assertNotNull(centralServer.getClientFromNickname("Flavio"));

        RMIClient cli4 = new RMIClient( 1234);
        cli4.connect("Maddi");
        assertNotNull(centralServer.getClientFromNickname("Maddi"));
        waitExecution(cli1,  2000);
        cli2.disconnect();
        waitExecution(cli3,  0);
        cli4.disconnect();

        assertNull( centralServer.getClientFromNickname("Gamba") );
        assertNull(centralServer.getClientFromNickname("Alessandro"));
        assertNull(centralServer.getClientFromNickname("Flavio"));
        assertNull(centralServer.getClientFromNickname("Maddi"));
    }


    @Test
    @DisplayName("Connecting after reaching max players")
    void testConnect() throws IOException, NotBoundException {
        RMIClient client = new RMIClient("localhost", 1234);

        client.connect("Salatino");
        client.setNumOfPlayers(5);
        client.setNumOfPlayers(2);

        TCPClient cli2 = new TCPClient(8888);
        cli2.connect("Asdf");

        RMIClient cli3 = new RMIClient(1234);
        assertThrows(
                IllegalStateException.class,
                () -> cli3.connect("SUSI")
        );
    }
}
