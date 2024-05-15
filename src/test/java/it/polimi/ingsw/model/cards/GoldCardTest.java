package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.PlayArea;
import it.polimi.ingsw.Point;
import it.polimi.ingsw.model.cards.cardstrategies.CornerCoverGoldCard;
import it.polimi.ingsw.model.cards.cardstrategies.ItemCountGoldCard;
import it.polimi.ingsw.model.cards.cardstrategies.SimpleGoldCard;
import it.polimi.ingsw.GameResource;
import org.junit.jupiter.api.*;

import java.util.Hashtable;
import java.util.Map;

import static it.polimi.ingsw.CornerDirection.*;
import static it.polimi.ingsw.GameResource.*;
import static org.junit.jupiter.api.Assertions.*;

class GoldCardTest {
    private static final PlayArea playArea = new PlayArea();
    private static int potionCardCornerPlaceIdx;
    private static int scrollCardCornerPlaceIdx;
    private static int cornerCardIdx;

    @BeforeAll
    public static void initializePlayArea(){
        StartingCard startingCard = new StartingCard(
                new GameResource[]{MUSHROOM},
                new Corner(WOLF,LEAF, TL),
                new Corner(null,WOLF, TR),
                new Corner(null, MUSHROOM, BL),
                new Corner(MUSHROOM, BUTTERFLY, BR)
        );
        // TURNING UP CARD
        startingCard.turnFaceUp();

        playArea.placeStartingCard(startingCard);

        System.out.println(startingCard.getCorner(TL).getCardRef().getPosition());

        ResourceCard leafOne = new ResourceCard(
                LEAF,
                new Corner(LEAF,TL),
                new Corner(LEAF, TR),
                new Corner(null, BR)
        );
        // TURNING UP CARD
        leafOne.turnFaceUp();

        int freeCornerIndex = 0;
        for(Corner corn : playArea.getFreeCorners()){
            if(corn.equals(startingCard.getCorner(TR))){
                freeCornerIndex = playArea.getFreeCorners().indexOf(corn);
                break;
            }
        }


        playArea.placeCard(leafOne, playArea.getFreeCorners().get(freeCornerIndex));

        System.out.println(leafOne.getCorner(TL).getCardRef().getPosition());

        ResourceCard leafTwo = new ResourceCard(
                LEAF,
                new Corner(LEAF, TL),
                new Corner(null, TR),
                new Corner(LEAF, BL)
        );
        // TURNING UP CARD
        leafTwo.turnFaceUp();

        for(Corner corn : playArea.getFreeCorners()){
            if(corn.equals(startingCard.getCorner(TL))){
                freeCornerIndex = playArea.getFreeCorners().indexOf(corn);
                break;
            }
        }

        playArea.placeCard(leafTwo, playArea.getFreeCorners().get(freeCornerIndex));

        System.out.println(leafTwo.getCorner(TL).getCardRef().getPosition());
        ResourceCard leafThree = new ResourceCard(
                LEAF,
                1,
                new Corner(null, TL),
                new Corner(null, TR),
                new Corner(LEAF, BL)
        );
        // TURNING UP CARD
        leafThree.turnFaceUp();

        for(Corner corn : playArea.getFreeCorners()){
            if(corn.equals(leafTwo.getCorner(TL))){
                freeCornerIndex = playArea.getFreeCorners().indexOf(corn);
                break;
            }
        }

        playArea.placeCard(leafThree, playArea.getFreeCorners().get(freeCornerIndex));

        System.out.println(leafThree.getCorner(TL).getCardRef().getPosition());
        ResourceCard leafFour = new ResourceCard(
                LEAF,
                new Corner(BUTTERFLY, TR),
                new Corner(LEAF, BR),
                new Corner(QUILL, BL)
        );
        // TURNING DOWN CARD
        leafFour.turnFaceDown();

        for(Corner corn : playArea.getFreeCorners()){
            if(corn.equals(leafThree.getCorner(TR))){
                freeCornerIndex = playArea.getFreeCorners().indexOf(corn);
                break;
            }
        }

        playArea.placeCard(leafFour, playArea.getFreeCorners().get(freeCornerIndex));
        System.out.println(leafFour.getCorner(TL).getCardRef().getPosition());
        ResourceCard mushroomOne = new ResourceCard(
                MUSHROOM,
                new Corner(QUILL, TR),
                new Corner(LEAF, BL),
                new Corner(MUSHROOM, BR)
        );
        // TURNING UP CARD
        mushroomOne.turnFaceUp();

        for(Corner corn : playArea.getFreeCorners()){
            if(corn.equals(startingCard.getCorner(BR))){
                freeCornerIndex = playArea.getFreeCorners().indexOf(corn);
                break;
            }
        }

        playArea.placeCard(mushroomOne, playArea.getFreeCorners().get(freeCornerIndex));
        System.out.println(mushroomOne.getCorner(TL).getCardRef().getPosition());
        ResourceCard butterflyOne = new ResourceCard(
                BUTTERFLY,
                new Corner(SCROLL, TL),
                new Corner(BUTTERFLY, TR),
                new Corner(MUSHROOM, BR)
        );
        // TURNING UP CARD
        butterflyOne.turnFaceUp();

        for(Corner corn : playArea.getFreeCorners()){
            if(corn.equals(mushroomOne.getCorner(TR))){
                freeCornerIndex = playArea.getFreeCorners().indexOf(corn);
                break;
            }
        }

        playArea.placeCard(butterflyOne, playArea.getFreeCorners().get(freeCornerIndex));
        System.out.println(butterflyOne.getCorner(TL).getCardRef().getPosition());

        ResourceCard butterflyTwo = new ResourceCard(
                BUTTERFLY,
                new Corner(null, TL),
                new Corner(BUTTERFLY, TR),
                new Corner(BUTTERFLY, BR)
        );
        // TURNING UP CARD
        butterflyTwo.turnFaceUp();

        for(Corner corn : playArea.getFreeCorners()){
            if(corn.equals(startingCard.getCorner(BL)) ){
                freeCornerIndex = playArea.getFreeCorners().indexOf(corn);
                break;
            }
        }

        playArea.placeCard(butterflyTwo, playArea.getFreeCorners().get(freeCornerIndex));
        System.out.println(butterflyTwo.getCorner(TL).getCardRef().getPosition());

        for(Corner corn : playArea.getFreeCorners()){
            if(corn.equals(leafOne.getCorner(TR)) ){
                potionCardCornerPlaceIdx = playArea.getFreeCorners().indexOf(corn);
                break;
            }
        }

        for(Corner corn : playArea.getFreeCorners()){
            if(corn.equals(leafFour.getCorner(TR)) ){
                scrollCardCornerPlaceIdx = playArea.getFreeCorners().indexOf(corn);
                break;
            }
        }

        for(Corner corn:playArea.getFreeCorners()){
            if(corn.equals(leafFour.getCorner(BR))){
                cornerCardIdx = playArea.getFreeCorners().indexOf(corn);
            }
        }

        System.out.println(playArea.getVisibleResources());
    }

    @Nested
    class ScrollGoldCardTest{
        private GoldCard testCard;
        @BeforeEach
        void setup(){
            Map<GameResource,Integer> plCost = new Hashtable<>();
            plCost.put(MUSHROOM, 1);
            plCost.put(LEAF, 2);
            testCard = new GoldCard(
                    LEAF,
                    1,
                    plCost,
                    new ItemCountGoldCard(SCROLL),
                    new Corner(null, TL),
                    new Corner(SCROLL, TR),
                    new Corner(null, BR)
            );
            // TURNING UP CARD
            testCard.turnFaceUp();
        }

        @Test
        void getPlacementCost() {
            Map<GameResource, Integer> placementCostMap = testCard.getPlacementCost();
            assertEquals(2, placementCostMap.get(LEAF));
            assertEquals(1, placementCostMap.get(MUSHROOM));
        }

        @Test
        void calculatePointsOnPlace() {
            playArea.placeCard(testCard, playArea.getFreeCorners().get(scrollCardCornerPlaceIdx));

            int placementPoints = testCard.calculatePointsOnPlace(playArea);
            assertEquals(2, placementPoints);
        }

    }

    @Nested
    class PotionGoldCardTest{
        private GoldCard testCard;
        @BeforeEach
        void setup(){
            Map<GameResource,Integer> plCost = new Hashtable<>();
            plCost.put(MUSHROOM, 2);
            plCost.put(LEAF, 1);
            testCard = new GoldCard(
                    MUSHROOM,
                    1,
                    plCost,
                    new ItemCountGoldCard(POTION),
                    new Corner(null, TL),
                    new Corner(POTION, TR),
                    new Corner(null, BR)
            );
            // TURNING UP CARD
            testCard.turnFaceUp();
        }

        @Test
        void getPlacementCost() {
            Map<GameResource, Integer> placementCostMap = testCard.getPlacementCost();
            assertEquals(1, placementCostMap.get(LEAF));
            assertEquals(2, placementCostMap.get(MUSHROOM));
        }

        @Test
        void calculatePointsOnPlace() {
            playArea.placeCard(testCard, playArea.getFreeCorners().get(potionCardCornerPlaceIdx));

            int placementPoints = testCard.calculatePointsOnPlace(playArea);
            assertEquals(1, placementPoints);
        }

    }

    @Nested
    class SimpleThreeGoldCardTest{
        private GoldCard testCard;

        @BeforeEach
        void setup(){
            Hashtable<GameResource, Integer> plCost = new Hashtable<>();
            plCost.put(BUTTERFLY, 3);
            testCard = new GoldCard(
                    BUTTERFLY,
                    3,
                    plCost,
                    new SimpleGoldCard(),
                    new Corner(QUILL, BL),
                    new Corner(null, BR)
            );
        }

        @Test
        void getPlacementCost() {
            testCard.turnFaceUp();
            Map<GameResource, Integer> placementCostMap = testCard.getPlacementCost();
            assertEquals(3, placementCostMap.get(BUTTERFLY));

            testCard.turnFaceDown();
            for(GameResource res: GameResource.values()){
                assertNull(testCard.getPlacementCost().get(res));
            }
        }

        @Test
        void calculatePointsOnPlace() {
            testCard.turnFaceUp();
            int placementPoints = testCard.calculatePointsOnPlace(playArea);
            assertEquals(3, placementPoints);

            testCard.turnFaceDown();
            assertEquals(0, testCard.calculatePointsOnPlace(playArea));
        }

    }


    @Nested
    class SimpleFiveGoldCardTest{
        private GoldCard testCard;

        @BeforeEach
        void setup(){
            Hashtable<GameResource, Integer> plCost = new Hashtable<>();
            plCost.put(LEAF, 5);
            testCard = new GoldCard(
                    LEAF,
                    5,
                    plCost,
                    new SimpleGoldCard(),
                    new Corner(null, TL),
                    new Corner(null, TR)
            );
        }

        @Test
        void getPlacementCost() {
            testCard.turnFaceUp();
            Map<GameResource, Integer> placementCostMap = testCard.getPlacementCost();
            assertEquals(5, placementCostMap.get(LEAF));

            testCard.turnFaceDown();
            for(GameResource res: GameResource.values()){
                assertNull(testCard.getPlacementCost().get(res));
            }
        }

        @Test
        void calculatePointsOnPlace() {
            testCard.turnFaceUp();
            int placementPoints = testCard.calculatePointsOnPlace(playArea);
            assertEquals(5, placementPoints);

            testCard.turnFaceDown();
            assertEquals(0, testCard.calculatePointsOnPlace(playArea));
        }

    }


    @Nested
    class CornerGoldCardTest{
        private GoldCard testCard;
        @BeforeEach
        void setup(){
            Map<GameResource, Integer> plCost = new Hashtable<>();
            plCost.put(LEAF, 3);
            plCost.put(MUSHROOM, 1);

            testCard = new GoldCard(
                    LEAF,
                    2,
                    plCost,
                    new CornerCoverGoldCard(),
                    new Corner(null, TL),
                    new Corner(null, BR),
                    new Corner(null, TR)
            );
            testCard.turnFaceUp();
        }
        @Test
        void getPlacementCost() {
            Map<GameResource, Integer> placementCostMap = testCard.getPlacementCost();
            assertEquals(3, placementCostMap.get(LEAF));
            assertEquals(1, placementCostMap.get(MUSHROOM));
        }

        @Test
        void calculatePointsOnPlace() {
            playArea.placeCard(testCard, playArea.getFreeCorners().get(cornerCardIdx));
            System.out.println(testCard.getCorner(TL).getCardRef().getPosition());

            int placementPoints = testCard.calculatePointsOnPlace(playArea);
            assertEquals(6, placementPoints);
        }
    }

    @Test
    @DisplayName("Equals: card with different strat")
    void equalsTest1(){
        Map<GameResource, Integer> plCost = new Hashtable<>();
        plCost.put(LEAF, 3);
        plCost.put(MUSHROOM, 1);

        GoldCard testCard1 = new GoldCard(
                LEAF,
                2,
                plCost,
                new CornerCoverGoldCard(),
                new Corner(null, TL),
                new Corner(null, BR),
                new Corner(null, TR)
        );


        GoldCard testCard2 = new GoldCard(
                LEAF,
                2,
                plCost,
                new SimpleGoldCard(),
                new Corner(null, TL),
                new Corner(null, BR),
                new Corner(null, TR)
        );

        assertNotEquals(testCard1,testCard2);
    }

    @Test
    @DisplayName("Equals: card with diff placement cost")
    void equalsTest2(){
        Map<GameResource, Integer> plCost = new Hashtable<>();
        plCost.put(LEAF, 3);
        plCost.put(MUSHROOM, 1);

        GoldCard testCard1 = new GoldCard(
                LEAF,
                2,
                plCost,
                new CornerCoverGoldCard(),
                new Corner(null, TL),
                new Corner(null, BR),
                new Corner(null, TR)
        );


        plCost = new Hashtable<>();
        plCost.put(LEAF, 5);
        GoldCard testCard2 = new GoldCard(
                LEAF,
                2,
                plCost,
                new CornerCoverGoldCard(),
                new Corner(null, TL),
                new Corner(null, BR),
                new Corner(null, TR)
        );

        assertNotEquals(testCard1,testCard2);
    }

    @Test
    @DisplayName("Equals: card with diff corners")
    void equalsTest3(){
        Map<GameResource, Integer> plCost = new Hashtable<>();
        plCost.put(LEAF, 3);
        plCost.put(MUSHROOM, 1);

        GoldCard testCard1 = new GoldCard(
                LEAF,
                2,
                plCost,
                new CornerCoverGoldCard(),
                new Corner(null, TL),
                new Corner(null, BR),
                new Corner(null, TR)
        );


        plCost = new Hashtable<>();
        plCost.put(LEAF, 5);
        GoldCard testCard2 = new GoldCard(
                LEAF,
                2,
                plCost,
                new CornerCoverGoldCard(),
                new Corner(null, TL),
                new Corner(null, TR)
        );

        assertNotEquals(testCard1,testCard2);
    }

    @Test
    @DisplayName("Equals: different subtype cards")
    void equalsTest(){
        Map<GameResource, Integer> plCost = new Hashtable<>();
        plCost.put(LEAF, 3);
        plCost.put(MUSHROOM, 1);

        GoldCard testCard1 = new GoldCard(
                LEAF,
                2,
                plCost,
                new CornerCoverGoldCard(),
                new Corner(null, TL),
                new Corner(null, BR),
                new Corner(null, TR)
        );


        GoldCard testCard2 = new GoldCard(
                LEAF,
                2,
                plCost,
                new SimpleGoldCard(),
                new Corner(null, TL),
                new Corner(null, BR),
                new Corner(null, TR)
        );

        ResourceCard resCard = new ResourceCard();

        assertNotEquals(resCard,testCard1);
        assertNotEquals(resCard,testCard2);
    }

    @Test
    @DisplayName("Equals: equal corner cover cards ")
    void equalsTest4(){
        Map<GameResource, Integer> plCost = new Hashtable<>();
        plCost.put(LEAF, 3);
        plCost.put(MUSHROOM, 1);

        GoldCard testCard1 = new GoldCard(
                LEAF,
                2,
                plCost,
                new CornerCoverGoldCard(),
                new Corner(null, TL),
                new Corner(null, BR),
                new Corner(null, TR)
        );

        GoldCard testCardCopy = new GoldCard(
                LEAF,
                2,
                plCost,
                new CornerCoverGoldCard(),
                new Corner(null, TL),
                new Corner(null, BR),
                new Corner(null, TR)
        );

        GoldCard placedCard = (GoldCard) testCardCopy.setPosition(new Point(2,2));

        assertEquals(testCard1,testCardCopy);
        assertEquals(placedCard, testCard1);
        assertEquals(placedCard,testCardCopy);
    }

    @Test
    @DisplayName("Equals: equal simple gold cards ")
    void equalsTest5(){
        Map<GameResource, Integer> plCost = new Hashtable<>();
        plCost.put(LEAF, 3);
        plCost.put(MUSHROOM, 1);

        GoldCard testCard1 = new GoldCard(
                LEAF,
                2,
                plCost,
                new SimpleGoldCard(),
                new Corner(null, TL),
                new Corner(null, BR),
                new Corner(null, TR)
        );

        GoldCard testCardCopy = new GoldCard(
                LEAF,
                2,
                plCost,
                new SimpleGoldCard(),
                new Corner(null, TL),
                new Corner(null, BR),
                new Corner(null, TR)
        );

        assertEquals(testCard1,testCardCopy);
    }

    @Test
    @DisplayName("Equals: equal item count cards ")
    void equalsTest6(){
        Map<GameResource, Integer> plCost = new Hashtable<>();
        plCost.put(LEAF, 3);
        plCost.put(MUSHROOM, 1);

        GoldCard testCard1 = new GoldCard(
                LEAF,
                2,
                plCost,
                new ItemCountGoldCard(SCROLL),
                new Corner(null, TL),
                new Corner(null, BR),
                new Corner(null, TR)
        );

        GoldCard testCardCopy = new GoldCard(
                LEAF,
                2,
                plCost,
                new ItemCountGoldCard(SCROLL),
                new Corner(null, TL),
                new Corner(null, BR),
                new Corner(null, TR)
        );

        GoldCard placedCard = (GoldCard) testCardCopy.setPosition(new Point(2,2));

        assertEquals(testCard1,testCardCopy);
        assertEquals(placedCard, testCard1);
        assertEquals(placedCard,testCardCopy);
    }


    @Test
    @DisplayName("Equals: diff item in item count cards ")
    void equalsTest7(){
        Map<GameResource, Integer> plCost = new Hashtable<>();
        plCost.put(LEAF, 3);
        plCost.put(MUSHROOM, 1);

        GoldCard testCard1 = new GoldCard(
                LEAF,
                2,
                plCost,
                new ItemCountGoldCard(SCROLL),
                new Corner(null, TL),
                new Corner(null, BR),
                new Corner(null, TR)
        );

        GoldCard testCardCopy = new GoldCard(
                LEAF,
                2,
                plCost,
                new ItemCountGoldCard(LEAF),
                new Corner(null, TL),
                new Corner(null, BR),
                new Corner(null, TR)
        );

        assertNotEquals(testCard1,testCardCopy);
    }


}