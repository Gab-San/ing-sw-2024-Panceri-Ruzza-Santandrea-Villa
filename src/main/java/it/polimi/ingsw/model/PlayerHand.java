package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.StartingCard;

import java.util.LinkedList;
import java.util.List;

public class PlayerHand{
    static final int MAX_CARDS = 3;
    final List<PlayCard> cards;
    ObjectiveCard secretObjective;
    //TODO: change objective to a list to allow for the selection during game setup??
    StartingCard startingCard;

    PlayerHand(){
        cards = new LinkedList<>();
        secretObjective = null;
        startingCard = null;
    }

    @Override
    public boolean equals(Object other) {
        if(other == this) return true;
        if(other instanceof PlayerHand hand){
            return cards.equals(hand.cards) &&
                    secretObjective.equals(hand.secretObjective) &&
                    startingCard.equals(hand.startingCard);
        }
        else return false;
    }

    public boolean containsCard(PlayCard card) {
        for(PlayCard c : cards){
            if(c == card) return true;
        }
        return false;
    }
    public PlayCard getCard(int pos){
        if (pos < 0 || pos >= cards.size()) return null;
        else return cards.get(pos);
    }
    public void addCard(PlayCard card) throws RuntimeException{
        if(cards.size() > MAX_CARDS) throw new RuntimeException("Too many cards in hand!");
        cards.add(card);
    }
    public void removeCard(PlayCard card) throws RuntimeException{
        if(!cards.contains(card)) throw new RuntimeException("Card wasn't in hand!");
        cards.remove(card);
    }

    public ObjectiveCard getSecretObjective() {
        return secretObjective;
    }
    public void setSecretObjective(ObjectiveCard secretObjective) {
        this.secretObjective = secretObjective;
    }

    public StartingCard getStartingCard() {
        return startingCard;
    }
    public void setStartingCard(StartingCard startingCard) {
        this.startingCard = startingCard;
    }
}

