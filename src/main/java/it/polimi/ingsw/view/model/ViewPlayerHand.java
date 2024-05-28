package it.polimi.ingsw.view.model;

import it.polimi.ingsw.view.model.cards.ViewCard;
import it.polimi.ingsw.view.model.cards.ViewObjectiveCard;
import it.polimi.ingsw.view.model.cards.ViewPlayCard;
import it.polimi.ingsw.view.model.cards.ViewStartCard;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ViewPlayerHand extends ViewHand {
    public ViewPlayerHand(String nickname) {
        super(nickname);
    }
    @Override
    public void setSecretObjectiveCards(List<ViewObjectiveCard> secretObjectiveCards){
        if(secretObjectiveCards != null)
            secretObjectiveCards.forEach(ViewCard::turnFaceUp);
        super.setSecretObjectiveCards(secretObjectiveCards);
    }
    @Override
    public void addSecretObjectiveCard(ViewObjectiveCard objectiveCard){
        if(objectiveCard != null)
            objectiveCard.turnFaceUp();
        super.addSecretObjectiveCard(objectiveCard);
    }
    @Override
    public void setStartCard(ViewStartCard startCard){
        if(startCard != null) startCard.turnFaceUp();
        super.setStartCard(startCard);
    }

    /**
     * Flips the card in hand at position index (not valid for flipping starting card)
     * @param index position of the card to flip (1-3)
     * @throws IndexOutOfBoundsException if <br>- index < 1 <br>- index > number of cards in hand
     */
    public void flipCard(int index) throws IndexOutOfBoundsException{
        getCards().get(index-1).flip();
    }
    /**
     * Flips the card in hand identified by cardID (also valid for flipping starting card)
     * @param cardID ID of the card to flip (including starting card)
     * @throws IllegalArgumentException there is no card in hand with given cardID
     */
    public void flipCard(String cardID) throws IllegalArgumentException{
        for(ViewPlayCard c : getCards()){
            if(c.getCardID().equals(cardID)) {
                c.flip();
                return;
            }
        }
        if(getStartCard() != null){
            boolean matchesStartingCard = getStartCard().getCardID().equals(cardID) || cardID.matches("[sS]tarting|STARTING");
            if(matchesStartingCard){
                getStartCard().flip();
                return;
            }
        }

        throw new IllegalArgumentException("Card " + cardID + " is not in this hand.");
    }
}
