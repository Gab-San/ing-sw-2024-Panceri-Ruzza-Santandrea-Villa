package it.polimi.ingsw.model.cards.objective;

import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlaceableCard;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.model.enums.CornerDirection.*;
import static it.polimi.ingsw.model.enums.CornerDirection.BL;
import static it.polimi.ingsw.model.enums.GameResource.*;
import static it.polimi.ingsw.model.enums.GameResource.WOLF;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatternObjectiveCardTest extends BaseObjectiveCardTest {
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
    @Test
    void pattern_multipleSolvesPlaceFromCenterOfDiagonalTest(){
        ObjectiveCard objectiveCard = new ObjectiveCard(new PatternObjectiveStrategy(createPatternDIAG_BLUE()), 1);
        // diag1 makes one diagonal pattern
        // diag2 makes two diagonal patterns
        // diag3 makes two diagonal patterns
        PlaceableCard diag1, diag2;
        diag1 = placePlayCard(WOLF, startingCard.getCorner(TR));
        diag2 = placePlayCard(WOLF, startingCard.getCorner(BR));
        diag1 = placePlayCard(WOLF, diag1.getCorner(TR));
        diag1 = placePlayCard(WOLF, diag1.getCorner(TR));
        placePlayCard(WOLF, diag2.getCorner(TR), true);
        diag2 = placePlayCard(WOLF, diag2.getCorner(BL));
        diag2 = placePlayCard(WOLF, diag2.getCorner(BL), true);
        diag2 = placePlayCard(WOLF, diag2.getCorner(BL));
        diag2 = placePlayCard(WOLF, diag2.getCorner(BL), true);

        PlaceableCard diag3;
        diag3 = placePlayCard(WOLF, startingCard.getCorner(TL));
        placePlayCard(WOLF, diag3.getCorner(TR));
        diag3 = placePlayCard(WOLF, diag3.getCorner(BL));
        diag3 = placePlayCard(WOLF, diag3.getCorner(BL));
        diag3 = placePlayCard(WOLF, diag3.getCorner(BL));
        diag3 = placePlayCard(WOLF, diag3.getCorner(BL));

        assertEquals(5, objectiveCard.calculatePoints(playArea));
    }
}
