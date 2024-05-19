package it.polimi.ingsw.view;

import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.view.model.ViewBoard;
import it.polimi.ingsw.view.model.ViewDeck;
import it.polimi.ingsw.view.model.ViewOpponentHand;
import it.polimi.ingsw.view.model.cards.ViewCard;
import it.polimi.ingsw.view.model.cards.ViewGoldCard;
import it.polimi.ingsw.view.model.cards.ViewObjectiveCard;
import it.polimi.ingsw.view.model.cards.ViewResourceCard;

import javax.swing.text.View;

// synchronized is on all model methods
public class ModelUpdater {
    private final ViewBoard board;

    public ModelUpdater(ViewBoard board) {
        this.board = board;
    }
    //TODO: UI synchronize and refresh on all update methods

    void createPlayer(String nickname, boolean isConnected, int turn, PlayerColor color) {
        if (board.getPlayerHand().getNickname().equals(nickname)) {
            board.getPlayerHand().setTurn(turn);
            board.getPlayerHand().setColor(color);
        }
        else{
            board.addPlayer(nickname);
            ViewOpponentHand hand = board.getOpponentHand(nickname);
            hand.setConnected(isConnected);
            hand.setTurn(turn);
            hand.setColor(color);
        }
    }
    void updatePlayer(String nickname, PlayerColor color){
        if(board.getPlayerHand().getNickname().equals(nickname))
            board.getPlayerHand().setColor(color);
        else board.getOpponentHand(nickname).setColor(color);
    }
    void updatePlayer(String nickname, int playerTurn){
        if(board.getPlayerHand().getNickname().equals(nickname))
            board.getPlayerHand().setTurn(playerTurn);
        else board.getOpponentHand(nickname).setTurn(playerTurn);
    }
    void updatePlayer(String nickname, boolean isConnected){
        if(!board.getPlayerHand().getNickname().equals(nickname))
            board.getOpponentHand(nickname).setConnected(isConnected);
    }

    private void deckCardTypeMismatch(){
        throw new IllegalArgumentException("Deck type and Card type mismatch");
    }
    private void illegalArgument(){
        throw new IllegalArgumentException("Illegal argument passed.");
    }
    void createDeck(char deck, String topCardId, String firstRevealedId, String secondRevealedId){
        try {
            //TODO: import from json
            ViewCard topCard = null;
            ViewCard firstRevealed = null;
            ViewCard secondRevealed = null;
            switch (deck) {
                case ViewBoard.RESOURCE_DECK:
                    board.getResourceCardDeck().setTopCard((ViewResourceCard) topCard);
                    board.getResourceCardDeck().setFirstRevealed((ViewResourceCard) firstRevealed);
                    board.getResourceCardDeck().setSecondRevealed((ViewResourceCard) secondRevealed);
                    break;
                case ViewBoard.GOLD_DECK:
                    board.getGoldCardDeck().setTopCard((ViewGoldCard) topCard);
                    board.getGoldCardDeck().setFirstRevealed((ViewGoldCard) firstRevealed);
                    board.getGoldCardDeck().setSecondRevealed((ViewGoldCard) secondRevealed);
                    break;
                case ViewBoard.OBJECTIVE_DECK:
                    board.getObjectiveCardDeck().setTopCard((ViewObjectiveCard) topCard);
                    board.getObjectiveCardDeck().setFirstRevealed((ViewObjectiveCard) firstRevealed);
                    board.getObjectiveCardDeck().setSecondRevealed((ViewObjectiveCard) secondRevealed);
                    break;
                default: return;
            }
        }catch (ClassCastException e){
            //TODO: handle argument error, maybe just return (ignore wrong update?)
            deckCardTypeMismatch();
        }
    }
    void deckReveal(char deck, String revealedId, int cardPosition){
        ViewCard topCard = switch (deck){
            case ViewBoard.RESOURCE_DECK -> board.getResourceCardDeck().getTopCard();
            case ViewBoard.GOLD_DECK -> board.getGoldCardDeck().getTopCard();
            case ViewBoard.OBJECTIVE_DECK -> board.getObjectiveCardDeck().getTopCard();
            default -> null;
        };
        if(revealedId == null || cardPosition > 2 || cardPosition < 1) illegalArgument();

        if(!topCard.getCardID().equals(revealedId)){
            //TODO: import from JSON here
            ViewCard cardFromJSON = topCard;
            topCard = cardFromJSON;
        }
        try {
            boolean first = cardPosition == 1;
            switch (deck) {
                case ViewBoard.RESOURCE_DECK:
                    if (first) board.getResourceCardDeck().setFirstRevealed((ViewResourceCard) topCard);
                    else board.getResourceCardDeck().setSecondRevealed((ViewResourceCard) topCard);
                    break;
                case ViewBoard.GOLD_DECK:
                    if (first) board.getGoldCardDeck().setFirstRevealed((ViewGoldCard) topCard);
                    else board.getGoldCardDeck().setSecondRevealed((ViewGoldCard) topCard);
                    break;
                case ViewBoard.OBJECTIVE_DECK:
                    if (first) board.getObjectiveCardDeck().setFirstRevealed((ViewObjectiveCard) topCard);
                    else board.getObjectiveCardDeck().setSecondRevealed((ViewObjectiveCard) topCard);
                    break;
                default: return;
            }
        }catch (ClassCastException e){
            //TODO: handle argument error, maybe just return (ignore wrong update?)
            deckCardTypeMismatch();
        }
    }
    void deckUpdate(char deck, String topCardId){
        try {
            //TODO: import from json
            ViewCard topCard = null;
            switch (deck) {
                case ViewBoard.RESOURCE_DECK:
                    board.getResourceCardDeck().setTopCard((ViewResourceCard) topCard);
                    break;
                case ViewBoard.GOLD_DECK:
                    board.getGoldCardDeck().setTopCard((ViewGoldCard) topCard);
                    break;
                case ViewBoard.OBJECTIVE_DECK:
                    board.getObjectiveCardDeck().setTopCard((ViewObjectiveCard) topCard);
                    break;
                default: return;
            }
        }catch (ClassCastException e){
            //TODO: handle argument error, maybe just return (ignore wrong update?)
            deckCardTypeMismatch();
        }
    }
    void emptyDeck(char deck){
        switch (deck) {
            case ViewBoard.RESOURCE_DECK:
                board.getResourceCardDeck().setTopCard(null);
                break;
            case ViewBoard.GOLD_DECK:
                board.getGoldCardDeck().setTopCard(null);
                break;
            case ViewBoard.OBJECTIVE_DECK:
                board.getObjectiveCardDeck().setTopCard(null);
                break;
            default: return;
        }
    }
    void emptyReveal(char deck, int position){
        if(position > 2 || position < 1) illegalArgument();
        boolean first = position == 1;
        switch (deck) {
            case ViewBoard.RESOURCE_DECK:
                if (first) board.getResourceCardDeck().setFirstRevealed(null);
                else board.getResourceCardDeck().setSecondRevealed(null);
                break;
            case ViewBoard.GOLD_DECK:
                if (first) board.getGoldCardDeck().setFirstRevealed(null);
                else board.getGoldCardDeck().setSecondRevealed(null);
                break;
            case ViewBoard.OBJECTIVE_DECK:
                if (first) board.getObjectiveCardDeck().setFirstRevealed(null);
                else board.getObjectiveCardDeck().setSecondRevealed(null);
                break;
            default: return;
        }
    }
}
