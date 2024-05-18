package it.polimi.ingsw.view;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.view.model.cards.*;

import static it.polimi.ingsw.CornerDirection.*;

import java.util.*;

public class ViewCardGenerator {
    private static Random random=null;
    private static List<GameResource> resources;
    private static int numOfResources;

    enum Distribution{
        RANDOM,
        ONLY_RESOURCE,
        ONLY_GOLD
    }

    private static void setUp(){
        if(random != null) return;
        random = new Random();
        numOfResources = GameResource.values().length+1;
        resources = new ArrayList<>(numOfResources);
        resources.addAll(Arrays.asList(GameResource.values()));
        resources.add(null);
    }
    public static GameResource getRandomResource(){
        setUp();
        return resources.get(random.nextInt(numOfResources));
    }
    public static GameResource getRandomResourceNotNull(){
        setUp();
        return resources.get(random.nextInt(numOfResources-1));
    }
    public static List<ViewCorner> getRandomCornerList(){
        setUp();
        List<ViewCorner> cornerList = new LinkedList<>();
        cornerList.add(new ViewCorner(getRandomResource(), getRandomResource(), TL));
        cornerList.add(new ViewCorner(getRandomResource(), getRandomResource(), TR));
        cornerList.add(new ViewCorner(getRandomResource(), getRandomResource(), BR));
        cornerList.add(new ViewCorner(getRandomResource(), getRandomResource(), BL));
        return cornerList;
    }
    public static ViewStartCard getRandomStartingCard(){
        setUp();
        List<GameResource> centralResources = new LinkedList<>();
        GameResource res = null;
        while (res == null){
            res = getRandomResource();
            if(res != null) centralResources.add(res); // at least one non-null centralResource
        }
        centralResources.add(getRandomResource()); // the other 2 can be random
        centralResources.add(getRandomResource());
        return new ViewStartCard("S"+random.nextInt(9), "", "",
                getRandomCornerList(), centralResources);
    }
    public static ViewResourceCard getRandomResourceCard(){
        setUp();
        GameResource randColor;
        randColor = resources.get(random.nextInt(4)); // only the first 4 are valid colors
        return new ViewResourceCard("R"+random.nextInt(40), "", "",
                getRandomCornerList(), random.nextInt(2), randColor);
    }
    public static ViewGoldCard getRandomGoldCard(){
        setUp();
        GameResource randColor;
        randColor = resources.get(random.nextInt(4)); // only the first 4 are valid colors
        List<GameResource> placementCost = new LinkedList<>();
        int numOfRes = random.nextInt(3)+3;
        for (int i = 0; i < numOfRes; i++) {
            GameResource res = getRandomResource();
            if(res == null) i--;        // no null values in placementCost
            else placementCost.add(res);
        }

        return new ViewGoldCard("G"+random.nextInt(40), "", "",
                getRandomCornerList(), 1+random.nextInt(2), randColor, placementCost, "TEST");
    }
    public static List<ViewPlayCard> getRandomResourceCards(int num, boolean allFront){
        return getRandomCards(num,allFront,Distribution.ONLY_RESOURCE);
    }
    public static List<ViewPlayCard> getRandomGoldCards(int num, boolean allFront){
        return getRandomCards(num,allFront,Distribution.ONLY_GOLD);
    }
    public static List<ViewPlayCard> getRandomCards(int num, boolean allFront){
        return getRandomCards(num,allFront, Distribution.RANDOM);
    }
    private static List<ViewPlayCard> getRandomCards(int num, boolean allFront, Distribution distribution){
        setUp();
        List<ViewPlayCard> cards = new LinkedList<>();
        for (int i = 0; i < num; i++) {
            ViewPlayCard card =
            switch (distribution){
                case RANDOM -> random.nextBoolean() ? getRandomResourceCard() : getRandomGoldCard();
                case ONLY_RESOURCE -> getRandomResourceCard();
                case ONLY_GOLD -> getRandomGoldCard();
            };
            if(random.nextBoolean() || allFront) card.turnFaceUp();
            cards.add(card);
        }
        return cards;
    }
    private static ViewPlaceableCard makeEmptyCard(boolean faceUp){
        setUp();
        List<ViewCorner> listEmptyCorners = new LinkedList<>();
        for(CornerDirection dir : CornerDirection.values()){
            listEmptyCorners.add(new ViewCorner(null, null, dir));
        }
        ViewPlaceableCard card = new ViewResourceCard("RNO", "", "",
                listEmptyCorners, 0, GameResource.QUILL);
        if(faceUp) card.turnFaceUp();
        return card;
    }
    public static List<ViewPlaceableCard> getEmptyCards(int num, boolean faceUp){
        setUp();
        List<ViewPlaceableCard> cards = new LinkedList<>();
        for (int i = 0; i < num; i++) {
            cards.add(makeEmptyCard(faceUp));
        }
        return cards;
    }
    public static ViewObjectiveCard getRandomObjectiveCard(boolean patternType) {
        setUp();
        String type = patternType ? ViewObjectiveCard.PATTERN_TYPE : ViewObjectiveCard.RESOURCE_TYPE;
        StringBuilder value = new StringBuilder();
        int len = patternType ? 9 : random.nextInt(4)+2;
        for (int i = 1; i <= len; i++) {
            GameResource res = getRandomResourceNotNull();
            if(patternType)
                value.append(res.asColor());
            else value.append(res.toString());
            if(patternType && i%3 == 0 && i != len){
                value.append(" ");
            }
        }
        return new ViewObjectiveCard("O"+random.nextInt(16), "", "",
                type, value.toString());
    }
    public static List<ViewObjectiveCard> getRandomObjectiveCards(int num){
        setUp();
        List<ViewObjectiveCard> cards = new LinkedList<>();
        for (int i = 0; i < num; i++) {
            ViewObjectiveCard card = getRandomObjectiveCard(random.nextBoolean());
            cards.add(card);
        }
        return cards;
    }

}
