package it.polimi.ingsw.server;

import it.polimi.ingsw.server.rmi.RMIClient;
import it.polimi.ingsw.server.tcp.TCPClient;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BoardInteractionTest {

    private final ExecutorService pool = Executors.newCachedThreadPool();
    private CentralServer centralServer;
    @BeforeAll
    static void setup(){
        CentralServer.main(new String[]{"8888", "1234"});
    }

    @BeforeEach
    void initialize(){
        centralServer = CentralServer.getSingleton();
    }

    @AfterAll
    static void close(){
        System.exit(0);
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
    @Order(1)
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
    @Order(2)
    void testMultipleJoin() throws IOException, NotBoundException {
        TCPClient cli1 = new TCPClient("localhost", 8888);

        cli1.connect("Gamba");
        validateClient(cli1);
        assertNotNull(centralServer.getClientFromNickname("Gamba"));
        cli1.setNumOfPlayers(5);
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


        cli1.sendMsg("Ciao");
        cli4.sendMsg("Ciao Gamba");
        cli3.sendMsg("LA fuffa è bella");
        cli2.testCmd("WOWOWOWO");
        waitExecution(cli1,  2000);
        cli2.disconnect();
        waitExecution(cli3,  0);
        cli4.disconnect();
    }

}
