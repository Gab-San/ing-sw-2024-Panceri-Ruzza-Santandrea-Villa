package it.polimi.ingsw.view.model;

import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.view.model.cards.ViewCard;
import it.polimi.ingsw.view.model.cards.ViewObjectiveCard;
import it.polimi.ingsw.view.model.cards.ViewPlayCard;
import it.polimi.ingsw.view.model.cards.ViewStartCard;

import java.util.LinkedList;
import java.util.List;

import static it.polimi.ingsw.view.tui.ConsoleBackgroundColors.getColorFromEnum;
import static it.polimi.ingsw.view.tui.ConsoleTextColors.RESET;

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
        // If a player connection is set and the status is the same as before
        // either the player was disconnected and is still disconnected or
        // they were connected and they are still
        boolean changed = isConnected != connected;
        isConnected = connected;
        view.notifyOpponentUpdate(nickname, nickname + " " +
                (isConnected ? (changed ? "reconnected" : "connected") : "disconnected"));
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
        view.notifyOpponentUpdate(nickname, nickname + " has drawn a card");
        super.addCard(card);
    }

    @Override
    public synchronized void removeCard(ViewPlayCard card) {
        super.removeCard(card);
        view.notifyOpponentUpdate(nickname, nickname + " has used a card in their hand");
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
        view.notifyOpponentUpdate(nickname, nickname + " has received an objective");
        super.addSecretObjectiveCard(objectiveCard);
    }

    @Override
    public synchronized void chooseObjective(String choiceID) {
        super.chooseObjective(choiceID);
        view.notifyOpponentUpdate(nickname, nickname + " has chosen their objective");
    }

    @Override
    public void setStartCard(ViewStartCard startCard){
        if(startCard != null) startCard.turnFaceDown();
        if(startingCardId != null)
            view.notifyOpponentUpdate(nickname, nickname + " has received their starting card");
        else view.notifyOpponentUpdate(nickname,  nickname + " has placed their starting card");
        super.setStartCard(startCard);
    }

    @Override
    public synchronized boolean setColor(PlayerColor color) {
        if(super.setColor(color)){
            view.notifyOpponentUpdate(nickname, nickname + "'s color was set to " + getColorFromEnum(color) + color + RESET);
            return true;
        }
        return false;
    }

    @Override
    public synchronized boolean setTurn(int turn) {
        if(super.setTurn(turn)){
            view.notifyOpponentUpdate(nickname, nickname + "'s turn was set to " + turn);
            return true;
        }
        return false;
    }
}
