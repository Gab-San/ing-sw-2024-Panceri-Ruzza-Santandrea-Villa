package it.polimi.ingsw.view;

import it.polimi.ingsw.*;
import it.polimi.ingsw.model.listener.remote.events.playarea.CardPosition;
import it.polimi.ingsw.model.listener.remote.events.playarea.SerializableCorner;
import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.view.model.ViewBoard;
import it.polimi.ingsw.view.model.ViewOpponentHand;
import it.polimi.ingsw.view.model.ViewPlayArea;
import it.polimi.ingsw.view.model.ViewPlayerHand;
import it.polimi.ingsw.view.model.cards.*;
import it.polimi.ingsw.view.model.json.JsonImporter;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

// synchronization is done inside ViewModel already
//FIXME: remove "implements VirtualClient" (it's useful while implementing)
public class ModelUpdater implements VirtualClient {
    private final ViewBoard board;
    private final View view;
    private final boolean verbose;
    private final JsonImporter jsonImporter;


    public ModelUpdater(ViewBoard board, View view) {
        this(board, view, false);
    }
    public ModelUpdater(ViewBoard board, View view, boolean verbose) {
        this.board = board;
        this.view = view;
        this.verbose = verbose;
        jsonImporter = Client.getCardJSONImporter();
    }
    public void update(String msg) {
        view.showChatMessage(msg);
    }
    public void ping() { } //TODO: remove ping()

    public void notifyTimeoutDisconnect(){
        view.notifyTimeout();
    }
    private void notifyMyAreaUpdate(String msg){
        if(verbose)
            notifyView(msg);
        else
            view.update(SceneID.getMyAreaSceneID(), msg);
    }
    private void notifyOpponentUpdate(String nickname, String msg){
        if(verbose)
            notifyView(msg);
        else
            view.update(SceneID.getOpponentAreaSceneID(nickname), msg);
    }
    private void notifyBoardUpdate(String msg){
        if(verbose)
            notifyView(msg);
        else
            view.update(SceneID.getMyAreaSceneID(), msg);
    }
    private void notifyView(String msg){
        view.showNotification(msg);
    }
    public void reportError(String errorMessage) {
        view.showError(errorMessage);
    }
    public void showChatMessage(String message){
        view.showChatMessage(message);
    }
    public void setPlayerState(String nickname, boolean isConnected, int turn, PlayerColor color) {
        if (board.getPlayerHand().getNickname().equals(nickname)) {
            board.getPlayerHand().setTurn(turn);
            board.getPlayerHand().setColor(color);
            if(turn != 0 || color != null)
                notifyMyAreaUpdate("Your turn and color were set");
        }
        else{
            //getOpponentHand will also run addPlayer if it wasn't run before.
            ViewOpponentHand hand = board.getOpponentHand(nickname);
            hand.setConnected(isConnected);
            hand.setTurn(turn);
            hand.setColor(color);
            notifyOpponentUpdate(nickname, nickname + " joined the game!");
        }
    }
    public void updatePlayer(String nickname, PlayerColor color){
        if(board.getPlayerHand().getNickname().equals(nickname)) {
            if(board.getPlayerHand().setColor(color))
                notifyMyAreaUpdate("Your color was set");
        }
        else {
            if(board.getOpponentHand(nickname).setColor(color))
                notifyOpponentUpdate(nickname, nickname + "'s color was set");
        }
    }
    public void updatePlayer(String nickname, int playerTurn){
        if(board.getPlayerHand().getNickname().equals(nickname)){
            if(board.getPlayerHand().setTurn(playerTurn))
                notifyMyAreaUpdate("Your turn was set");
        }
        else{
            if(board.getOpponentHand(nickname).setTurn(playerTurn))
                notifyOpponentUpdate(nickname, nickname + "'s turn was set");
        }
    }
    public void updatePlayer(String nickname, boolean isConnected){
        if(!board.getPlayerHand().getNickname().equals(nickname)){
            board.getOpponentHand(nickname).setConnected(isConnected);
            String connectionString = isConnected ? "reconnected" : "disconnected";
            notifyOpponentUpdate(nickname, nickname + " " + connectionString);
        }
    }
    public void playerDeadLockUpdate(String nickname, boolean isDeadLocked) {
        board.setPlayerDeadlock(nickname, isDeadLocked);
        if(isDeadLocked)
            if(board.getPlayerHand().getNickname().equals(nickname))
                notifyMyAreaUpdate("You are deadlocked!");
            else
                notifyOpponentUpdate(nickname, nickname + " is deadlocked!");
    }


    private void deckCardTypeMismatch(){
        throw new IllegalArgumentException("Deck type and Card type mismatch");
    }
    private void illegalArgument(){
        throw new IllegalArgumentException("Illegal argument passed.");
    }
    private String getDeckName(char deck){
        return switch (deck) {
            case ViewBoard.RESOURCE_DECK -> "Resource deck";
            case ViewBoard.GOLD_DECK -> "Gold deck";
            case ViewBoard.OBJECTIVE_DECK -> "Objective deck";
            default -> "";
        };
    }
    public void setDeckState(char deck, String topId, String firstId, String secondId) {
        try {
            ViewCard topCard = jsonImporter.getCard(topId);
            ViewCard firstRevealed = jsonImporter.getCard(topId);
            ViewCard secondRevealed = jsonImporter.getCard(topId);
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
            if(topCard != null) // only false if the call comes from setDeckState(deck, firstCardId, secondCardId)
                notifyBoardUpdate(getDeckName(deck) + " was updated.");
        }catch (ClassCastException e){
            //FIXME: handle argument error, maybe just return (ignore wrong update?)
            deckCardTypeMismatch();
        }
    }
    public void deckUpdate(char deck, String revealedId, int cardPosition) {
        if(!Character.toString(deck).matches("[RGO]")) return;

        ViewCard topCard = switch (deck){
            case ViewBoard.RESOURCE_DECK -> board.getResourceCardDeck().getTopCard();
            case ViewBoard.GOLD_DECK -> board.getGoldCardDeck().getTopCard();
            case ViewBoard.OBJECTIVE_DECK -> board.getObjectiveCardDeck().getTopCard();
            default -> null; // never triggered
        };
        if(revealedId == null || cardPosition > 2 || cardPosition < 0) illegalArgument();

        if(topCard == null || !revealedId.equals(topCard.getCardID())){
            topCard = jsonImporter.getCard(revealedId);
        }
        try {
            switch (deck) {
                case ViewBoard.RESOURCE_DECK:
                    switch (cardPosition){
                        case 0:
                            board.getResourceCardDeck().setTopCard((ViewResourceCard) topCard);
                            break;
                        case 1:
                            board.getResourceCardDeck().setFirstRevealed((ViewResourceCard) topCard);
                            break;
                        case 2:
                            board.getResourceCardDeck().setSecondRevealed((ViewResourceCard) topCard);
                            break;
                    }
                    break;
                case ViewBoard.GOLD_DECK:
                    switch (cardPosition){
                        case 0:
                            board.getGoldCardDeck().setTopCard((ViewGoldCard) topCard);
                            break;
                        case 1:
                            board.getGoldCardDeck().setFirstRevealed((ViewGoldCard) topCard);
                            break;
                        case 2:
                            board.getGoldCardDeck().setSecondRevealed((ViewGoldCard) topCard);
                            break;
                    }
                    break;
                case ViewBoard.OBJECTIVE_DECK:
                    switch (cardPosition){
                        case 0:
                            board.getObjectiveCardDeck().setTopCard((ViewObjectiveCard) topCard);
                            break;
                        case 1:
                            board.getObjectiveCardDeck().setFirstRevealed((ViewObjectiveCard) topCard);
                            break;
                        case 2:
                            board.getObjectiveCardDeck().setSecondRevealed((ViewObjectiveCard) topCard);
                            break;
                    }
                    break;
            }
            if(cardPosition == 0){
                notifyBoardUpdate("The top card of " + getDeckName(deck) + " was drawn");
            }
            else{
                String cardPos = cardPosition == 1 ? "First" : "Second";
                notifyBoardUpdate(cardPos + " revealed card of " + getDeckName(deck) + " was drawn");
            }
        }catch (ClassCastException e){
            //TODO: handle argument error, maybe just return (ignore wrong update?)
            deckCardTypeMismatch();
        }
    }
    public void setDeckState(char deck, String revealedId, int cardPosition){
        deckUpdate(deck, revealedId, cardPosition);
    }
    public void setDeckState(char deck, String firstCardId, String secondCardId) {
        setDeckState(deck, null, firstCardId, secondCardId);
        notifyBoardUpdate(getDeckName(deck) + " is now empty! Only the 2 revealed cards remain.");
    }
    public void emptyFaceDownPile(char deck){
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
        notifyBoardUpdate("Last top card was drawn from " + getDeckName(deck) + ". It is now empty.");
    }
    public void emptyReveal(char deck, int position){
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
        String cardPos = first ? "First" : "Second";
        notifyBoardUpdate(cardPos + " revealed card of " + getDeckName(deck) + " was drawn. Deck is empty so no card has replaced it.");
    }
    public void setEmptyDeckState(char deck) {
        return;
        //FIXME: this isn't needed as decks are initialised empty
//        setDeckState(deck, null,null,null);
    }


    public void setBoardState(int currentTurn, GamePhase gamePhase) {
        if(board.setGamePhase(gamePhase) || board.setCurrentTurn(currentTurn))
            notifyBoardUpdate("Game phase and turn updated.");
    }
    public void updatePhase(GamePhase gamePhase) {
        if(board.setGamePhase(gamePhase))
            view.showNotification("Game phase updated to " + gamePhase);
    }
    public void updateTurn(int currentTurn) {
        if(board.setCurrentTurn(currentTurn))
            view.showNotification("Turn advanced to " + currentTurn);
    }
    public void updateScore(String nickname, int score) {
        board.setScore(nickname, score);
        notifyBoardUpdate(nickname + "'s score is now " + score);
    }

    public void setPlayerHandState(String nickname, List<String> playCardIDs, List<String> objectiveCardIDs, String startingCardID) {
        List<ViewPlayCard> cardsInHand = jsonImporter.getPlayCards(playCardIDs);
        List<ViewObjectiveCard> objectives = new LinkedList<>();
        objectiveCardIDs.forEach(id -> objectives.add(jsonImporter.getObjectiveCard(id)));
        ViewStartCard startCard = jsonImporter.getStartCard(startingCardID);

        if (board.getPlayerHand().getNickname().equals(nickname)) {
            ViewPlayerHand hand = board.getPlayerHand();
            hand.setCards(cardsInHand);
            hand.setSecretObjectiveCards(objectives);
            hand.setStartCard(startCard);

            notifyMyAreaUpdate("Your hand was set");
        }
        else{
            ViewOpponentHand hand = board.getOpponentHand(nickname);
            hand.setCards(cardsInHand);
            hand.setSecretObjectiveCards(objectives);
            hand.setStartCard(startCard);
            notifyOpponentUpdate(nickname, nickname + "'s hand was set");
        }
    }
    public void playerHandAddedCardUpdate(String nickname, String drawnCardId) {
        ViewPlayCard drawnCard = jsonImporter.getPlayCard(drawnCardId);

        if (board.getPlayerHand().getNickname().equals(nickname)) {
            ViewPlayerHand hand = board.getPlayerHand();
            hand.addCard(drawnCard);

            notifyMyAreaUpdate("Your have drawn a card");
        }
        else{
            ViewOpponentHand hand = board.getOpponentHand(nickname);
            hand.addCard(drawnCard);

            notifyOpponentUpdate(nickname, nickname + " has drawn a card");
        }
    }
    public void playerHandRemoveCard(String nickname, String playCardId) {
        ViewPlayCard card = jsonImporter.getPlayCard(playCardId);

        if (board.getPlayerHand().getNickname().equals(nickname)) {
            ViewPlayerHand hand = board.getPlayerHand();
            hand.removeCard(card);

            notifyMyAreaUpdate("You have used a card in your hand");
        }
        else{
            ViewOpponentHand hand = board.getOpponentHand(nickname);
            hand.removeCard(card);

            notifyOpponentUpdate(nickname, nickname + " has used a card in their hand");
        }
    }
    public void playerHandAddObjective(String nickname, String objectiveCard) {
        ViewObjectiveCard objective = jsonImporter.getObjectiveCard(objectiveCard);

        if (board.getPlayerHand().getNickname().equals(nickname)) {
            ViewPlayerHand hand = board.getPlayerHand();
            hand.addSecretObjectiveCard(objective);

            notifyMyAreaUpdate("You have received an objective");
        }
        else{
            ViewOpponentHand hand = board.getOpponentHand(nickname);
            hand.addSecretObjectiveCard(objective);

            notifyOpponentUpdate(nickname, nickname + " has received an objective");
        }
    }
    public void playerHandChooseObject(String nickname, String chosenObjectiveId) {
        if (board.getPlayerHand().getNickname().equals(nickname)) {
            ViewPlayerHand hand = board.getPlayerHand();
            hand.chooseObjective(chosenObjectiveId);
            notifyMyAreaUpdate("You have chosen your objective");
        }
        else{
            ViewOpponentHand hand = board.getOpponentHand(nickname);
            hand.chooseObjective(chosenObjectiveId);
            notifyOpponentUpdate(nickname, nickname + " has chosen their objective");
        }
    }
    public void playerHandSetStartingCard(String nickname, String startingCardId) {
        ViewStartCard startCard = jsonImporter.getStartCard(startingCardId);

        if (board.getPlayerHand().getNickname().equals(nickname)) {
            ViewPlayerHand hand = board.getPlayerHand();
            hand.setStartCard(startCard);

            notifyMyAreaUpdate("You have received your starting card");
        }
        else{
            ViewOpponentHand hand = board.getOpponentHand(nickname);
            hand.setStartCard(startCard);

            notifyOpponentUpdate(nickname, nickname + " has received their starting card");
        }
    }

    public void setPlayAreaState(String nickname, List<CardPosition> cardPositions, Map<GameResource, Integer> visibleResources, List<SerializableCorner> freeSerializableCorners) {
        ViewPlayArea playArea = board.getPlayerArea(nickname);
        playArea.clearFreeCorners();

        for(CardPosition pos : cardPositions){
            ViewPlaceableCard card = (ViewPlaceableCard) jsonImporter.getCard(pos.cardId());
            if(card != null) {
                playArea.setCard(new Point(pos.row(), pos.col()), card);
                List<ViewCorner> cardFreeCorners = freeSerializableCorners.stream()
                        .filter(c -> c.cardCornerId().equals(card.getCardID()))
                        .map(SerializableCorner::getCornerDirection)
                        .map(card::getCorner)
                        .distinct()
                        .toList();
                playArea.addFreeCorners(cardFreeCorners);
            }
        }
        playArea.setVisibleResources(visibleResources);

        if(board.getPlayerHand().getNickname().equals(nickname))
            notifyMyAreaUpdate("Your playArea was initialised");
        else
            notifyOpponentUpdate(nickname, nickname + "'s playArea was initialised");
    }
    public void updatePlaceCard(String nickname, String placedCardId, int row, int col) {
        ViewPlayArea playArea = board.getPlayerArea(nickname);
        ViewPlaceableCard card = (ViewPlaceableCard) jsonImporter.getCard(placedCardId);
        playArea.placeCard(new Point(row, col), card);

        if(board.getPlayerHand().getNickname().equals(nickname))
            notifyMyAreaUpdate("You placed a card");
        else
            notifyOpponentUpdate(nickname, nickname + " placed a card");
    }
    public void visibleResourcesUpdate(String nickname, Map<GameResource, Integer> visibleResources) {
        ViewPlayArea playArea = board.getPlayerArea(nickname);
        playArea.setVisibleResources(visibleResources);
        if(board.getPlayerHand().getNickname().equals(nickname))
            notifyMyAreaUpdate("");
        else
            notifyOpponentUpdate(nickname, "");
    }
    private boolean serializableCornerEqualsCorner(SerializableCorner serCorner, ViewCorner cardCorner){
        return serCorner.cardCornerId().equals(cardCorner.getCardRef().getCardID())
                && serCorner.getCornerDirection().equals(cardCorner.getDirection());
    }
    public void freeCornersUpdate(String nickname, List<SerializableCorner> freeSerializableCorners) {
        ViewPlayArea playArea = board.getPlayerArea(nickname);

        //get cardID from SerializableCorner and then find related card in cardMatrix
        for(ViewCorner corner : playArea.getFreeCorners()){
            List<SerializableCorner> duplicates = freeSerializableCorners.stream()
                    .filter(c->serializableCornerEqualsCorner(c, corner))
                    .toList();
            freeSerializableCorners.removeAll(duplicates);
        }

        List<ViewCorner> newCorners = new LinkedList<>();
        for(SerializableCorner serCorner : freeSerializableCorners) {
            try{
                Point pos = playArea.getPositionByID(serCorner.cardCornerId());
                ViewCorner corner = playArea.getCardAt(pos).getCorner(serCorner.getCornerDirection());
                newCorners.add(corner);
            }catch (IllegalArgumentException ignored){}
        }
        playArea.addFreeCorners(newCorners);

        if(board.getPlayerHand().getNickname().equals(nickname))
            notifyMyAreaUpdate("");
        else
            notifyOpponentUpdate(nickname, "");
    }
}
