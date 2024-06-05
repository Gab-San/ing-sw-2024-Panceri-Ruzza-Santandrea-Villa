package it.polimi.ingsw.network.tcp.client;

import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.network.CentralServer;
import it.polimi.ingsw.network.tcp.server.TCPServerSocket;
import it.polimi.ingsw.stub.PuppetServer;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.MethodName.class)
class TCPClientSocketTest {
    private final ExecutorService pool = Executors.newCachedThreadPool();
    private TCPServerSocket server;
    private ServerProxy client;

    @BeforeEach
    void resetMySingleton() throws NoSuchFieldException, IllegalAccessException, IOException {
        Field singleton = CentralServer.class.getDeclaredField("singleton");
        singleton.setAccessible(true);
        singleton.set(null, null);
        server = new TCPServerSocket(8888);
    }

    @AfterEach
    void close(){
        server.closeServer();
    }

    private void waitExecution(TCPClientSocket client, int time){
        CountDownLatch latch = new CountDownLatch(2);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                latch.countDown();
            }
        }, time/2, time/2);

        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        timer.cancel();
        try {
            client.getProxy().disconnect();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void assertInstantiationException(){
        assertThrows(
                IllegalArgumentException.class,
                () -> new TCPClientSocket("localhost", 80000)
        );

        assertThrows(
                UnknownHostException.class,
                () -> new TCPClientSocket("www.googo", 8000)
        );
    }

    @Test
    void testDisconnect() throws RemoteException {

        TCPClientSocket tcpClientSocket;
        try {
            tcpClientSocket = new TCPClientSocket("localhost", 8888);
        } catch (IOException e){
            throw new RuntimeException(e);
        }

        client = tcpClientSocket.getProxy();
        try {
            client.connect("Corolla");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        assertEquals("Corolla", client.getNickname());
        client.disconnect();
        assertTrue(tcpClientSocket.isClosed());

        try {
            tcpClientSocket = new TCPClientSocket("localhost", 8888);
        } catch (IOException e){
            throw new RuntimeException(e);
        }

        client = tcpClientSocket.getProxy();
        client.connect("Corolla");
        assertEquals("Corolla", client.getNickname());
        client.disconnect();
        assertTrue(tcpClientSocket.isClosed());
    }

    @Test
    @DisplayName("Basic connect testing")
    void testConnect() throws RemoteException {
        TCPClientSocket tcpClientSocket;
        try {
            tcpClientSocket = new TCPClientSocket("localhost", 8888);
        } catch (IOException e){
            throw new RuntimeException(e);
        }

        client = tcpClientSocket.getProxy();
        client.connect("Gamba");
        assertEquals("Gamba", client.getNickname());
        client.disconnect();
    }

    @Test
    @DisplayName("Connecting a second time with a different nickname")
    void testConnect2() throws RemoteException {

        TCPClientSocket tcpClientSocket;
        try {
            tcpClientSocket = new TCPClientSocket("localhost", 8888);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
        client = tcpClientSocket.getProxy();
        client.connect("Gamba");

        assertThrows(
                IllegalStateException.class,
                () -> client.connect("Giovanni")
        );

        assertNotEquals("Giovanni",client.getNickname());
        client.disconnect();
    }

    @Test
    @DisplayName("Connecting after closing socket")
    void testConnect3() throws IOException, InterruptedException {
        TCPClientSocket cli1 = new TCPClientSocket("localhost", 8888);
        cli1.closeSocket();
        assertThrows(
                RemoteException.class,
                () -> cli1.getProxy().connect("Gamba")
        );
    }

    @Test
    @DisplayName("Pinging after closing server socket")
    void testRemoteException() throws IOException {
        PuppetServer server = new PuppetServer(10000);
        TCPClientSocket client;
        try {
             client = new TCPClientSocket("localhost", 10000);
        } catch (IOException err){
            System.err.println(err.getMessage());
            return;
        }
        server.closeServer();
        assertThrows(
                RemoteException.class,
                client.getProxy()::ping
        );
    }

    @Test
    void testSendMsgFail() throws IOException{
        TCPClientSocket client = new TCPClientSocket(8888);
        client.getProxy().connect("Gamba");
        client.closeSocket();
        assertThrows(
                RemoteException.class,
                () ->client.getProxy().sendMsg("all", "Test fail")
        );
    }

    @Test
    void testSendMsg() throws IOException {
        TCPClientSocket client = new TCPClientSocket("localhost", 8888);
        client.getProxy().connect("Giacomo");
        client.getProxy().sendMsg("all", "Ciao ragazzi");
        client.getProxy().sendMsg("all", "Come va?");
        client.getProxy().disconnect();
    }


    @Test
    void testChooseColor() throws IOException {
        TCPClientSocket client = new TCPClientSocket("localhost", 8888);
        client.getProxy().connect("Giacomo");
        client.getProxy().chooseColor('b');
        client.getProxy().disconnect();
    }

    @Test
    void testChooseObjective() throws IOException {
        TCPClientSocket client = new TCPClientSocket("localhost", 8888);
        client.getProxy().connect("Giacomo");
        client.getProxy().chooseObjective(2);
        client.getProxy().disconnect();
    }

    @Test
    void testDraw() throws IOException {
        TCPClientSocket client = new TCPClientSocket("localhost", 8888);
        client.getProxy().connect("Giacomo");
        client.getProxy().draw('r', 0);
        waitExecution(client, 1000);
    }


    @Test
    void testStartGame() throws IOException {
        TCPClientSocket client = new TCPClientSocket("localhost", 8888);
        client.getProxy().connect("Giacomo");
        client.getProxy().restartGame(3);
        waitExecution(client, 1000);
    }

    @Test
    void testPlaceStartingCard() throws IOException {
        TCPClientSocket client = new TCPClientSocket("localhost", 8888);
        client.getProxy().connect("Giacomo");
        client.getProxy().placeStartCard(true);
        waitExecution(client, 1000);
    }

    @Test
    void testPlaceCard() throws IOException {
        TCPClientSocket client = new TCPClientSocket("localhost", 8888);
        client.getProxy().connect("Giacomo");
        client.getProxy().placeCard("R2", new GamePoint(1,1), "TR", true);
        waitExecution(client, 2000);
    }

    @Test
    void setNumOfPlayers() throws IOException {
        TCPClientSocket client = new TCPClientSocket(8888);
        client.getProxy().connect("Gamba");
        client.getProxy().setNumOfPlayers(2);
        waitExecution(client, 1000);
    }

}