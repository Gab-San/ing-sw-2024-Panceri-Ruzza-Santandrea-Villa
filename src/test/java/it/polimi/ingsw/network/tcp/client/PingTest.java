package it.polimi.ingsw.network.tcp.client;

import it.polimi.ingsw.network.CentralServer;
import it.polimi.ingsw.network.CommandPassthrough;
import it.polimi.ingsw.network.tcp.server.TCPServerSocket;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertNull;

public class PingTest {

    @Test
    void pingTest() throws RemoteException, InterruptedException {
        TCPServerSocket server;
        try {
            server = new TCPServerSocket(8888);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        TCPClientSocket client;
        try {
            client = new TCPClientSocket("localhost", 8888);
        } catch (IOException e){
            throw new RuntimeException(e);
        }

        CommandPassthrough virtualServer = client.getProxy();

        virtualServer.ping();
        CountDownLatch latch = new CountDownLatch(2);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                latch.countDown();
            }
        },1000,1000);
        latch.await();
        timer.cancel();
        client.closeSocket();
        server.closeServer();
    }

    @Test
    void timeoutPingTest() throws IOException, InterruptedException {
        new TCPServerSocket(8888);
        TCPClientSocket clientSocket = new TCPClientSocket(8888);
        clientSocket.getProxy().connect("GIANGIANNI");
        clientSocket.closeSocket();
        CountDownLatch latch = new CountDownLatch(30);
            Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                latch.countDown();
            }
        },1000,1000);
        latch.await();
        timer.cancel();
        assertNull(CentralServer.getSingleton().getClientFromNickname("GIANGIANNI"));
    }
}
