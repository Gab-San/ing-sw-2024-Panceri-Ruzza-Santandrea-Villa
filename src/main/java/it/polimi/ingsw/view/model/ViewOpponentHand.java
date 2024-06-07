package it.polimi.ingsw.view.model;

import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.view.SceneID;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.events.DisplayEvent;
import it.polimi.ingsw.view.events.update.*;
import it.polimi.ingsw.view.model.cards.ViewCard;
import it.polimi.ingsw.view.model.cards.ViewObjectiveCard;
import it.polimi.ingsw.view.model.cards.ViewPlayCard;
import it.polimi.ingsw.view.model.cards.ViewStartCard;

import java.util.List;

public class ViewOpponentHand extends ViewHand{
    private boolean isConnected;

    public ViewOpponentHand(String nickname, View view) {
        super(nickname, view);
        isConnected = true;
    }

    public synchronized boolean isConnected() {
        return isConnected;
    }
    public synchronized void setConnected(boolean connected) {
        // If a player connection is set and the status is the same as before
        // either the player was disconnected and is still disconnected or
        // they were connected and they are still
        boolean changed = isConnected != connected;
        isConnected = connected;
        notifyView(SceneID.getOpponentAreaSceneID(nickname), new DisplayConnection(nickname,
                isConnected, changed));
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
        notifyView(SceneID.getOpponentAreaSceneID(nickname),
                new DisplayAddedCard(nickname, false, cards));
        super.addCard(card);
    }

    @Override
    public synchronized void removeCard(ViewPlayCard card) {
        super.removeCard(card);
        notifyView(SceneID.getOpponentAreaSceneID(nickname),
                new DisplayRemoveCards(nickname, false, cards));
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
        notifyView(SceneID.getOpponentAreaSceneID(nickname), new DisplayAddedObjective(nickname, false,
                secretObjectiveCards));
        super.addSecretObjectiveCard(objectiveCard);
    }

    @Override
    public synchronized void chooseObjective(String choiceID) {
        super.chooseObjective(choiceID);
        notifyView(SceneID.getOpponentAreaSceneID(nickname),
                new DisplayChosenObjective(nickname, false, choiceID));
    }

    @Override
    public void setStartCard(ViewStartCard startCard){
        if(startCard != null) startCard.turnFaceDown();
        // Check into event
        notifyView(SceneID.getOpponentAreaSceneID(nickname),
                    new DisplayStartingCard(nickname, false, this.startCard == null, startCard));
        super.setStartCard(startCard);
    }

    @Override
    public synchronized boolean setColor(PlayerColor color) {
        if(super.setColor(color)){
            notifyView(SceneID.getOpponentAreaSceneID(nickname),
                    new DisplayPlayerColor(nickname, false, color));
            return true;
        }
        return false;
    }

    @Override
    public synchronized boolean setTurn(int turn) {
        if(super.setTurn(turn)){
            notifyView(SceneID.getOpponentAreaSceneID(nickname),
                    new DisplayPlayerTurn(nickname, false, turn));
            return true;
        }
        return false;
    }
}
