package it.polimi.ingsw.view.model;

import it.polimi.ingsw.view.model.cards.ViewObjectiveCard;
import it.polimi.ingsw.view.model.cards.ViewPlayCard;
import it.polimi.ingsw.view.model.cards.ViewStartCard;

import java.util.Collections;
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

    public String getNickname(){
        return nickname;
    }
    public List<ViewPlayCard> getCards(){
        return Collections.unmodifiableList(cards);
    }
    public List<ViewObjectiveCard> getSecretObjectives(){
        return Collections.unmodifiableList(secretObjectiveCards);
    }
    public ViewStartCard getStartCard(){
        return startCard;
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
     * Flips the card in hand at position index (not valid for flipping starting card)
     * @param index position of the card to flip (1-3)
     * @throws IndexOutOfBoundsException if index < 1 or index > number of cards in hand
     */
    public void flipCard(int index) throws IndexOutOfBoundsException{
        cards.get(index-1).flip();
    }
    /**
     * Flips the card in hand identified by cardID (also valid for flipping starting card)
     * @param cardID ID of the card to flip (including starting card)
     * @throws IllegalArgumentException there is no card in hand with given cardID
     */
    public void flipCard(String cardID) throws IllegalArgumentException{
        for(ViewPlayCard c : cards){
            if(c.getCardID().equals(cardID)) {
                c.flip();
                return;
            }
        }
        if(startCard != null){
            boolean matchesStartingCard = startCard.getCardID().equals(cardID) || cardID.matches("[sS]tarting");
            if(matchesStartingCard){
                startCard.flip();
                return;
            }
        }

        throw new IllegalArgumentException("Card " + cardID + " is not in this hand.");
    }
}
