//package it.polimi.ingsw.server.tcp;
//
//import it.polimi.ingsw.Point;
//import it.polimi.ingsw.server.CentralServer;
//import it.polimi.ingsw.server.testingStub.PuppetServer;
//import org.junit.jupiter.api.*;
//
//import java.io.IOException;
//import java.lang.reflect.Field;
//import java.net.UnknownHostException;
//import java.rmi.RemoteException;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@TestMethodOrder(MethodOrderer.MethodName.class)
//class TCPClientSocketTest {
//    private final ExecutorService pool = Executors.newCachedThreadPool();
//    private TCPServer server;
//
//    @BeforeEach
//    void resetMySingleton() throws NoSuchFieldException, IllegalAccessException, IOException {
//        Field singleton = CentralServer.class.getDeclaredField("singleton");
//        singleton.setAccessible(true);
//        singleton.set(null, null);
//        server = new TCPServer(8888);
//    }
//
//    @AfterEach
//    void close(){
//        server.closeServer();
//    }
//
//    private void validateClient(TCPClientSocket client){
//        while (client.getNickname() == null){
//            try {
//                Thread.sleep(50);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
//
//    private void waitExecution(TCPClientSocket client, int time){
//        pool.execute(
//                ()->{
//                    try {
//                        Thread.sleep(time);
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
//                    try {
//                        client.disconnect();
//                    } catch (RemoteException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//        );
//        while (!client.isClosed());
//    }
//
//    @Test
//    void assertInstantiationException(){
//        assertThrows(
//                IllegalArgumentException.class,
//                () -> new TCPClientSocket("localhost", 80000)
//        );
//
//        assertThrows(
//                UnknownHostException.class,
//                () -> new TCPClientSocket("www.googo", 8000)
//        );
//    }
//
//    @Test
//    void testDisconnect() throws RemoteException {
//
//        TCPClientSocket client;
//        try {
//            client = new TCPClientSocket("localhost", 8888);
//        } catch (IOException e){
//            throw new RuntimeException(e);
//        }
//
//        try {
//            client.connect("Corolla");
//        } catch (RemoteException e) {
//            throw new RuntimeException(e);
//        }
//        validateClient(client);
//        client.disconnect();
//        assertTrue(client.isClosed());
//
//        try {
//            client = new TCPClientSocket("localhost", 8888);
//        } catch (IOException e){
//            throw new RuntimeException(e);
//        }
//        client.connect("Corolla");
//        validateClient(client);
//        assertEquals(client.getNickname(), "Corolla");
//        waitExecution(client,  5000);
//        assertTrue(client.isClosed());
//    }
//
//    @Test
//    @DisplayName("Basic connect testing")
//    void testConnect() {
//        TCPClientSocket client;
//        try {
//            client = new TCPClientSocket("localhost", 8888);
//        } catch (IOException e){
//            throw new RuntimeException(e);
//        }
//
//        try {
//            client.connect("Gamba");
//        } catch (RemoteException e) {
//            throw new RuntimeException(e);
//        }
//        waitExecution(client,  3000);
//        assertEquals("Gamba", client.getNickname());
//    }
//
//    @Test
//    @DisplayName("Connecting a second time with a different nickname")
//    void testConnect2() throws RemoteException {
//
//        TCPClientSocket client;
//        try {
//            client = new TCPClientSocket("localhost", 8888);
//        } catch (IOException e){
//            throw new RuntimeException(e);
//        }
//
//        client.connect("Gamba");
//        client.connect("Giovanni");
//
//        waitExecution(client, 2000);
//        assertNotEquals("Giovanni",client.getNickname());
//    }
//
//    @Test
//    @DisplayName("Connecting after closing socket")
//    void testConnect3() throws IOException, InterruptedException {
//        TCPClientSocket cli1 = new TCPClientSocket("localhost", 8888);
//        cli1.closeSocket();
//        assertThrows(
//                RemoteException.class,
//                () -> cli1.connect("Gamba")
//        );
//        waitExecution(cli1, 2000);
//        assertNull(cli1.getNickname());
//    }
//
//    @Test
//    @DisplayName("Closing socket before connect check message")
//    void testConnect4() throws IOException {
//        TCPClientSocket client = new TCPClientSocket("localhost", 8888);
//        client.connect("Gianni");
//        client.closeSocket();
//        assertThrows(
//                IllegalStateException.class,
//                () -> client.testCmd("SUS")
//        );
//    }
//
//    @Test
//    @DisplayName("Closing socket before connect")
//    void testConnect5() throws IOException {
//        TCPClientSocket client = new TCPClientSocket("localhost", 8888);
//        client.connect("Gianni");
//        client.closeSocket();
//        assertThrows(
//                IllegalStateException.class,
//                () -> client.testCmd("SUS")
//        );
//    }
//
//    @Test
//    @DisplayName("Pinging after closing server socket")
//    void testRemoteException() throws IOException {
//        PuppetServer server = new PuppetServer(10000);
//
//        TCPClientSocket client = new TCPClientSocket("localhost", 10000);
//        server.closeServer();
//        assertThrows(
//                RemoteException.class,
//                client::ping
//        );
//    }
//
//
//
//    @Test
//    void testCmd() throws IOException {
//        TCPClientSocket client = new TCPClientSocket("localhost", 8888);
//
//        client.connect("Giovanni");
//        validateClient(client);
//        client.testCmd("SONO GIOVANNI STO TESTANDO");
//        waitExecution(client, 1500);
//    }
//
//    @Test
//    void testSendMsgFail() throws IOException{
//        TCPClientSocket client = new TCPClientSocket(8888);
//        client.connect("Gamba");
//        validateClient(client);
//        client.closeSocket();
//        assertThrows(
//                RemoteException.class,
//                () ->client.sendMsg("Test fail")
//        );
//    }
//
//    @Test
//    void testSendMsg() throws IOException {
//        TCPClientSocket client = new TCPClientSocket("localhost", 8888);
//        client.connect("Giacomo");
//        validateClient(client);
//        client.sendMsg("Ciao ragazzi");
//        client.sendMsg("Come va?");
//        waitExecution(client, 5000);
//    }
//
//
//    @Test
//    void testChooseColor() throws IOException {
//        TCPClientSocket client = new TCPClientSocket("localhost", 8888);
//        client.connect("Giacomo");
//        validateClient(client);
//        client.chooseColor('b');
//        waitExecution(client, 2000);
//    }
//
//    @Test
//    void testChooseObjective() throws IOException {
//        TCPClientSocket client = new TCPClientSocket("localhost", 8888);
//        client.connect("Giacomo");
//        validateClient(client);
//        client.chooseObjective(2);
//        waitExecution(client, 2000);
//    }
//
//    @Test
//    void testDraw() throws IOException {
//        TCPClientSocket client = new TCPClientSocket("localhost", 8888);
//        client.connect("Giacomo");
//        validateClient(client);
//        client.draw('r', 0);
//        waitExecution(client, 2000);
//    }
//
//
//    @Test
//    void testStartGame() throws IOException {
//        TCPClientSocket client = new TCPClientSocket("localhost", 8888);
//        client.connect("Giacomo");
//        validateClient(client);
//        client.startGame(3);
//        waitExecution(client, 5000);
//    }
//
//    @Test
//    void testPlaceStartingCard() throws IOException {
//        TCPClientSocket client = new TCPClientSocket("localhost", 8888);
//        client.connect("Giacomo");
//        validateClient(client);
//        client.placeStartCard(true);
//        waitExecution(client, 2000);
//    }
//
//    @Test
//    void testPlaceCard() throws IOException {
//        TCPClientSocket client = new TCPClientSocket("localhost", 8888);
//        client.connect("Giacomo");
//        validateClient(client);
//        client.placeCard("R2", new Point(1,1), "TR", true);
//        waitExecution(client, 2000);
//    }
//
//    @Test
//    void setNumOfPlayers() throws IOException {
//        TCPClientSocket client = new TCPClientSocket(8888);
//        client.connect("Gamba");
//        validateClient(client);
//        client.setNumOfPlayers(2);
//        waitExecution(client, 500);
//    }
//
//}