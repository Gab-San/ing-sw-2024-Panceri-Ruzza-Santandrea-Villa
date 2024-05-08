package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.exceptions.DeckException;
import it.polimi.ingsw.model.exceptions.PlayerHandException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoardDeckTest extends BaseBoardTest{
    final int deckSleepTime = 3;
    @Test
    public void dealTest(){
        joinPlayers(4);
        try{
            for(Player player  : players){
                board.deal(Board.STARTING_DECK, player.getHand());
                board.deal(Board.OBJECTIVE_DECK, player.getHand());
                assertThrows(PlayerHandException.class, ()->board.deal(Board.OBJECTIVE_DECK, player.getHand()));
                assertThrows(PlayerHandException.class, ()->board.deal(Board.STARTING_DECK, player.getHand()));

                assertThrows(IllegalStateException.class, ()->board.deal(Board.RESOURCE_DECK, player.getHand()));
                assertThrows(IllegalStateException.class, ()->board.deal(Board.GOLD_DECK, player.getHand()));
            }
        }catch (DeckException e){
            fail("DeckException was raised: " + e.getMessage());
        }

        for(Player player  : players){
            assertNotNull(player.getHand().getStartingCard());
            assertEquals(2, player.getHand().getObjectiveChoices().size());
        }
    }
    @ParameterizedTest
    @ValueSource(chars={Board.RESOURCE_DECK, Board.GOLD_DECK, Board.OBJECTIVE_DECK, Board.STARTING_DECK})
    public void drawTest(char deck) throws InterruptedException{
        joinPlayers(4);
        try{
            for(Player player : players){
                board.drawTop(deck, player.getHand());
                Thread.sleep(deckSleepTime); // sleeps are needed to allow the deck threads to refill the deck in time
                board.drawFirst(deck, player.getHand());
                Thread.sleep(deckSleepTime);
                board.drawSecond(deck, player.getHand());
                Thread.sleep(deckSleepTime);

                assertDoesNotThrow(()->player.getHand().getCard(2));
                assertThrows(IndexOutOfBoundsException.class, ()->player.getHand().getCard(3));

                // the next draw throws exception due to playerHand being full
                assertThrows(IllegalStateException.class, ()->board.drawTop(deck, player.getHand()));

                player.getHand().popCard(2);
                assertDoesNotThrow(()->board.drawFirst(deck, player.getHand()));
                Thread.sleep(deckSleepTime); // sleeps are needed to allow the deck threads to refill the deck in time
            }
        }catch (DeckException e){
            fail("DeckException was raised: " + e.getMessage());
        }catch (IllegalStateException e){
            assertTrue(deck == Board.OBJECTIVE_DECK || deck == Board.STARTING_DECK,
                    "IllegalStateException was raised with msg: " + e.getMessage());
        }
    }
    @Test
    public void revealedObjectivesTest(){
        List<ObjectiveCard> revealedObjectiveList = board.getRevealedObjectives();
        assertEquals(2, revealedObjectiveList.size());
        assertNotEquals(revealedObjectiveList.get(0), revealedObjectiveList.get(1));
    }
    @ParameterizedTest
    @ValueSource(chars={Board.RESOURCE_DECK, Board.GOLD_DECK, Board.OBJECTIVE_DECK, Board.STARTING_DECK})
    public void drawToCompletion(char deck) throws InterruptedException{
        joinPlayers(4);
        Player player = players[0];
        int i=0;
        try{
            for (i = 0; i < 50; i++) {
                board.drawTop(deck, player.getHand());
                player.getHand().popCard(0);
                Thread.sleep(deckSleepTime);
            }
            fail("Drawing from empty deck did not raise IllegalStateException");
        }catch (DeckException e){
            assertEquals(38, i,
                "DeckException was raised at i=" + i + " with msg: " + e.getMessage());
        }catch (IndexOutOfBoundsException e){
            fail("IndexOutOfBoundsException was raised in popCard call at i=" + i + " with msg: " + e.getMessage());
        }
        catch (IllegalStateException e){
            assertTrue(deck == Board.OBJECTIVE_DECK || deck == Board.STARTING_DECK,
            "IllegalStateException was raised at i=" + i + " with msg: " + e.getMessage());
        }
    }
}
