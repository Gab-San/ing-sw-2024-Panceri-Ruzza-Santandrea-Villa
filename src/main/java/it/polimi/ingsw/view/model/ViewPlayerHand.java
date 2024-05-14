package it.polimi.ingsw.view.model;

import it.polimi.ingsw.view.model.cards.ViewObjectiveCard;
import it.polimi.ingsw.view.model.cards.ViewPlayCard;
import it.polimi.ingsw.view.model.cards.ViewStartCard;

import java.util.LinkedList;
import java.util.List;

public class ViewPlayerHand {
    private final String nickname;
    private final List<ViewPlayCard> cards;
    private ViewStartCard startCard;
    private final List<ViewObjectiveCard> secretObjectiveCards;

    public ViewPlayerHand(String nickname) {
        this.nickname = nickname;
        this.cards = new LinkedList<>();
        startCard = null;
        this.secretObjectiveCards = new LinkedList<>();
    }

    public void setCards(List<ViewPlayCard> cards){
        this.cards.clear();
        this.cards.addAll(cards);
    }
    public void setSecretObjectiveCards(List<ViewObjectiveCard> secretObjectiveCards){
        this.secretObjectiveCards.clear();
        this.secretObjectiveCards.addAll(secretObjectiveCards);
    }
    public void setStartCard(ViewStartCard startCard){
        this.startCard = startCard;
    }
    public void clearStartCard(){
        startCard = null;
    }

    /**
     * Flips the card in hand at position index
     * @param index position of the card to flip (1-3)
     * @throws IndexOutOfBoundsException if index < 1 or index > number of cards in hand
     */
    public void flipCard(int index) throws IndexOutOfBoundsException{
        cards.get(index-1).flip();
    }
}
