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

    public boolean isConnected() {
        return isConnected;
    }
    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    @Override
    public void setCards(List<ViewPlayCard> cards){
        if(cards != null)
            cards.forEach(ViewCard::turnFaceDown);
        super.setCards(cards);
    }
    @Override
    public void setSecretObjectiveCards(List<ViewObjectiveCard> secretObjectiveCards){
        if(secretObjectiveCards != null)
            secretObjectiveCards.forEach(ViewCard::turnFaceDown);
        super.setSecretObjectiveCards(secretObjectiveCards);
    }
    @Override
    public void setStartCard(ViewStartCard startCard){
        if(startCard != null) startCard.turnFaceDown();
        super.setStartCard(startCard);
    }
}
