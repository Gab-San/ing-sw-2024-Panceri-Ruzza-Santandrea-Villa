package it.polimi.ingsw.view.model;

import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.view.SceneID;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.events.update.*;
import it.polimi.ingsw.view.model.cards.ViewCard;
import it.polimi.ingsw.view.model.cards.ViewObjectiveCard;
import it.polimi.ingsw.view.model.cards.ViewPlayCard;
import it.polimi.ingsw.view.model.cards.ViewStartCard;

import java.util.List;

public class ViewPlayerHand extends ViewHand {
    public ViewPlayerHand(String nickname, View view) {
        super(nickname, view);
    }
    @Override
    public void setCards(List<ViewPlayCard> cards){
        if(cards != null)
            cards.forEach(ViewCard::turnFaceUp);
        super.setCards(cards);
    }

    public ViewPlayCard getCard(int idx){
        return cards.get(idx);
    }

    @Override
    public void addCard(ViewPlayCard card){
        if(card != null)
            card.turnFaceUp();
        notifyView(SceneID.getMyAreaSceneID(),
                 new DisplayAddedCard(nickname, true, cards));
        super.addCard(card);
    }

    @Override
    public synchronized void removeCard(ViewPlayCard card) {
        super.removeCard(card);
        notifyView(SceneID.getMyAreaSceneID(),
                new DisplayRemoveCards(nickname, true, cards));
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
        notifyView(SceneID.getMyAreaSceneID(),
                new DisplayAddedObjective(nickname, true, secretObjectiveCards));
        super.addSecretObjectiveCard(objectiveCard);
    }

    @Override
    public synchronized void chooseObjective(String choiceID) {
        super.chooseObjective(choiceID);
        notifyView(SceneID.getMyAreaSceneID(),
                new DisplayChosenObjective(nickname, true, choiceID));
    }

    @Override
    public void setStartCard(ViewStartCard startCard){
        if(startCard != null) startCard.turnFaceUp();
        notifyView(SceneID.getMyAreaSceneID(),
                new DisplayStartingCard(nickname, true, startCard));
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

    @Override
    public synchronized boolean setColor(PlayerColor color) {
        if(super.setColor(color)){
            notifyView(SceneID.getMyAreaSceneID(),
                    new DisplayPlayerColor(nickname, true, color));
            return true;
        }
        return false;
    }

    @Override
    public synchronized boolean setTurn(int turn) {
        if(super.setTurn(turn)){
            notifyView(SceneID.getMyAreaSceneID(),
                    new DisplayPlayerTurn(nickname, true, turn));
            return true;
        }
        return false;
    }
}
