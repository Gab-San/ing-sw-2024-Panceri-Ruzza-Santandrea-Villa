package it.polimi.ingsw.view.model;

import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.view.SceneID;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.events.DisplayEvent;
import it.polimi.ingsw.view.model.cards.ViewObjectiveCard;
import it.polimi.ingsw.view.model.cards.ViewPlayCard;
import it.polimi.ingsw.view.model.cards.ViewStartCard;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class ViewHand extends JComponent {
    protected final String nickname;
    protected final List<ViewPlayCard> cards;
    protected ViewStartCard startCard;
    protected final List<ViewObjectiveCard> secretObjectiveCards;
    protected PlayerColor color;
    protected boolean isDeadlocked;
    private int turn;
    protected final View view;

    public static final String COLOR_PROPERTY = "PROPERTY_COLOR";

    public ViewHand(String nickname, View view) {
        this.nickname = nickname;
        this.view = view;
        this.cards = Collections.synchronizedList(new LinkedList<>());
        startCard = null;
        isDeadlocked = false;
        this.secretObjectiveCards = Collections.synchronizedList(new LinkedList<>());
        color = null;
        turn = 0;
    }

    public String getNickname(){
        return nickname;
    }
    public List<ViewPlayCard> getCards(){
        return Collections.unmodifiableList(cards);
    }
    public ViewPlayCard getCardByID(String cardID) throws IllegalArgumentException{
        synchronized (cards){
            return cards.stream().filter(c->c.getCardID().equals(cardID))
                    .findFirst().orElseThrow(()->new IllegalArgumentException("Card isn't in your hand!"));
        }
    }
    public List<ViewObjectiveCard> getSecretObjectives(){
        return Collections.unmodifiableList(secretObjectiveCards);
    }

    public synchronized ViewStartCard getStartCard(){
        return startCard;
    }
    protected synchronized void setStartCard(ViewStartCard startCard){
        this.startCard = startCard;
    }
    public synchronized void clearStartCard(){
        startCard = null;
    }

    public synchronized void setCards(List<ViewPlayCard> cards){
        this.cards.clear();
        if(cards == null) return;
        this.cards.addAll(cards);
    }
    public synchronized void addCard(ViewPlayCard card){
        if(card == null) return;
        this.cards.add(card);
    }
    public synchronized void removeCard(ViewPlayCard card){
        if(card == null) return;
        this.cards.remove(card);
    }
    protected synchronized void setSecretObjectiveCards(List<ViewObjectiveCard> secretObjectiveCards){
        this.secretObjectiveCards.clear();
        if(secretObjectiveCards == null) return;
        this.secretObjectiveCards.addAll(secretObjectiveCards);
    }
    protected synchronized void addSecretObjectiveCard(ViewObjectiveCard secretObjectiveCard){
        if(secretObjectiveCard == null) return;
        this.secretObjectiveCards.add(secretObjectiveCard);
    }
    public synchronized void chooseObjective(String choiceID){
        ViewObjectiveCard choice = null;
        for(ViewObjectiveCard obj : secretObjectiveCards){
            if(obj.getCardID().equals(choiceID))
                choice = obj;
        }
        if(choice != null){
            secretObjectiveCards.clear();
            addSecretObjectiveCard(choice);
        }
    }

    public synchronized PlayerColor getColor() {
        return color;
    }
    public synchronized boolean setColor(PlayerColor color) {
        boolean changed = this.color != color;
        this.color = color;
        return changed;
    }

    public synchronized int getTurn() {
        return turn;
    }
    public synchronized boolean setTurn(int turn) {
        boolean changed = this.turn != turn;
        this.turn = turn;
        return changed;
    }

    public boolean isDeadlocked() {
        return isDeadlocked;
    }
    public void setDeadlocked(boolean deadlocked) {
        isDeadlocked = deadlocked;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
    }

    public synchronized void notifyView(SceneID scene, DisplayEvent event){
        view.update(scene, event);
    }
}
