package it.polimi.ingsw.server.tcp;

import it.polimi.ingsw.server.tcp.TCPClient;
import it.polimi.ingsw.server.tcp.TCPServer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PingTest {

    private void waitExecution(TCPClient client, int time){
        new Thread(
                ()->{
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    client.closeSocket();
                }
        ).start();
        while (!client.isClosed());
    }

    @Test
    void pingTest() throws RemoteException {
        TCPServer server;
        try {
            server = new TCPServer(8888);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        TCPClient client;
        try {
            client = new TCPClient("localhost", 8888);
        } catch (IOException e){
            throw new RuntimeException(e);
        }


        client.ping();
        waitExecution(client,  2000);
        server.closeServer();
    }
}
