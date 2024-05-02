package it.polimi.ingsw.model.cards.objective;

import it.polimi.ingsw.model.PlayArea;
import it.polimi.ingsw.model.Point;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.enums.GameResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Hashtable;
import java.util.Map;

import static it.polimi.ingsw.model.enums.CornerDirection.*;
import static it.polimi.ingsw.model.enums.GameResource.*;
import static org.junit.jupiter.api.Assertions.*;

public class ObjectiveCardTest {
    PlayArea playArea;
    PlaceableCard startingCard;

    @BeforeEach
    void setUpPlayArea(){
        playArea = new PlayArea();
        StartingCard card = new StartingCard(
                new GameResource[]{LEAF, WOLF, MUSHROOM},
                new Corner(null, LEAF, TL),
                new Corner(null, BUTTERFLY, TR),
                new Corner(FILLED, WOLF, BL),
                new Corner(FILLED, MUSHROOM, BR)
        );
        playArea.placeStartingCard(card);
        startingCard = playArea.getCardMatrix().get(new Point(0,0));
        assertEquals(1, playArea.getVisibleResources().get(LEAF));
        assertEquals(1, playArea.getVisibleResources().get(WOLF));
        assertEquals(1, playArea.getVisibleResources().get(BUTTERFLY));
        assertEquals(1, playArea.getVisibleResources().get(MUSHROOM));
        // 1 resource of each after setup
    }
    private ResourceCard makeResourceCard(GameResource resource){
        return new ResourceCard(resource, 0,
                new Corner(resource, TL),
                new Corner(resource, TR),
                new Corner(resource, BL),
                new Corner(resource, BR)
        );
    }
    private PlaceableCard placePlayCard(GameResource resource, Corner corner, boolean placeOnBack){
        PlayCard card = makeResourceCard(resource);
        if(!placeOnBack) card.turnFaceUp();
        playArea.placeCard(card, corner);
        return card.getCorner(TL).getCardRef();
    }
    private PlaceableCard placePlayCard(GameResource resource, Corner corner) {
        return placePlayCard(resource,corner,false);
    }

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
    private PatternObjective createPatternDIAG_BLUE(){
        return new PatternObjective("**B *B* B**");
    }
    private PatternObjective createPattern_L_RED_GREEN(){
        return new PatternObjective("*R* *R* **G");
    }

    @Test
    void patternTest_DIAG_allFront(){
        ObjectiveCard objectiveCard = new ObjectiveCard(new PatternObjectiveStrategy(createPatternDIAG_BLUE()), 1);
        PlaceableCard card;
        card = placePlayCard(WOLF, startingCard.getCorner(TR));
        card = placePlayCard(WOLF, card.getCorner(TR));
        card = placePlayCard(WOLF, card.getCorner(TR));

        assertEquals(1, objectiveCard.calculatePoints(playArea));
    }
    @Test
    void patternTest_DIAG_allBack(){
        ObjectiveCard objectiveCard = new ObjectiveCard(new PatternObjectiveStrategy(createPatternDIAG_BLUE()), 2);
        PlaceableCard card;
        card = placePlayCard(WOLF, startingCard.getCorner(TR), true);
        card = placePlayCard(WOLF, card.getCorner(TR), true);
        card = placePlayCard(WOLF, card.getCorner(TR), true);

        assertEquals(2, objectiveCard.calculatePoints(playArea));
    }
    @Test
    void patternTest_DIAG_mixedSides(){
        ObjectiveCard objectiveCard = new ObjectiveCard(new PatternObjectiveStrategy(createPatternDIAG_BLUE()), 3);
        PlaceableCard card;
        card = placePlayCard(WOLF, startingCard.getCorner(TR));
        card = placePlayCard(WOLF, card.getCorner(TR), true);
        card = placePlayCard(WOLF, card.getCorner(TR), true);

        assertEquals(3, objectiveCard.calculatePoints(playArea));
    }
    @Test
    void patternTest_DIAG_reverseDiagonal(){
        ObjectiveCard objectiveCard = new ObjectiveCard(new PatternObjectiveStrategy(createPatternDIAG_BLUE()), 1);
        PlaceableCard card;
        card = placePlayCard(WOLF, startingCard.getCorner(TR));
        card = placePlayCard(WOLF, card.getCorner(TL), true);
        card = placePlayCard(WOLF, card.getCorner(TL), true);

        assertEquals(0, objectiveCard.calculatePoints(playArea));
    }

    @Test
    void patternTest_L_mixedSides(){
        ObjectiveCard objectiveCard = new ObjectiveCard(new PatternObjectiveStrategy(createPattern_L_RED_GREEN()), 4);
        PlaceableCard card;
        card = placePlayCard(MUSHROOM, startingCard.getCorner(TR));
        card = placePlayCard(MUSHROOM, startingCard.getCorner(BR), true);
        card = placePlayCard(LEAF, card.getCorner(BR), true);

        assertEquals(4, objectiveCard.calculatePoints(playArea));
    }
    @Test
    void pattern_DIAG_singleCardUseTest(){
        ObjectiveCard objectiveCard = new ObjectiveCard(new PatternObjectiveStrategy(createPatternDIAG_BLUE()), 1);
        PlaceableCard card;
        card = placePlayCard(WOLF, startingCard.getCorner(TR));
        card = placePlayCard(WOLF, card.getCorner(TR), true);
        card = placePlayCard(WOLF, card.getCorner(TR), true);
        card = placePlayCard(WOLF, card.getCorner(TR), true);
        card = placePlayCard(WOLF, card.getCorner(TR), true);
        // scenario: 5 blue cards in a diagonal

        assertEquals(1, objectiveCard.calculatePoints(playArea));
    }
    @Test
    void pattern_interruptedDIAGTest(){
        ObjectiveCard objectiveCard = new ObjectiveCard(new PatternObjectiveStrategy(createPatternDIAG_BLUE()), 1);
        PlaceableCard card;
        card = placePlayCard(WOLF, startingCard.getCorner(TR));
        card = placePlayCard(WOLF, card.getCorner(TR), true);
        card = placePlayCard(LEAF, card.getCorner(TR), true);
        card = placePlayCard(WOLF, card.getCorner(TR), true);
        card = placePlayCard(WOLF, card.getCorner(TR));

        assertEquals(0, objectiveCard.calculatePoints(playArea));
    }
    @Test
    void pattern_multipleSolvesTest(){
        ObjectiveCard objectiveCard = new ObjectiveCard(new PatternObjectiveStrategy(createPatternDIAG_BLUE()), 1);
        PlaceableCard card;
        card = placePlayCard(WOLF, startingCard.getCorner(TR));
        card = placePlayCard(WOLF, card.getCorner(TR), true);
        card = placePlayCard(WOLF, card.getCorner(TR));
        card = placePlayCard(WOLF, card.getCorner(TR), true);
        card = placePlayCard(WOLF, card.getCorner(TR));
        card = placePlayCard(WOLF, card.getCorner(TR));
        card = placePlayCard(WOLF, startingCard.getCorner(BL), true);
        card = placePlayCard(WOLF, card.getCorner(BL));
        card = placePlayCard(WOLF, card.getCorner(BL));

        assertEquals(3, objectiveCard.calculatePoints(playArea));
    }
}
