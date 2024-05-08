package it.polimi.ingsw.tcp;

import it.polimi.ingsw.server.tcp.TCPClient;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;


class TCPClientTest {
    String host = "localhost";
    int objPort = 8888;
    int msgPort = 13450;
    @Test
    public void testConnect(){
        TCPClient client;
        try {
            client = new TCPClient(host,objPort,msgPort);
        } catch (IOException | NumberFormatException ioException){
            ioException.printStackTrace(System.err);
            throw new RuntimeException(ioException);
        }


        try{
            client.connect("Gamba");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        assertEquals("Gamba", client.nickname);

        assertThrows(IllegalStateException.class,
                () -> client.connect("Giovanni")
        );
    }

    @Test
    public void testConnect2(){
        TCPClient client;
        try {
            client = new TCPClient(host,objPort,msgPort);
        } catch (IOException | NumberFormatException ioException){
            ioException.printStackTrace(System.err);
            throw new RuntimeException(ioException);
        }


        try{
            System.out.println("Pinging...");
            client.connect("Gamba");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        try{
            System.out.println("Pinging...");
            client.connect("Giovanni");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        assertEquals("Giovanni", client.nickname);
    }

    @Test
    public void testCmd(){
        TCPClient client;
        try {
            client = new TCPClient(host,objPort,msgPort);
        } catch (IOException | NumberFormatException ioException){
            ioException.printStackTrace(System.err);
            throw new RuntimeException(ioException);
        }

        try{
            System.out.println("Pinging...");
            client.connect("Falafel");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        assertEquals("Falafel", client.nickname);

        try {
            client.testCmd("TEST");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}