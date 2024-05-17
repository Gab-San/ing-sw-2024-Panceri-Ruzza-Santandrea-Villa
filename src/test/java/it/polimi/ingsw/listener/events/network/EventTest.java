package it.polimi.ingsw.listener.events.network;

import it.polimi.ingsw.listener.events.network.stub.PuppetClient;
import it.polimi.ingsw.listener.events.network.stub.StubView;
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
        client = new PuppetClient(view);
        board = new Board("SUSSY BAKA");
    }

    @Test
    void testCreatePlayer(){
        board.subscribeToListeners(nick,client);
        board.addPlayer(new Player(nick));

    }
}