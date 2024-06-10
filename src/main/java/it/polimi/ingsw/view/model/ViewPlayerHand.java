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
 * The local player's hand.
 * Also contains the player's information such as
 * nickname, color, turn and deadlock status.
 */
public class ViewPlayerHand extends ViewHand {
    /**
     * Constructs the local player hand
     * @param nickname the player's nickname
     * @param view the View this hand should notify events to.
     */
    public ViewPlayerHand(String nickname, View view) {
        super(nickname, view);
    }

    /**
     * @param cards list of cards to set as the hand content. <br>
     *             Turns the cards face-down before setting them to the hand.
     */
    @Override
    public void setCards(List<ViewPlayCard> cards){
        if(cards != null)
            cards.forEach(ViewCard::turnFaceUp);
        super.setCards(cards);
    }

    /**
     * Returns card at given index
     * @param idx (0-2) the 0-based card index to return
     * @return the playCard in hand at given index
     * @throws IndexOutOfBoundsException the index does not correspond to a card in hand.
     */
    public ViewPlayCard getCard(int idx) throws IndexOutOfBoundsException{
        return cards.get(idx);
    }

    /**
     * Adds a playCard to the hand. <br>
     * Also notifies the addCard event to myArea scene.
     * Turns the card face-up before adding it to hand.
     */
    @Override
    public void addCard(ViewPlayCard card){
        if(card != null)
            card.turnFaceUp();
        notifyView(SceneID.getMyAreaSceneID(),
                 new DisplayAddedCard(nickname, true, cards));
        super.addCard(card);
    }
    /**
     * Removes a playCard from the hand. <br>
     * Also notifies the removeCard event to myArea scene.
     */
    @Override
    public synchronized void removeCard(ViewPlayCard card) {
        super.removeCard(card);
        notifyView(SceneID.getMyAreaSceneID(),
                new DisplayRemoveCards(nickname, true, cards));
    }
    /**
     * @param secretObjectiveCards list of objectiveCards to set as the hand content. <br>
     *                      Turns all objective cards face-up before setting them to the hand.
     */
    @Override
    public void setSecretObjectiveCards(List<ViewObjectiveCard> secretObjectiveCards){
        if(secretObjectiveCards != null)
            secretObjectiveCards.forEach(ViewCard::turnFaceUp);
        super.setSecretObjectiveCards(secretObjectiveCards);
    }
    /**
     * Adds a objectiveCard to the hand. <br>
     * Turns the objectiveCard face-up before adding it to hand. <br>
     * Also notifies the addObjective event to myArea scene.
     * @param objectiveCard objectiveCard to add to the hand. <br>
     */
    @Override
    public void addSecretObjectiveCard(ViewObjectiveCard objectiveCard){
        if(objectiveCard != null)
            objectiveCard.turnFaceUp();
        notifyView(SceneID.getMyAreaSceneID(),
                new DisplayAddedObjective(nickname, true, secretObjectiveCards));
        super.addSecretObjectiveCard(objectiveCard);
    }

    /**
     * Removes all objectiveCards that do *not* have the given ID. <br>
     * Also notifies the chosen objective event to myArea scene.
     */
    @Override
    public synchronized void chooseObjective(String choiceID) {
        super.chooseObjective(choiceID);
        notifyView(SceneID.getMyAreaSceneID(),
                new DisplayChosenObjective(nickname, true, choiceID));
    }
    /**
     * Sets the start card in this hand. <br>
     * Turns the start card face-up before setting it to the hand. <br>
     * Also notifies the starting card update event to myArea scene.
     */
    @Override
    public void setStartCard(ViewStartCard startCard){
        if(startCard != null) startCard.turnFaceUp();
        notifyView(SceneID.getMyAreaSceneID(),
                new DisplayStartingCard(nickname, true, this.startCard == null, startCard));
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

    /**
     * Sets this player's color. <br>
     * Also notifies the color update event to myArea scene.
     * if the color changed with this assignment.
     */
    @Override
    public synchronized boolean setColor(PlayerColor color) {
        if(super.setColor(color)){
            firePropertyChange(ChangeNotifications.COLOR_CHANGE, null, color);
            notifyView(SceneID.getMyAreaSceneID(),
                    new DisplayPlayerColor(nickname, true, color));
            return true;
        }
        return false;
    }

    /**
     * Sets this player's turn. <br>
     * Also notifies the turn update event to myArea scene if
     * the turn changed with this assignment.
     */
    @Override
    public synchronized boolean setTurn(int turn) {
        if(super.setTurn(turn)){
            notifyView(SceneID.getMyAreaSceneID(),
                    new DisplayPlayerTurn(nickname, true, turn));
            return true;
        }
        return false;
    }

    /**
     * Sets the deadlock status for this player. <br>
     * Also notifies the deadlock event to myArea scene if
     * the deadlock status was set to true with this assignment.
     */
    @Override
    public void setDeadlocked(boolean deadlocked) {
        super.setDeadlocked(deadlocked);
        if(deadlocked)
            notifyView(SceneID.getMyAreaSceneID(), new DisplayDeadLock(nickname, true));
    }
}
