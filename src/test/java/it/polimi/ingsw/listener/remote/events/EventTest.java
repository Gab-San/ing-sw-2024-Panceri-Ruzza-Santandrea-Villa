package it.polimi.ingsw.listener.remote.events;

import it.polimi.ingsw.listener.remote.events.stub.PuppetClient;
import it.polimi.ingsw.listener.remote.events.stub.StubView;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EventTest {
    private PuppetClient client;
    private StubView view;
    private Board board;
    private final String nick= "Gamba";

    @BeforeEach
    void setup(){
        view = new StubView();
        client = new PuppetClient("Gorlanino", view);
        board = new Board("SUSSY BAKA");
    }

    @Test
    void testCreatePlayer() throws InterruptedException {
        board.subscribeClientToUpdates(nick,client);
        board.addPlayer(new Player(nick));
        board.addPlayer(new Player("Corradino"));
//        board.subscribeClientToUpdates("SUSSY", new PuppetClient("SUSSY", view));
//        board.addPlayer(new Player("PUPPA"));
        board.unsubscribeClientFromUpdates(nick);
//        board.addPlayer(new Player("LAVANDA"));
//        board.subscribeClientToUpdates("GIOVANNA", new PuppetClient("GIOVANNA", view));
        Thread.sleep(2000);
        System.err.println("Disconnected Client");
        PuppetClient puppetClient = new PuppetClient("Gorlanino", view);
        board.subscribeClientToUpdates(nick, puppetClient);

        while (!puppetClient.notificationReceived) Thread.sleep(1000);
    }
}