package it.polimi.ingsw.view;

import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.Point;
import it.polimi.ingsw.view.model.ViewDeck;
import it.polimi.ingsw.view.model.ViewOpponentHand;
import it.polimi.ingsw.view.model.ViewPlayArea;
import it.polimi.ingsw.view.model.ViewPlayerHand;
import it.polimi.ingsw.view.model.cards.*;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.function.Supplier;

import static it.polimi.ingsw.view.ViewCardGenerator.*;

public class ViewBoardGenerator {
    static final Random random = new Random();

    private static PlayerColor getRandomPlayerColor(){
        PlayerColor[] colors = PlayerColor.values();
        return colors[random.nextInt(colors.length)];
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
        Queue<ViewPlaceableCard> cards = new LinkedList<>(getRandomCards(30, false));
        for(ViewPlaceableCard card : cards){
            int randomCornerIdx = random.nextInt(playArea.getFreeCorners().size());
            ViewCorner corner = playArea.getFreeCorners().get(randomCornerIdx);
            Point position = corner.getCardRef().getPosition().move(corner.getDirection());
            playArea.placeCard(position, card);
        }
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
