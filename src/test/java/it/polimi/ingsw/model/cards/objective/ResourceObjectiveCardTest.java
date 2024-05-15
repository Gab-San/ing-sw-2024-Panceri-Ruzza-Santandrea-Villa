package it.polimi.ingsw.model.cards.objective;

import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlaceableCard;
import it.polimi.ingsw.GameResource;
import org.junit.jupiter.api.Test;

import java.util.Hashtable;
import java.util.Map;

import static it.polimi.ingsw.CornerDirection.BR;
import static it.polimi.ingsw.CornerDirection.TR;
import static it.polimi.ingsw.GameResource.LEAF;
import static it.polimi.ingsw.GameResource.WOLF;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ResourceObjectiveCardTest extends BaseObjectiveCardTest {
    @Test
    void resourceObjectiveTest_singleResource(){
        Map<GameResource, Integer> map = new Hashtable<>();
        map.put(LEAF, 3);
        ObjectiveCard objectiveCardLEAF = new ObjectiveCard(new ResourceObjectiveStrategy(map),1);
        map = new Hashtable<>();
        map.put(WOLF, 3);
        ObjectiveCard objectiveCardWOLF = new ObjectiveCard(new ResourceObjectiveStrategy(map),1);

        assertEquals(0, objectiveCardLEAF.calculatePoints(playArea));
        assertEquals(0, objectiveCardWOLF.calculatePoints(playArea));

        PlaceableCard card1 = placePlayCard(LEAF, startingCard.getCorner(TR)); // covers butterfly
        PlaceableCard card2 = placePlayCard(LEAF, startingCard.getCorner(BR)); // covers mushroom

        assertEquals(9, playArea.getVisibleResources().get(LEAF));
        assertEquals(3, objectiveCardLEAF.calculatePoints(playArea));
        assertEquals(0, objectiveCardWOLF.calculatePoints(playArea));

        PlaceableCard card3 = placePlayCard(WOLF, card1.getCorner(BR));
        assertEquals(7, playArea.getVisibleResources().get(LEAF));
        assertEquals(5, playArea.getVisibleResources().get(WOLF));
        assertEquals(2, objectiveCardLEAF.calculatePoints(playArea));
        assertEquals(1, objectiveCardWOLF.calculatePoints(playArea));

        PlaceableCard card4 = placePlayCard(LEAF, card3.getCorner(TR), true);
        assertEquals(8, playArea.getVisibleResources().get(LEAF));
        assertEquals(4, playArea.getVisibleResources().get(WOLF));
        assertEquals(2, objectiveCardLEAF.calculatePoints(playArea));
        assertEquals(1, objectiveCardWOLF.calculatePoints(playArea));


        map = new Hashtable<>();
        map.put(LEAF, 3);
        ObjectiveCard objectiveCardLEAF2 = new ObjectiveCard(new ResourceObjectiveStrategy(map),1);
        assertNotEquals(objectiveCardLEAF, objectiveCardWOLF);
        assertEquals(objectiveCardLEAF, objectiveCardLEAF2);
    }
    @Test
    void resourceObjectiveTest_mixedResource() {
        Map<GameResource, Integer> map = new Hashtable<>();
        map.put(LEAF, 1);
        map.put(WOLF, 1);
        ObjectiveCard objectiveCardMIXED = new ObjectiveCard(new ResourceObjectiveStrategy(map),1);

        assertEquals(1, objectiveCardMIXED.calculatePoints(playArea));

        PlaceableCard card1 = placePlayCard(LEAF, startingCard.getCorner(TR)); // covers butterfly
        PlaceableCard card2 = placePlayCard(LEAF, startingCard.getCorner(BR)); // covers mushroom

        assertEquals(9, playArea.getVisibleResources().get(LEAF));
        assertEquals(1, playArea.getVisibleResources().get(WOLF)); // 1 WOLF on the starting card
        assertEquals(1, objectiveCardMIXED.calculatePoints(playArea));

        PlaceableCard card3 = placePlayCard(WOLF, card1.getCorner(BR));
        assertEquals(7, playArea.getVisibleResources().get(LEAF));
        assertEquals(5, playArea.getVisibleResources().get(WOLF));
        assertEquals(5, objectiveCardMIXED.calculatePoints(playArea));

        PlaceableCard card4 = placePlayCard(LEAF, card3.getCorner(TR), true);
        assertEquals(8, playArea.getVisibleResources().get(LEAF));
        assertEquals(4, playArea.getVisibleResources().get(WOLF));
        assertEquals(4, objectiveCardMIXED.calculatePoints(playArea));
    }
    @Test
    void resourceObjectiveTest_multiplePointsPerSolve() {
        Map<GameResource, Integer> map = new Hashtable<>();
        map.put(LEAF, 1);
        map.put(WOLF, 1);
        ObjectiveCard objectiveCardMIXED = new ObjectiveCard(new ResourceObjectiveStrategy(map),3);

        assertEquals(3, objectiveCardMIXED.calculatePoints(playArea));

        PlaceableCard card1 = placePlayCard(LEAF, startingCard.getCorner(TR)); // covers butterfly
        PlaceableCard card2 = placePlayCard(LEAF, startingCard.getCorner(BR)); // covers mushroom

        assertEquals(9, playArea.getVisibleResources().get(LEAF));
        assertEquals(1, playArea.getVisibleResources().get(WOLF)); // 1 WOLF on the starting card
        assertEquals(3, objectiveCardMIXED.calculatePoints(playArea));

        PlaceableCard card3 = placePlayCard(WOLF, card1.getCorner(BR));
        assertEquals(7, playArea.getVisibleResources().get(LEAF));
        assertEquals(5, playArea.getVisibleResources().get(WOLF));
        assertEquals(15, objectiveCardMIXED.calculatePoints(playArea));

        PlaceableCard card4 = placePlayCard(LEAF, card3.getCorner(TR), true);
        assertEquals(8, playArea.getVisibleResources().get(LEAF));
        assertEquals(4, playArea.getVisibleResources().get(WOLF));
        assertEquals(12, objectiveCardMIXED.calculatePoints(playArea));
    }

}
