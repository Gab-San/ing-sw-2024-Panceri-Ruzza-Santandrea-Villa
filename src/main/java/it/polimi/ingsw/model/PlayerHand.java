package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.StartingCard;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class PlayerHand{
    static final int MAX_CARDS = 3;
    private final List<PlayCard> cards;
    private ObjectiveCard secretObjective;
    private ObjectiveCard[] objectiveChoices;
    //TODO: change objective to a list to allow for the selection during game setup??
    private StartingCard startingCard;

    PlayerHand(){
        cards = new LinkedList<>();
        secretObjective = null;
        objectiveChoices = new ObjectiveCard[2];
        startingCard = null;
    }

    @Override
    public boolean equals(Object other) {
        if(other == this) return true;
        if(other instanceof PlayerHand otherHand){
            return cards.equals(otherHand.cards) &&
                    secretObjective.equals(otherHand.secretObjective) &&
                    startingCard.equals(otherHand.startingCard) &&
                    Arrays.equals(objectiveChoices, otherHand.objectiveChoices);
        }
        else return false;
    }

    public boolean containsCard(@NotNull PlayCard card) {
        for(PlayCard c : cards){
            if(c == card) return true;
        }
        return false;
    }
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
        return secretObjective;
    }
    public void setSecretObjective(ObjectiveCard secretObjective) {
        this.secretObjective = secretObjective;
    }
    public ObjectiveCard[] getObjectiveChoices() {
        return objectiveChoices;
    }
    public void setObjectiveChoices(@NotNull ObjectiveCard firstObjectiveChoice, @NotNull ObjectiveCard secondObjectiveChoice) {
        objectiveChoices[0] = firstObjectiveChoice;
        objectiveChoices[1] = secondObjectiveChoice;
    }

    /**
     * @param choice index of the chosen secret objective (1 or 2)
     * @throws IndexOutOfBoundsException if choice <= 0 or choice > 2
     */
    public void chooseObjective(int choice) throws IndexOutOfBoundsException{
        if(objectiveChoices[0] == null || objectiveChoices[1] == null) throw new RuntimeException("Objective choices not initialized.");
        if(secretObjective != null) throw new RuntimeException("Secret objective was already chosen.");

        setSecretObjective(objectiveChoices[choice-1]);
        objectiveChoices = null;
    }

    public StartingCard getStartingCard() {
        return startingCard;
    }
    public void setStartingCard(StartingCard startingCard) {
        this.startingCard = startingCard;
    }
}

