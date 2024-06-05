package it.polimi.ingsw.view;

import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.model.functions.UsefulFunc;
import it.polimi.ingsw.view.model.*;
import it.polimi.ingsw.view.model.cards.*;

import java.util.*;
import java.util.function.Supplier;

import static it.polimi.ingsw.view.ViewCardGenerator.*;

public class ViewBoardGenerator {
    static final Random random = new Random();

    public static PlayerColor getRandomPlayerColor(){
        PlayerColor[] colors = PlayerColor.values();
        return colors[random.nextInt(colors.length)];
    }

    public static PlayerColor getRandomAvailableColor(ViewBoard board){
        List<PlayerColor> colors = new LinkedList<>(List.of(PlayerColor.values()));
        board.getAllPlayerHands().forEach(h->colors.remove(h.getColor()));
        if(!colors.isEmpty())
            return colors.get(random.nextInt(colors.size()));
        else return null;
    }

    public static int getRandomScore(){
        return random.nextInt(30);
    }

    public static void fillHandRandomly(ViewPlayerHand hand){
        hand.setStartCard(getRandomStartingCard());
        hand.setCards(getRandomCards(3, true));
        hand.setSecretObjectiveCards(getRandomObjectiveCards(random.nextInt(2)+1));
        hand.setTurn(random.nextInt(5));
        hand.setColor(getRandomPlayerColor());
    }
    public static void fillHandRandomly(ViewOpponentHand hand){
        hand.setStartCard(getRandomStartingCard());
        hand.setCards(getRandomCards(3, true)); // allFront=true to test that all cards are being forced face down
        hand.setSecretObjectiveCards(getRandomObjectiveCards(random.nextInt(2)+1));
        hand.setTurn(random.nextInt(5));
        hand.setConnected(random.nextBoolean());
        hand.setColor(getRandomPlayerColor());
    }
    public static void fillPlayAreaRandomly(ViewPlayArea playArea){
        playArea.placeStarting(getRandomStartingCard());
        List<ViewPlaceableCard> cards = new LinkedList<>(getRandomCards(30, false));
        for(ViewPlaceableCard card : cards){
            int randomCornerIdx = random.nextInt(playArea.getFreeCorners().size());
            ViewCorner corner = playArea.getFreeCorners().get(randomCornerIdx);
            GamePoint position = corner.getCardRef().getPosition().move(corner.getDirection());
            playArea.placeCard(position, card);
        }
        int numOfVisibleResources = random.nextInt(30*4);
        int[] resAmount = new int[7];
        for (int i = 0; i < numOfVisibleResources; i++) {
            GameResource res = getRandomResource();
            if(res != null && res != GameResource.FILLED)
                resAmount[res.getResourceIndex()]++;
            else i--;
        }
        playArea.setVisibleResources(UsefulFunc.resourceArrayToMap(resAmount));
    }
    public static <C extends ViewCard> void fillDeckRandomly(ViewDeck<C> deck, Class<C> cardClass){
        Supplier<C> cardMaker;
        if (cardClass.equals(ViewResourceCard.class))
            cardMaker = () -> (C) getRandomResourceCard();
        else if (cardClass.equals(ViewGoldCard.class))
            cardMaker = () -> (C) getRandomGoldCard();
        else
            cardMaker = () -> (C) getRandomObjectiveCard(random.nextBoolean());

        deck.setTopCard(cardMaker.get());
        deck.setFirstRevealed(cardMaker.get());
        deck.setSecondRevealed(cardMaker.get());
    }
}
