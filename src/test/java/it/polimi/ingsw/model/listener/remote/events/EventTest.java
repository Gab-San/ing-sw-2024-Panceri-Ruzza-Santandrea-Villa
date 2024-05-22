package it.polimi.ingsw.model.listener.remote.events;


import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.PlayArea;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerHand;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.cards.objective.ResourceObjectiveStrategy;
import it.polimi.ingsw.model.deck.PlayableDeck;
import it.polimi.ingsw.model.deck.cardfactory.CardFactory;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.GameResource;
import it.polimi.ingsw.model.enums.PlayerColor;
import it.polimi.ingsw.model.exceptions.DeckException;
import it.polimi.ingsw.model.exceptions.DeckInstantiationException;
import it.polimi.ingsw.model.exceptions.PlayerHandException;
import it.polimi.ingsw.model.listener.GameListener;
import it.polimi.ingsw.model.listener.remote.events.playarea.SerializableCorner;
import it.polimi.ingsw.stub.SimpleListener;
import it.polimi.ingsw.stub.StubClient;
import it.polimi.ingsw.stub.StubView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.CountDownLatch;

import static it.polimi.ingsw.model.enums.GameResource.LEAF;
import static it.polimi.ingsw.model.enums.GameResource.WOLF;
import static org.junit.jupiter.api.Assertions.*;

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
    void testBoardEvents() throws InterruptedException {
        board.setGamePhase(GamePhase.DRAWCARD);
        board.setCurrentTurn(3);
        CountDownLatch latch1 = new CountDownLatch(2);
        Timer timer1 = new Timer();
        timer1.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                latch1.countDown();
            }
        }, 500, 500);
        latch1.await();
        timer1.cancel();
        assertEquals(GamePhase.DRAWCARD, view.getGamePhase());
        assertEquals(3, view.getTurn());
        Player player = new Player(nick);
        board.addPlayer(player);
        board.addScore(player, 10);
        board.nextTurn();
        CountDownLatch latch2 = new CountDownLatch(2);
        Timer timer2 = new Timer();
        timer2.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                latch2.countDown();
            }
        }, 500 , 500);
        latch2.await();
        timer2.cancel();
        assertEquals(1, view.getTurn());
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
        board.addScore(player, 10);
        board.checkEndgame();
        CountDownLatch latch3 = new CountDownLatch(1);
        Timer timer3 = new Timer();
        timer3.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                latch3.countDown();
            }
        }, 500, 500);
        latch3.await();
        timer3.cancel();
        assertTrue(view.isEndgame());
    }

    @Test
    void endgameTriggerWithEmptyDeck() throws InterruptedException {
        Player testPlayer = new Player(nick);
        board.addPlayer(testPlayer);
        PlayerHand hand = testPlayer.getHand();
        while(true){
            try{
                board.drawTop(Board.RESOURCE_DECK,hand );
                hand.popCard(0);
            } catch (DeckException e){
                break;
            }
        }
        while(true){
            try{
                board.drawTop(Board.GOLD_DECK,hand );
                hand.popCard(0);
            } catch (DeckException e){
                break;
            }
        }
        board.checkEndgame();

        CountDownLatch latch = new CountDownLatch(5);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                latch.countDown();
            }
        }, 500, 500);
        latch.await();
        assertTrue(view.isEndgame());
    }

    @Test
    void testPlayerUpdates() throws InterruptedException {
        Player player = new Player(nick);
        board.addPlayer(player);
        player.setConnected(false);
        player.setTurn(2);
        player.setColor(PlayerColor.BLUE);
        board.setPlayerDeadLock(player, true);
        CountDownLatch latch = new CountDownLatch(2);
        Timer timer = new Timer();
        CountDownLatch finalLatch1 = latch;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                finalLatch1.countDown();
            }
        }, 500, 500);
        latch.await();
        Player viewPlayer =  view.getPlayer(nick);
        assertNotNull(viewPlayer);
        assertEquals(nick, viewPlayer.getNickname());
        assertFalse(viewPlayer.isConnected());
        assertEquals(viewPlayer.getTurn(), 2);
        assertEquals(viewPlayer.getColor(), PlayerColor.BLUE);
        assertTrue(board.getPlayerDeadlocks().get(player));
        board.removePlayer(nick);
        latch = new CountDownLatch(2);
        timer = new Timer();
        CountDownLatch finalLatch = latch;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                finalLatch.countDown();
            }
        }, 500, 500);
        latch.await();
        assertThrows(
                NoSuchElementException.class,
                () -> view.getPlayer(nick)
        );
    }


    @Test
    void testPlayArea() throws InterruptedException {
        Player player = new Player(nick);
        board.addPlayer(player);

        StartingCard card = new StartingCard("S0", new GameResource[]{LEAF},
                new Corner(WOLF, CornerDirection.TL),
                new Corner(LEAF, CornerDirection.BR));
        player.getHand().setStartingCard(card);

        board.placeStartingCard(player, true);

        List<SerializableCorner> freeCorners = new ArrayList<>();
        freeCorners.add(new SerializableCorner("S0", CornerDirection.TL.toString()));
        freeCorners.add(new SerializableCorner("S0", CornerDirection.BR.toString()));

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

        assertEquals(freeCorners,view.getFreeCorners());
        assertEquals(view.getVisibleResources(), board.getPlayerAreas().get(player).getVisibleResources());

        PlayCard playCard = new ResourceCard("A0", WOLF,
                new Corner(LEAF, CornerDirection.BR));
        playCard.turnFaceUp();
        player.getHand().addCard(() -> playCard);

        board.placeCard(player, playCard, card.getCorner(CornerDirection.TL));
        PlayArea playArea = board.getPlayerAreas().get(player);
        playArea.addListener(new SimpleListener(new StubClient("PIPPO", new StubView())));
        List<SerializableCorner> freeCorners1 = new ArrayList<>();
        freeCorners1.add(new SerializableCorner("S0", CornerDirection.BR.toString()));

        CountDownLatch latch1 = new CountDownLatch(2);
        Timer timer1 = new Timer();
        timer1.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                latch1.countDown();
            }
        }, 500, 500);
        latch1.await();
        timer1.cancel();
        assertEquals( freeCorners1 ,view.getFreeCorners());
        assertEquals(view.getVisibleResources(), board.getPlayerAreas().get(player).getVisibleResources());
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
    void testDeckCreationEvents() throws InterruptedException, DeckInstantiationException {
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