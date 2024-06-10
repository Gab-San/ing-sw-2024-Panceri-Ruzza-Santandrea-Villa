package it.polimi.ingsw.view.model;

import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.view.SceneID;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.events.update.*;
import it.polimi.ingsw.view.gui.ChangeNotifications;
import it.polimi.ingsw.view.model.cards.ViewCard;
import it.polimi.ingsw.view.model.cards.ViewObjectiveCard;
import it.polimi.ingsw.view.model.cards.ViewPlayCard;
import it.polimi.ingsw.view.model.cards.ViewStartCard;

import java.util.List;

/**
 * An opponent's hand.
 * Also contains the player's information such as
 * nickname, color, turn, connection and deadlock status.
 */
public class ViewOpponentHand extends ViewHand {
    private boolean isConnected;

    /**
     * Constructs an opponent hand
     * @param nickname the opponent's nickname
     * @param view the View this hand should notify events to.
     */
    public ViewOpponentHand(String nickname, View view) {
        super(nickname, view);
        isConnected = true;
    }

    /**
     * @return true if this opponent is connected
     */
    public synchronized boolean isConnected() {
        return isConnected;
    }

    /**
     * Sets the connection status of this opponent. <br>
     * Also notifies the connection event to this opponent's scene.
     * @param connected new connection value (true = online)
     */
    public synchronized void setConnected(boolean connected) {
        // If a player connection is set and the status is the same as before
        // either the player was disconnected and is still disconnected or
        // they were connected and they are still
        boolean changed = isConnected != connected;
        if(changed) firePropertyChange(ChangeNotifications.CONNECTION_CHANGE, this.isConnected, connected);
        isConnected = connected;
        notifyView(SceneID.getOpponentAreaSceneID(nickname), new DisplayConnection(nickname,
                isConnected, changed));
    }

    /**
     * @param cards list of cards to set as the hand content. <br>
     *             Turns the cards face-down before setting them to the hand.
     */
    @Override
    public void setCards(List<ViewPlayCard> cards){
        if(cards != null)
            cards.forEach(ViewCard::turnFaceDown);
        super.setCards(cards);
    }

    /**
     * Adds a playCard to the hand. <br>
     * Also notifies the addCard event to this opponent's scene.
     * Turns the card face-down before adding it to hand.
     */
    @Override
    public void addCard(ViewPlayCard card){
        if(card != null)
            card.turnFaceDown();
        notifyView(SceneID.getOpponentAreaSceneID(nickname),
                new DisplayAddedCard(nickname, false, cards));
        super.addCard(card);
    }

    /**
     * Removes a playCard from the hand. <br>
     * Also notifies the removeCard event to this opponent's scene.
     */
    @Override
    public synchronized void removeCard(ViewPlayCard card) {
        super.removeCard(card);
        notifyView(SceneID.getOpponentAreaSceneID(nickname),
                new DisplayRemoveCards(nickname, false, cards));
    }

    /**
     * @param secretObjectiveCards list of objectiveCards to set as the hand content. <br>
     *                      Turns all objective cards face-down before setting them to the hand.
     */
    @Override
    public void setSecretObjectiveCards(List<ViewObjectiveCard> secretObjectiveCards){
        if(secretObjectiveCards != null)
            secretObjectiveCards.forEach(ViewCard::turnFaceDown);
        super.setSecretObjectiveCards(secretObjectiveCards);
    }
    /**
     * Adds a objectiveCard to the hand. <br>
     * Turns the objectiveCard face-down before adding it to hand. <br>
     * Also notifies the addObjective event to this opponent's scene.
     * @param objectiveCard objectiveCard to add to the hand. <br>
     */
    @Override
    public void addSecretObjectiveCard(ViewObjectiveCard objectiveCard){
        if(objectiveCard != null)
            objectiveCard.turnFaceDown();
        notifyView(SceneID.getOpponentAreaSceneID(nickname), new DisplayAddedObjective(nickname, false,
                secretObjectiveCards));
        super.addSecretObjectiveCard(objectiveCard);
    }

    /**
     * Removes all objectiveCards that do *not* have the given ID. <br>
     * Also notifies the chosen objective event to this opponent's scene.
     */
    @Override
    public synchronized void chooseObjective(String choiceID) {
        super.chooseObjective(choiceID);
        notifyView(SceneID.getOpponentAreaSceneID(nickname),
                new DisplayChosenObjective(nickname, false, choiceID));
    }
    /**
     * Sets the start card in this hand. <br>
     * Turns the start card face-down before setting it to the hand. <br>
     * Also notifies the starting card update event to this opponent's scene.
     */
    @Override
    public void setStartCard(ViewStartCard startCard){
        if(startCard != null) startCard.turnFaceDown();
        // Check into event
        notifyView(SceneID.getOpponentAreaSceneID(nickname),
                    new DisplayStartingCard(nickname, false, this.startCard == null, startCard));
        super.setStartCard(startCard);
    }

    /**
     * Sets this opponent's color. <br>
     * Also notifies the color update event to this opponent's scene
     * if the color changed with this assignment.
     */
    @Override
    public synchronized boolean setColor(PlayerColor color) {
        if(super.setColor(color)){
            notifyView(SceneID.getOpponentAreaSceneID(nickname),
                    new DisplayPlayerColor(nickname, false, color));
            return true;
        }
        return false;
    }

    /**
     * Sets this opponent's turn. <br>
     * Also notifies the turn update event to this opponent's scene
     * if the turn changed with this assignment.
     */
    @Override
    public synchronized boolean setTurn(int turn) {
        if(super.setTurn(turn)){
            notifyView(SceneID.getOpponentAreaSceneID(nickname),
                    new DisplayPlayerTurn(nickname, false, turn));
            return true;
        }
        return false;
    }

    /**
     * Sets the deadlock status for this player. <br>
     * Also notifies the deadlock event to this opponent's scene if
     * the deadlock status was set to true with this assignment.
     */
    @Override
    public void setDeadlocked(boolean deadlocked) {
        super.setDeadlocked(deadlocked);
        if(deadlocked)
            notifyView(SceneID.getOpponentAreaSceneID(nickname), new DisplayDeadLock(nickname, false));
    }
}
