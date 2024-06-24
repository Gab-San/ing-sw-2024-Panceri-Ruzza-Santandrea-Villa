package it.polimi.ingsw.view.model;

import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.view.SceneID;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.events.DisplayEvent;
import it.polimi.ingsw.view.gui.ChangeNotifications;
import it.polimi.ingsw.view.model.cards.ViewObjectiveCard;
import it.polimi.ingsw.view.model.cards.ViewPlayCard;
import it.polimi.ingsw.view.model.cards.ViewStartCard;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * A generic player's hand.
 * Also contains the player's information such as
 * nickname, color, turn and deadlock status.
 */
public abstract class ViewHand extends JComponent {
    protected final String nickname;
    /**
     * List of cards in player's hand
     */
    protected final List<ViewPlayCard> cards;
    protected ViewStartCard startCard;
    /**
     * List of secret objectives of this player.
     * Contains 2 cards during objective selection and 1 card after that.
     */
    protected final List<ViewObjectiveCard> secretObjectiveCards;
    protected PlayerColor color;
    protected boolean isDeadlocked;
    private int turn;
    protected final View view;

    /**
     * Constructs the ViewHand
     * @param nickname the player nickname
     * @param view the View this hand should notify events to.
     */
    protected ViewHand(String nickname, View view) {
        this.nickname = nickname;
        this.view = view;
        this.cards = Collections.synchronizedList(new LinkedList<>());
        startCard = null;
        isDeadlocked = false;
        this.secretObjectiveCards = Collections.synchronizedList(new LinkedList<>());
        color = null;
        turn = 0;
    }

    /**
     * @return this player's nickname
     */
    public String getNickname(){
        return nickname;
    }

    /**
     * @return list of PlayCards in this hand as an unmodifiable list.
     */
    public List<ViewPlayCard> getCards(){
        return Collections.unmodifiableList(cards);
    }

    /**
     * @param cardID the desired card's ID
     * @return the card in this hand with given ID
     * @throws IllegalArgumentException if there is no card with given ID in this hand
     */
    public ViewPlayCard getCardByID(String cardID) throws IllegalArgumentException{
        synchronized (cards){
            return cards.stream().filter(c->c.getCardID().equals(cardID))
                    .findFirst().orElseThrow(()->new IllegalArgumentException("Card "+ cardID +" isn't in your hand!"));
        }
    }
    /**
     * @return list of ObjectiveCards in this hand as an unmodifiable list.
     */
    public List<ViewObjectiveCard> getSecretObjectives(){
        return Collections.unmodifiableList(secretObjectiveCards);
    }

    /**
     * @return the starting card in this hand.
     *        Could be null if the player has no start card in hand.
     */
    public synchronized ViewStartCard getStartCard(){
        return startCard;
    }

    /**
     * Sets the start card in this hand.
     * @param startCard the starting card, can be null to set a state of "not having a starting card in hand"
     */
    protected synchronized void setStartCard(ViewStartCard startCard){
        if(startCard == null)
            firePropertyChange(ChangeNotifications.CLEAR_STARTING_CARD, this.startCard, null);
        else
            firePropertyChange(ChangeNotifications.SET_STARTING_CARD, this.startCard, startCard);

        this.startCard = startCard;
    }

    /**
     * Sets the playCard in hand to the given list of cards. <br>
     * Note that the playCard are added by reference to the hand's list,
     * but the list reference itself doesn't change. <br>
     * Changes to the list passed as a parameter after this method call
     * will not reflect in the hand. Changes to the playCard will.
     * @param cards list of cards to set as the hand content.
     */
    public synchronized void setCards(List<ViewPlayCard> cards){
        if(cards == null) return;
        firePropertyChange(ChangeNotifications.CLEAR_PLAY_CARDS, this.cards, null);
        this.cards.clear();
        cards.forEach(this::addCard);
    }

    /**
     * Adds a playCard to the hand.
     * @param card playCard to add to the hand.
     */
    public synchronized void addCard(ViewPlayCard card){
        if(card == null) return;
        this.cards.add(card);
        firePropertyChange(ChangeNotifications.ADD_CARD_HAND, null, card);
    }
    /**
     * Removes a playCard from the hand.
     * @param card playCard to remove from the hand.
     */
    public synchronized void removeCard(ViewPlayCard card){
        if(card == null) return;
        for(ViewPlayCard playCard : cards){
            if(playCard.equals(card)){
                firePropertyChange(ChangeNotifications.REMOVE_CARD_HAND, playCard, null);
            }
        }
        this.cards.remove(card);
    }
    /**
     * Sets the objectiveCards in hand to the given list of objectiveCards. <br>
     * Note that the objectiveCards are added by reference to the hand's list,
     * but the list reference itself doesn't change. <br>
     * Changes to the list passed as a parameter after this method call
     * will not reflect in the hand. Changes to the objectiveCards will.
     * @param objectiveCards list of objectiveCards to set as the hand content.
     */
    protected synchronized void setSecretObjectiveCards(List<ViewObjectiveCard> objectiveCards){
        firePropertyChange(ChangeNotifications.CLEAR_OBJECTIVES, secretObjectiveCards, null);
        this.secretObjectiveCards.clear();
        if(objectiveCards == null) return;
        for(ViewObjectiveCard objCard : objectiveCards){
            addSecretObjectiveCard(objCard);
        }
    }
    /**
     * Adds an objectiveCard to the hand.
     * @param secretObjectiveCard objectiveCard to add to the hand.
     */
    protected synchronized void addSecretObjectiveCard(ViewObjectiveCard secretObjectiveCard){
        if(secretObjectiveCard == null) return;
        this.secretObjectiveCards.add(secretObjectiveCard);
        firePropertyChange(ChangeNotifications.ADD_SECRET_CARD, null, secretObjectiveCard);
    }

    /**
     * Removes all objectiveCards that do *not* have the given ID
     * @param choiceID the chosen objectiveCard's ID
     */
    public synchronized void chooseObjective(String choiceID){
        ViewObjectiveCard choice = null;
        for(ViewObjectiveCard obj : secretObjectiveCards){
            if(obj.getCardID().equals(choiceID))
                choice = obj;
        }
        if(choice != null){
            firePropertyChange(ChangeNotifications.CLEAR_OBJECTIVES, secretObjectiveCards,null);
            secretObjectiveCards.clear();
            addSecretObjectiveCard(choice);
        }
        firePropertyChange(ChangeNotifications.CHOSEN_OBJECTIVE_CARD, null, choice);
    }

    /**
     * @return this player's color
     */
    public synchronized PlayerColor getColor() {
        return color;
    }

    /**
     * Sets this player's color
     * @param color the new player color (can be null to remove the color)
     * @return true if the color changed with this assignment
     */
    public synchronized boolean setColor(PlayerColor color) {
        boolean changed = this.color != color;
        if(changed) firePropertyChange(ChangeNotifications.COLOR_CHANGE, this.color, color);
        this.color = color;
        return changed;
    }

    /**
     * @return this player's turn
     */
    public synchronized int getTurn() {
        return turn;
    }

    /**
     * Sets this player's turn
     * @param turn the new player turn
     * @return true if the player's turn changed with this assignment
     */
    public synchronized boolean setTurn(int turn) {
        boolean changed = this.turn != turn;
        if(changed) firePropertyChange(ChangeNotifications.PLAYER_TURN_UPDATE, this.turn, turn);
        this.turn = turn;
        return changed;
    }

    /**
     * @return true if this player is deadlocked
     */
    public boolean isDeadlocked() {
        return isDeadlocked;
    }

    /**
     * Sets the deadlock status for this player
     * @param deadlocked new deadlock status
     */
    public void setDeadlocked(boolean deadlocked) {
        firePropertyChange(ChangeNotifications.PLAYER_DEADLOCK_UPDATE, this.isDeadlocked, deadlocked);
        isDeadlocked = deadlocked;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
    }

    /**
     * Notifies the View of an event
     * @param scene scene ID concerned by the event
     * @param event the event to be notified to the scene
     */
    public synchronized void notifyView(SceneID scene, DisplayEvent event){
        view.update(scene, event);
    }

    /**
     * Public override of this hand's firePropertyChange (would otherwise be protected)
     */
    public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        super.firePropertyChange(propertyName, oldValue, newValue);
    }
}
