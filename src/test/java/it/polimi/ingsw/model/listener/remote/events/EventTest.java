package it.polimi.ingsw.model.listener.remote.events;


import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerHand;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.ResourceCard;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.model.cards.objective.ResourceObjectiveStrategy;
import it.polimi.ingsw.model.deck.PlayableDeck;
import it.polimi.ingsw.model.deck.cardfactory.CardFactory;
import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.model.exceptions.DeckException;
import it.polimi.ingsw.model.exceptions.PlayerHandException;
import it.polimi.ingsw.model.listener.GameListener;
import it.polimi.ingsw.stub.SimpleListener;
import it.polimi.ingsw.stub.StubClient;
import it.polimi.ingsw.stub.StubView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.CountDownLatch;

import static it.polimi.ingsw.GameResource.WOLF;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EventTest {
    private Board board;
    private final String nick= "Gamba";
    StubView view;

    @BeforeEach
    void setup(){
        view = new StubView();
        StubClient client = new StubClient(nick, view);
        board = new Board();
        board.subscribeClientToUpdates(nick, client);
    }

    @AfterEach
    void close(){
        board.unsubscribeClientFromUpdates(nick);
    }

    @Test
    void testCreatePlayer() throws InterruptedException {
        board.addPlayer(new Player(nick));
//        Thread.sleep(1000);
        CountDownLatch latch = new CountDownLatch(2);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                latch.countDown();
            }
        }, 500, 500);
        latch.await();
        timer.cancel();
    }

    @Test
    void testPlayerUpdates() throws InterruptedException {
        Player player = new Player(nick);
        board.addPlayer(player);
        player.setConnected(false);
        player.setTurn(2);
        player.setColor(PlayerColor.BLUE);
        board.setPlayerDeadLock(player, true);
//        Thread.sleep(1000);
        CountDownLatch latch = new CountDownLatch(2);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                latch.countDown();
            }
        }, 500, 500);
        latch.await();
        timer.cancel();
    }


    @Test
    void testBoardEvents() throws InterruptedException {
        board.setGamePhase(GamePhase.DRAWCARD);
        board.setCurrentTurn(3);
        board.nextTurn();
//        Thread.sleep(1000);
        CountDownLatch latch = new CountDownLatch(2);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                latch.countDown();
            }
        }, 500, 500);
        latch.await();
        timer.cancel();
    }

    @Test
    void testObjectiveReveal() throws InterruptedException {
        board.revealObjectives();
//        Thread.sleep(1000);
        CountDownLatch latch = new CountDownLatch(2);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                latch.countDown();
            }
        }, 500, 500);
        latch.await();
        timer.cancel();
    }

    @Test
    @DisplayName("Deck State only first and second revealed")
    void testDeckCreationEvents() throws InterruptedException {
        board.unsubscribeClientFromUpdates(nick);
        PlayableDeck deckWithTwo = new PlayableDeck(Board.RESOURCE_DECK, new CardFactory("src/test/java/it/polimi/ingsw/model/deck/TestFile") {
            private int count = 0;
            @Override
            public PlayCard addCardToDeck() throws DeckException {
                if(count >= 2) throw  new DeckException();
                int id = new Random().nextInt();
                String cardId = "R" + id;
                count++;
                return new ResourceCard(cardId, WOLF);
            }

            @Override
            protected PlayCard instantiateCard(String cardId) {
                return null;
            }
        }, 2);
        GameListener listener = new SimpleListener(new StubClient("TEST", view));
        deckWithTwo.addListener(listener);

        CountDownLatch latch = new CountDownLatch(2);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                latch.countDown();
            }
        }, 500, 500);
        latch.await();
        timer.cancel();
    }

    private PlayableDeck createDeckWithThreeCards(){
        return new PlayableDeck(Board.RESOURCE_DECK, new CardFactory("src/test/java/it/polimi/ingsw/model/deck/TestFile") {
            private int count = 0;
            @Override
            public PlayCard addCardToDeck() throws DeckException {
                if(count >= 3) throw  new DeckException();
                int id = new Random().nextInt();
                String cardId = "R" + id;
                count++;
                return new ResourceCard(cardId, WOLF);
            }

            @Override
            protected PlayCard instantiateCard(String cardId) {
                return null;
            }
        }, 3);
    }

    @Test
    @DisplayName("Deck State: only first revealed")
    void testDeckStateOneCard1() throws InterruptedException {
        board.unsubscribeClientFromUpdates(nick);
        PlayableDeck deckWithTop = createDeckWithThreeCards();
        deckWithTop.getTopCard();
        deckWithTop.getSecondRevealedCard();

        GameListener listener = new SimpleListener(new StubClient("TEST", view));
        deckWithTop.addListener(listener);

        CountDownLatch latch = new CountDownLatch(2);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                latch.countDown();
            }
        }, 500, 500);
        latch.await();
        timer.cancel();
    }

    @Test
    @DisplayName("Deck State: only second revealed")
    void testDeckStateOneCard2() throws InterruptedException {
        board.unsubscribeClientFromUpdates(nick);
        PlayableDeck deckWithTop = createDeckWithThreeCards();
        deckWithTop.getTopCard();
        deckWithTop.getFirstRevealedCard();

        GameListener listener = new SimpleListener(new StubClient("TEST", view));
        deckWithTop.addListener(listener);

        CountDownLatch latch = new CountDownLatch(2);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                latch.countDown();
            }
        }, 500, 500);
        latch.await();
        timer.cancel();
    }



    @Test
    @DisplayName("Empty Deck")
    void testDeckCreationEmpty() throws InterruptedException {
        board.unsubscribeClientFromUpdates(nick);
        PlayableDeck emptyDeck = new PlayableDeck(Board.RESOURCE_DECK, new CardFactory("src/test/java/it/polimi/ingsw/model/deck/TestFile") {
            @Override
            public PlayCard addCardToDeck() throws DeckException {
                throw new DeckException();
            }

            @Override
            protected PlayCard instantiateCard(String cardId) {
                return null;
            }
        }, 0);

        GameListener listener = new SimpleListener(new StubClient("TEST", view));
        emptyDeck.addListener(listener);

        CountDownLatch latch = new CountDownLatch(2);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                latch.countDown();
            }
        }, 500, 500);
        latch.await();
        timer.cancel();
    }

    @Test
    void testDeckEvents() throws InterruptedException, DeckException {
        board.revealObjectives();
        Player player = new Player(nick);
        board.addPlayer(player);
        board.deal(Board.STARTING_DECK, player.getHand());
        board.drawFirst(Board.RESOURCE_DECK, player.getHand());

        CountDownLatch latch = new CountDownLatch(2);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                latch.countDown();
            }
        }, 500, 500);
        latch.await();
        timer.cancel();
    }

    @Test
    void testPlayerHandEvents() throws InterruptedException{
        board.revealObjectives();
        Player player = new Player(nick);
        board.addPlayer(player);
        PlayerHand hand = player.getHand();
        ResourceCard card = new ResourceCard("T32", WOLF);

        hand.addCard(() -> new ResourceCard("T0", WOLF));
        hand.addCard(() -> new ResourceCard("T34", WOLF));
        hand.addCard(() -> card);
        assertThrows(
                PlayerHandException.class,
                () -> hand.addCard(() -> new ResourceCard("T57", WOLF))
        );
        hand.popCard(1);
        hand.removeCard(card);
        Map<GameResource, Integer> res = new HashMap<>();
        res.put(WOLF, 3);
        hand.addObjectiveCard(new ObjectiveCard("T100", new ResourceObjectiveStrategy(res), 2));
        res.put(WOLF, 5);
        hand.addObjectiveCard(new ObjectiveCard("T101", new ResourceObjectiveStrategy(res), 2));
        hand.chooseObjective(2);
        hand.setStartingCard(new StartingCard("T200", new GameResource[]{WOLF}));

        CountDownLatch latch = new CountDownLatch(5);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                latch.countDown();
            }
        }, 500, 500);
        latch.await();
        timer.cancel();
    }
}