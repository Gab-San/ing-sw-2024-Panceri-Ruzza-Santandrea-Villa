package it.polimi.ingsw.view.model;

import it.polimi.ingsw.view.model.cards.ViewCard;
import it.polimi.ingsw.view.model.cards.ViewObjectiveCard;
import it.polimi.ingsw.view.model.cards.ViewPlayCard;
import it.polimi.ingsw.view.model.cards.ViewStartCard;

import java.util.LinkedList;
import java.util.List;

public class ViewOpponentHand extends ViewHand{
    private boolean isConnected;

    public ViewOpponentHand(String nickname) {
        super(nickname);
        isConnected = true;
    }

    public synchronized boolean isConnected() {
        return isConnected;
    }
    public synchronized void setConnected(boolean connected) {
        isConnected = connected;
    }

    @Override
    public void setCards(List<ViewPlayCard> cards){
        if(cards != null)
            cards.forEach(ViewCard::turnFaceDown);
        super.setCards(cards);
    }
    @Override
    public void addCard(ViewPlayCard card){
        if(card != null)
            card.turnFaceDown();
        super.addCard(card);
    }
    @Override
    public void removeCard(ViewPlayCard card){
        if(card != null)
            card.turnFaceDown();
        super.removeCard(card);
    }
    @Override
    public void setSecretObjectiveCards(List<ViewObjectiveCard> secretObjectiveCards){
        if(secretObjectiveCards != null)
            secretObjectiveCards.forEach(ViewCard::turnFaceDown);
        super.setSecretObjectiveCards(secretObjectiveCards);
    }
    @Override
    public void addSecretObjectiveCard(ViewObjectiveCard objectiveCard){
        if(objectiveCard != null)
            objectiveCard.turnFaceDown();
        super.addSecretObjectiveCard(objectiveCard);
    }
    @Override
    public void setStartCard(ViewStartCard startCard){
        if(startCard != null) startCard.turnFaceDown();
        super.setStartCard(startCard);
    }
}
