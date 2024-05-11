package it.polimi.ingsw.server.tcp;

import it.polimi.ingsw.server.tcp.testingStub.PuppetServer;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TCPClientTest {
    private ExecutorService pool = Executors.newCachedThreadPool();
    private static TCPServer tcpServer;

    @BeforeAll
    static void setup(){
        try {
            tcpServer = new TCPServer(8888);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    static void close(){
        tcpServer.closeServer();
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
    @Order(1)
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
    @Order(2)
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
    @Order(3)
    void testConnect2() {

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
        try {
            client.connect("Giovanni");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        waitExecution(client, 2000);
        assertNotEquals("Giovanni",client.getNickname());
    }

    @Test
    @Order(4)
    void testConnect3() throws RemoteException, InterruptedException {
        TCPClient cli1;
        try {
            cli1 = new TCPClient("localhost", 8888);
        } catch (IOException e){
            throw new RuntimeException(e);
        }

        TCPClient cli2;
        try {
            cli2 = new TCPClient("localhost", 8888);
        } catch (IOException e){
            throw new RuntimeException(e);
        }

        cli1.connect("Gamba");
        Thread.sleep(10);
        cli2.connect("Gamba");

        waitExecution(cli1, 2000);
        assertNull(cli2.getNickname());
        cli2.closeSocket();
    }

    @Test
    @Order(5)
    void testConnect4() throws RemoteException{
        TCPClient client;
        try {
            client = new TCPClient("localhost", 8888);
        } catch (IOException e){
            throw new RuntimeException(e);
        }

        client.connect("Gianni");
        client.closeSocket();
        assertThrows(
                IllegalStateException.class,
                () -> client.testCmd("SUS")
        );
    }

    @Test
    @Order(6)
    void testConnect5() throws RemoteException {
        PuppetServer server;
        try {
            server = new PuppetServer(10000);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        TCPClient client;
        try {
            client = new TCPClient("localhost", 10000);
        } catch (IOException e){
            throw new RuntimeException(e);
        }

        pool.execute(
                server::closeServer
        );
        assertThrows(
                RemoteException.class,
                client::ping
        );
    }



    @Test
    void testCmd() throws RemoteException {
        TCPClient client;

        try {
            client = new TCPClient("localhost", 8888);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        client.connect("Giovanni");
        validateClient(client);
        client.testCmd("SONO GIOVANNI STO TESTANDO");
        waitExecution(client, 1500);
    }

    @Test
    void testSendMsg() {
        TCPClient client;
        try {
            client = new TCPClient("localhost", 8888);
        } catch (IOException e){
            throw new RuntimeException(e);
        }

        try {
            client.connect("Giacomo");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        validateClient(client);
        try {
            client.sendMsg("Ciao ragazzi");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        try {
            client.sendMsg("Come va?");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        waitExecution(client, 5000);
    }
}