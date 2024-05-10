package it.polimi.ingsw.server.tcp;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertThrows;

class TCPClientTest {
    ExecutorService pool = Executors.newCachedThreadPool();
    @Test
    void startClient(){
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

        try {
            client.connect("K");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        try {
            client.testCmd("TEST");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testConnect() throws InterruptedException {
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
        pool.execute(
                ()->{
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    client.closeSocket();
                }
        );
        while (!client.isClosed());
    }


    @Test
    void testThread(){
        boolean done = false;
        new Thread(
                () -> {
                    while(!done){
                        System.out.println("...");
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        ).start();

        System.out.println("Ciao");
    }

    @Test
    void testConnect2() throws InterruptedException {
        Thread.sleep(50);
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
            client.connect("Gamba");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testSendMsg() throws InterruptedException {
        Thread.sleep(100);
        TCPClient client;
        try {
            client = new TCPClient("localhost", 8888);
        } catch (IOException e){
            throw new RuntimeException(e);
        }

        try {
            client.connect("Giovanni");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        try {
            client.sendMsg("Ciao ragazzi");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

}