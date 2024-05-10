package it.polimi.ingsw.server.tcp;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class TCPClientTest {
    @Test
    void startClient(){
        TCPClient client;
        try {
            client = new  TCPClient("localhost", 8888);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        client.start();
        assertThrows(
                IllegalArgumentException.class,
                () -> new TCPClient("localhost", 80000)
        );
    }

    @Test
    void testCmd(){
        TCPClient client;
        try {
            client = new TCPClient("localhost", 8888);
        } catch (IOException e){
            throw new RuntimeException(e);
        }

        client.start();
        try {
            client.testCmd("TEST");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testConnect() throws InterruptedException {
        Thread.sleep(100);
        TCPClient client;
        try {
            client = new TCPClient("localhost", 8888);
        } catch (IOException e){
            throw new RuntimeException(e);
        }

        client.start();
        try {
            client.connect("Gamba");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        try {
            client.testCmd("TEST");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}