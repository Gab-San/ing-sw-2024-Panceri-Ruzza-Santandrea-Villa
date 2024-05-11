package it.polimi.ingsw.server;

import it.polimi.ingsw.server.rmi.RMIClient;
import it.polimi.ingsw.server.rmi.RMIServer;
import it.polimi.ingsw.server.tcp.TCPClient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class SecondBoardInteractionTest {
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
    void testSetNumOfPlayers() throws IOException, NotBoundException {
        RMIClient client = new RMIClient("localhost", 1234);

        client.connect("Salatino");
        client.setNumOfPlayers(5);
        client.setNumOfPlayers(3);

        TCPClient cli2 = new TCPClient(8888);
        cli2.connect("Asdf");

        TCPClient cli3 = new TCPClient("localhost", 8888);
        cli3.connect("SINA");
        validateClient(cli3);

        RMIClient cli4 = new RMIClient(1234);
        assertThrows(
                IllegalStateException.class,
                () -> cli4.connect("SUSI")
        );
    }
}
