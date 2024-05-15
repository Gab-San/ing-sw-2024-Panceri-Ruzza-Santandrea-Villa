//package it.polimi.ingsw.server.tcp;
//
//import it.polimi.ingsw.server.tcp.client.TCPClientSocket;
//import org.junit.jupiter.api.Test;
//
//import java.io.IOException;
//import java.rmi.RemoteException;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class PingTest {
//
//    private void waitExecution(TCPClientSocket client, int time){
//        new Thread(
//                ()->{
//                    try {
//                        Thread.sleep(time);
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
//                    client.closeSocket();
//                }
//        ).start();
//        while (!client.isClosed());
//    }
//
//    @Test
//    void pingTest() throws RemoteException {
//        TCPServerSocket server;
//        try {
//            server = new TCPServerSocket(8888);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//
//        TCPClientSocket client;
//        try {
//            client = new TCPClientSocket("localhost", 8888);
//        } catch (IOException e){
//            throw new RuntimeException(e);
//        }
//
//
//        client.ping();
//        waitExecution(client,  2000);
//        server.closeServer();
//    }
//}
