package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.model.exceptions.PlayerHandException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class PlayerHand{
    static final int MAX_CARDS = 3;
    private final List<PlayCard> cards;
    private final List<ObjectiveCard> secretObjective;
    private StartingCard startingCard;
    private static int MAX_OBJECTIVES = 2;
    private final Player playerRef;


    PlayerHand(Player playerRef){
        cards = new LinkedList<>();
        secretObjective = new ArrayList<>(2);
        startingCard = null;
        this.playerRef = playerRef;
    }

    @Override
    public boolean equals(Object other) {
        if(other == this) return true;
        if(other instanceof PlayerHand otherHand){
            return cards.equals(otherHand.cards) &&
                    secretObjective.equals(otherHand.secretObjective) &&
                    startingCard.equals(otherHand.startingCard);
        }
        else return false;
    }

    public boolean containsCard(@NotNull PlayCard card) {
        for(PlayCard c : cards){
            if(c == card) return true;
        }
        return false;
    }

    //TODO [GAMBA] Make a popCard that removes the card (method remove(idx) returns the card at index)
    public PlayCard getCard(int pos){
        if (pos < 0 || pos >= cards.size()) return null;
        else return cards.get(pos);
    }
    public void addCard(@NotNull PlayCard card) throws RuntimeException{
        if(cards.size() > MAX_CARDS) throw new RuntimeException("Too many cards in hand!");
        cards.add(card);
    }
    public void removeCard(@NotNull PlayCard card) throws RuntimeException{
        if(!cards.contains(card)) throw new RuntimeException("Card wasn't in hand!");
        cards.remove(card);
    }

    public ObjectiveCard getSecretObjective() {
        return secretObjective.getFirst();
    }

    public void setCard(ObjectiveCard secretObjective) throws PlayerHandException {
        if(this.secretObjective.size() >= MAX_OBJECTIVES){
            throw new PlayerHandException(playerRef, ObjectiveCard.class);
        }
        this.secretObjective.add(secretObjective);
    }
    public List<ObjectiveCard> getObjectiveChoices() {
        return secretObjective;
    }

    /**
     * @param choice index of the chosen secret objective (1 or 2)
     * @throws IndexOutOfBoundsException if choice <= 0 or choice > 2
     */
    public void chooseObjective(int choice) throws IndexOutOfBoundsException{
        if(secretObjective.isEmpty()) throw new RuntimeException("Objective choices not initialized.");
        if(MAX_OBJECTIVES == 1) throw new RuntimeException("Secret objective was already chosen.");

        secretObjective.remove(choice-1);
        MAX_OBJECTIVES--;
    }

    public StartingCard getStartingCard() {
        return startingCard;
    }
    public void setCard(StartingCard startingCard) throws PlayerHandException {
        if(startingCard != null){
            throw new PlayerHandException("Starting Card already set", playerRef, StartingCard.class);
        }
        this.startingCard = startingCard;
    }

}

