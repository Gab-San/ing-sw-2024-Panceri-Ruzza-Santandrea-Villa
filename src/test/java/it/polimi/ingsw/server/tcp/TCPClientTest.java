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
    ExecutorService pool = Executors.newCachedThreadPool();
    static TCPServer tcpServer;

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
                    client.closeSocket();
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
    @Order(2)
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
        waitExecution(client, 2000);
        assertNull(client.getNickname());
    }

    @Test
    @Order(3)
    void testConnect3() throws RemoteException {
        TCPClient client;
        try {
            client = new TCPClient("localhost", 8888);
        } catch (IOException e){
            throw new RuntimeException(e);
        }

        client.connect("Sassuolo");
        validateClient(client);
        client.connect("Sassuolo");
        waitExecution(client, 1000);
    }

    @Test
    @Order(4)
    void testConnect4() throws RemoteException{
        TCPClient client;
        try {
            client = new TCPClient("localhost", 8888);
        } catch (IOException e){
            throw new RuntimeException(e);
        }

        client.connect("Gianni");
        waitExecution(client, 0);
        assertThrows(
                IllegalStateException.class,
                () -> client.testCmd("SUS")
        );
    }

    @Test
    @Order(5)
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


//    @Test
//    void sendIncorrectCommand(){
//        TCPClient client;
//        try {
//            client = new TCPClient("localhost", 8888);
//        } catch (IOException e){
//            throw new RuntimeException(e);
//        }
//    }


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
        waitExecution(client, 1000);
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

    @Test
    void testDisconnect(){

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
        try {
            client.disconnect();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        assertTrue(client.isClosed());
        waitExecution(client,  1000);
    }

    @Test
    void testSetNumOfPlayers() throws RemoteException {
        TCPClient client;
        try {
            client = new TCPClient("localhost", 8888);
        } catch (IOException e){
            throw new RuntimeException(e);
        }


        try {
            client.connect("Gambino");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        validateClient(client);
        client.setNumOfPlayers(2);
        waitExecution(client,  1000);
    }

    @Test
    void testSetNumOfPlayers2() throws RemoteException {
        TCPClient client;
        try {
            client = new TCPClient("localhost", 8888);
        } catch (IOException e){
            throw new RuntimeException(e);
        }


        try {
            client.connect("Salatino");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        validateClient(client);
        client.setNumOfPlayers(3);
        waitExecution(client,  1000);
    }

    @Test
    void testSetNumOfPlayers3() throws RemoteException {
        TCPClient client;
        try {
            client = new TCPClient("localhost", 8888);
        } catch (IOException e){
            throw new RuntimeException(e);
        }


        try {
            client.connect("Fuffy");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        validateClient(client);
        client.setNumOfPlayers(4);
        waitExecution(client,  1000);
    }

    @Test
    void testSetNumOfPlayers4() throws RemoteException {
        TCPClient client;
        try {
            client = new TCPClient("localhost", 8888);
        } catch (IOException e){
            throw new RuntimeException(e);
        }


        try {
            client.connect("Carlo");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        validateClient(client);
        client.setNumOfPlayers(4);
        waitExecution(client,  1000);
    }

    @Test
    void testSetNumOfPlayers5() throws RemoteException {
        TCPClient client;
        try {
            client = new TCPClient("localhost", 8888);
        } catch (IOException e){
            throw new RuntimeException(e);
        }


        try {
            client.connect("Andrea");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        validateClient(client);
        client.setNumOfPlayers(5);
        waitExecution(client,  1000);
    }
    @Test
    void testSetNumOfPlayers6() throws RemoteException {
        TCPClient client;
        try {
            client = new TCPClient("localhost", 8888);
        } catch (IOException e){
            throw new RuntimeException(e);
        }


        try {
            client.connect("Filippo");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        validateClient(client);
        client.setNumOfPlayers(1);
        waitExecution(client,  1000);
    }

}