package it.polimi.ingsw.view.model;

import it.polimi.ingsw.view.model.cards.ViewCard;
import it.polimi.ingsw.view.model.cards.ViewObjectiveCard;
import it.polimi.ingsw.view.model.cards.ViewPlayCard;
import it.polimi.ingsw.view.model.cards.ViewStartCard;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class ViewHand {
    private final String nickname;
    private final List<ViewPlayCard> cards;
    private ViewStartCard startCard;
    private final List<ViewObjectiveCard> secretObjectiveCards;

    public ViewHand(String nickname) {
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
        if(cards == null) return;
        this.cards.addAll(cards);
    }
    protected void setSecretObjectiveCards(List<ViewObjectiveCard> secretObjectiveCards){
        this.secretObjectiveCards.clear();
        if(secretObjectiveCards == null) return;
        this.secretObjectiveCards.addAll(secretObjectiveCards);
    }
    protected void setStartCard(ViewStartCard startCard){
        this.startCard = startCard;
    }
    public void clearStartCard(){
        startCard = null;
    }

}
