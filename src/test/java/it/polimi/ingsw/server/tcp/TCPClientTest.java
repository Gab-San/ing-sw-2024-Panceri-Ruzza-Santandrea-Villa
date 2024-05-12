package it.polimi.ingsw.server.tcp;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.server.CentralServer;
import it.polimi.ingsw.server.tcp.testingStub.PuppetServer;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TCPClientTest {
    private final ExecutorService pool = Executors.newCachedThreadPool();
    private TCPServer server;

    @BeforeEach
    void resetMySingleton() throws NoSuchFieldException, IllegalAccessException, IOException {
        Field singleton = CentralServer.class.getDeclaredField("singleton");
        singleton.setAccessible(true);
        singleton.set(null, null);
        server = new TCPServer(8888);
    }

    @AfterEach
    void close(){
        server.closeServer();
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
    void assertInstantiationException(){
        assertThrows(
                IllegalArgumentException.class,
                () -> new TCPClient("localhost", 80000)
        );

        assertThrows(
                UnknownHostException.class,
                () -> new TCPClient("www.googo", 8000)
        );
    }

    @Test
    void testDisconnect() throws RemoteException {

        TCPClient client;
        try {
            client = new TCPClient("localhost", 8888);
        } catch (IOException e){
            throw new RuntimeException(e);
        }

        try {
            client.connect("Corolla");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        validateClient(client);
        client.disconnect();
        assertTrue(client.isClosed());

        try {
            client = new TCPClient("localhost", 8888);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
        client.connect("Corolla");
        validateClient(client);
        assertEquals(client.getNickname(), "Corolla");
        waitExecution(client,  5000);
        assertTrue(client.isClosed());
    }

    @Test
    @DisplayName("Basic connect testing")
    void testConnect() {
        TCPClient client;
        try {
            client = new TCPClient("localhost", 8888);
        } catch (IOException e){
            throw new RuntimeException(e);
        }

        try {
            client.connect("Gamba");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        waitExecution(client,  2000);
        assertEquals("Gamba", client.getNickname());
    }

    @Test
    @DisplayName("Connecting a second time with a different nickname")
    void testConnect2() throws RemoteException {

        TCPClient client;
        try {
            client = new TCPClient("localhost", 8888);
        } catch (IOException e){
            throw new RuntimeException(e);
        }

        client.connect("Gamba");
        client.connect("Giovanni");

        waitExecution(client, 2000);
        assertNotEquals("Giovanni",client.getNickname());
    }

    @Test
    @DisplayName("Connecting with two clients same nickname")
    void testConnect3() throws IOException, InterruptedException {
        TCPClient cli1 = new TCPClient("localhost", 8888);
        cli1.closeSocket();
        assertThrows(
                RemoteException.class,
                () -> cli1.connect("Gamba")
        );
        waitExecution(cli1, 2000);
        assertNull(cli1.getNickname());
    }

    @Test
    @DisplayName("Closing socket before connect check message")
    void testConnect4() throws IOException {
        TCPClient client = new TCPClient("localhost", 8888);
        client.connect("Gianni");
        client.closeSocket();
        assertThrows(
                IllegalStateException.class,
                () -> client.testCmd("SUS")
        );
    }

    @Test
    @DisplayName("Closing socket before connect")
    void testConnect5() throws IOException {
        TCPClient client = new TCPClient("localhost", 8888);
        client.connect("Gianni");
        client.closeSocket();
        assertThrows(
                IllegalStateException.class,
                () -> client.testCmd("SUS")
        );
    }

    @Test
    @DisplayName("Pinging after closing server socket")
    void testRemoteException() throws IOException {
        PuppetServer server = new PuppetServer(10000);

        TCPClient client = new TCPClient("localhost", 10000);
        pool.execute(
                server::closeServer
        );
        assertThrows(
                RemoteException.class,
                client::ping
        );
    }



    @Test
    void testCmd() throws IOException {
        TCPClient client = new TCPClient("localhost", 8888);

        client.connect("Giovanni");
        validateClient(client);
        client.testCmd("SONO GIOVANNI STO TESTANDO");
        waitExecution(client, 1500);
    }

    @Test
    void testSendMsg() throws IOException {
        TCPClient client = new TCPClient("localhost", 8888);
        client.connect("Giacomo");
        validateClient(client);
        client.sendMsg("Ciao ragazzi");
        client.sendMsg("Come va?");
        waitExecution(client, 5000);
    }


    @Test
    void testChooseColor() throws IOException {
        TCPClient client = new TCPClient("localhost", 8888);
        client.connect("Giacomo");
        validateClient(client);
        client.chooseColor('b');
        waitExecution(client, 2000);
    }

    @Test
    void testChooseObjective() throws IOException {
        TCPClient client = new TCPClient("localhost", 8888);
        client.connect("Giacomo");
        validateClient(client);
        client.chooseObjective(2);
        waitExecution(client, 2000);
    }

    @Test
    void testDraw() throws IOException {
        TCPClient client = new TCPClient("localhost", 8888);
        client.connect("Giacomo");
        validateClient(client);
        client.draw('r', 0);
        waitExecution(client, 2000);
    }


    @Test
    void testStartGame() throws IOException {
        TCPClient client = new TCPClient("localhost", 8888);
        client.connect("Giacomo");
        validateClient(client);
        client.startGame();
        waitExecution(client, 5000);
    }

    @Test
    void testPlaceStartingCard() throws IOException {
        TCPClient client = new TCPClient("localhost", 8888);
        client.connect("Giacomo");
        validateClient(client);
        client.placeStartCard(true);
        waitExecution(client, 2000);
    }

    @Test
    void testPlaceCard() throws IOException {
        TCPClient client = new TCPClient("localhost", 8888);
        client.connect("Giacomo");
        validateClient(client);
        client.placeCard("R2", new Point(1,1), "TR", true);
        waitExecution(client, 2000);
    }

}