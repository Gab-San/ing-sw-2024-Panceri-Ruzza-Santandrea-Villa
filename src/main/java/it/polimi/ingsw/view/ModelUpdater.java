package it.polimi.ingsw.view;

import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.CardPosition;
import it.polimi.ingsw.SerializableCorner;
import it.polimi.ingsw.view.events.*;
import it.polimi.ingsw.view.events.state.*;
import it.polimi.ingsw.view.model.*;
import it.polimi.ingsw.view.model.cards.*;
import it.polimi.ingsw.view.model.json.JsonImporter;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.view.tui.ConsoleTextColors.RESET;
import static it.polimi.ingsw.view.tui.ConsoleTextColors.YELLOW_TEXT;

//FIXME Check that all ifs before board notification are correct and
// comment them
//DOCS: COMMENT ALL NEW EVENTS PLUS NEW SCENE
public class ModelUpdater {
    private final ViewBoard board;
    private final JsonImporter jsonImporter;

    public ModelUpdater(ViewBoard board) {
        this.board = board;
        jsonImporter = Client.getCardJSONImporter();
    }


    public synchronized void displayMessage(String messenger, String msg){
        board.notifyView(SceneID.getNotificationSceneID(), new DisplayMessageEvent(messenger, msg));
    }

    public synchronized void reportError(String errorMessage) {
        board.notifyView(SceneID.getNotificationSceneID(), new DisplayErrorEvent(errorMessage));
    }

    public synchronized void notifyIndirectDisconnect()  {
        board.notifyView(SceneID.getNotificationSceneID(), new NotifyTimeoutEvent());
    }

    public synchronized void setPlayerState(String nickname, boolean isConnected, int turn, PlayerColor color) {
        // Setting current player check
        if (board.getPlayerHand().getNickname().equals(nickname)) {
            board.getPlayerHand().setTurn(turn);
            board.getPlayerHand().setColor(color);
            if(turn != 0 || color != null)
                board.notifyView(SceneID.getMyAreaSceneID(), new DisplayPlayerState(nickname,
                        turn, color));
        }
        else{
            //getOpponentHand will also run addPlayer if it wasn't run before.
            ViewOpponentHand hand = board.getOpponentHand(nickname);
            hand.setConnected(isConnected);
            hand.setTurn(turn);
            hand.setColor(color);
            board.notifyView(SceneID.getOpponentAreaSceneID(nickname), new DisplayPlayerState(nickname,
                    isConnected, turn, color));
        }
    }
    public synchronized void updatePlayer(String nickname, PlayerColor color){
        if(board.getPlayerHand().getNickname().equals(nickname)) {
            board.getPlayerHand().setColor(color);
        }
        else {
            board.getOpponentHand(nickname).setColor(color);
        }
    }
    public synchronized void updatePlayer(String nickname, int playerTurn){
        if(board.getPlayerHand().getNickname().equals(nickname)){
            board.getPlayerHand().setTurn(playerTurn);
        }
        else{
            board.getOpponentHand(nickname).setTurn(playerTurn);
        }
    }
    public synchronized void updatePlayer(String nickname, boolean isConnected){
        // Connection needs to be displayed only for opponents
        if(!board.getPlayerHand().getNickname().equals(nickname)){
            board.getOpponentHand(nickname).setConnected(isConnected);
        }
    }

    public synchronized void removePlayer(String nickname)  {
        board.removePlayer(nickname);
    }
    public synchronized void playerDeadLockUpdate(String nickname, boolean isDeadLocked) {
        if(board.getPlayerHand().getNickname().equals(nickname)){
            board.getPlayerHand().setDeadlocked(isDeadLocked);
        } else {
            board.getOpponentHand(nickname).setDeadlocked(isDeadLocked);
        }
    }

    public synchronized void notifyEndgame()  {
        board.notifyView(SceneID.getNotificationSceneID(), new DisplayEndgameEvent());
    }

    public synchronized void notifyEndgame(String nickname, int score)  {
        board.notifyView(SceneID.getNotificationSceneID(), new DisplayEndgameEvent(nickname, score));
    }

    //TODO: [Ale] remove these debug prints (and maybe remove exception too)
    private void deckCardTypeMismatch(){
        System.out.println(YELLOW_TEXT + "CLEARLY AN ERROR." + RESET);
        System.out.println("|".repeat(500));
        throw new IllegalArgumentException("Deck type and Card type mismatch");
    }
    private void illegalArgument(){
        System.out.println(YELLOW_TEXT + "CLEARLY AN ARGUMENT ERROR." + RESET);
        System.out.println("|".repeat(500));
        throw new IllegalArgumentException("Illegal argument passed.");
    }

    public synchronized void setDeckState(char deck, String topId, String firstId, String secondId) {
        try {
            ViewCard topCard = jsonImporter.getCard(topId);
            ViewCard firstRevealed = jsonImporter.getCard(firstId);
            if(firstRevealed != null)
                firstRevealed.turnFaceUp();
            ViewCard secondRevealed = jsonImporter.getCard(secondId);
            if(secondRevealed != null)
                secondRevealed.turnFaceUp();
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
            }
            if(topCard != null)
                board.notifyView(SceneID.getBoardSceneID(),
                        new DisplayDeckState(deck, topCard,
                                firstRevealed, secondRevealed));
        }catch (ClassCastException e){
            deckCardTypeMismatch();
        }
    }
    public synchronized void deckUpdate(char deck, String revealedId, int cardPosition) {
        //TODO: handle argument error, maybe just return (ignore wrong update?)
        if(!Character.toString(deck).toUpperCase().matches("[RGO]")) illegalArgument();

        ViewCard card = jsonImporter.getCard(revealedId);
        try {
            switch (deck) {
                case ViewBoard.RESOURCE_DECK:
                    switch (cardPosition){
                        case 0:
                            board.getResourceCardDeck().setTopCard((ViewResourceCard) card);
                            break;
                        case 1:
                            board.getResourceCardDeck().setFirstRevealed((ViewResourceCard) card);
                            break;
                        case 2:
                            board.getResourceCardDeck().setSecondRevealed((ViewResourceCard) card);
                            break;
                    }
                    break;
                case ViewBoard.GOLD_DECK:
                    switch (cardPosition){
                        case 0:
                            board.getGoldCardDeck().setTopCard((ViewGoldCard) card);
                            break;
                        case 1:
                            board.getGoldCardDeck().setFirstRevealed((ViewGoldCard) card);
                            break;
                        case 2:
                            board.getGoldCardDeck().setSecondRevealed((ViewGoldCard) card);
                            break;
                    }
                    break;
                case ViewBoard.OBJECTIVE_DECK:
                    switch (cardPosition){
                        case 0:
                            board.getObjectiveCardDeck().setTopCard((ViewObjectiveCard) card);
                            break;
                        case 1:
                            board.getObjectiveCardDeck().setFirstRevealed((ViewObjectiveCard) card);
                            break;
                        case 2:
                            board.getObjectiveCardDeck().setSecondRevealed((ViewObjectiveCard) card);
                            break;
                    }
                    break;
            }
        }catch (ClassCastException e){
            deckCardTypeMismatch();
        }
    }
    public synchronized void setDeckState(char deck, String revealedId, int cardPosition){
        deckUpdate(deck, revealedId, cardPosition);
    }
    public synchronized void setDeckState(char deck, String firstCardId, String secondCardId) {
        setDeckState(deck, null, firstCardId, secondCardId);
    }
    public synchronized void emptyFaceDownPile(char deck){
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
        }
    }

    public synchronized void emptyReveal(char deck, int position){
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
        }
    }
    public synchronized void setEmptyDeckState(char deck) {
        setDeckState(deck, null,null,null);
    }

    public synchronized void setBoardState(int currentTurn, Map<String, Integer> scoreboard,
                                           GamePhase gamePhase, Map<String, Boolean> playerDeadLock){
        for (String nick : playerDeadLock.keySet()) {
            playerDeadLockUpdate(nick, playerDeadLock.get(nick));
            board.setScore(nick, scoreboard.getOrDefault(nick, 0));
        }
        boolean phaseChanged = board.setGamePhase(gamePhase);
        boolean turnChanged = board.setCurrentTurn(currentTurn);
//        if (phaseChanged || turnChanged)
//            board.notifyView(SceneID.getBoardSceneID(), new DisplayBoardState(currentTurn, scoreboard, gamePhase,
//                    playerDeadLock));
    }
    public synchronized void updatePhase(GamePhase gamePhase) {
        board.setGamePhase(gamePhase);
    }
    public synchronized void updateTurn(int currentTurn) {
        board.setCurrentTurn(currentTurn);
    }
    public synchronized void updateScore(String nickname, int score) {
        board.setScore(nickname, score);
    }

    public synchronized void setPlayerHandState(String nickname, List<String> playCardIDs,
                                                List<String> objectiveCardIDs, String startingCardID) {
        List<ViewPlayCard> cardsInHand = jsonImporter.getPlayCards(playCardIDs);
        List<ViewObjectiveCard> objectives = jsonImporter.getObjectiveCards(objectiveCardIDs);
        ViewStartCard startCard = jsonImporter.getStartCard(startingCardID);

        if (board.getPlayerHand().getNickname().equals(nickname)) {
            ViewPlayerHand hand = board.getPlayerHand();
            hand.setCards(cardsInHand);
            hand.setSecretObjectiveCards(objectives);
            hand.setStartCard(startCard);
            board.notifyView(SceneID.getMyAreaSceneID(), new DisplayHandState(nickname, true,
                    cardsInHand, objectives, startCard));
        }
        else{
            ViewOpponentHand hand = board.getOpponentHand(nickname);
            hand.setCards(cardsInHand);
            hand.setSecretObjectiveCards(objectives);
            hand.setStartCard(startCard);
            board.notifyView(SceneID.getOpponentAreaSceneID(nickname), new DisplayHandState(nickname, false,
                    cardsInHand, objectives, startCard));
        }
    }
    public synchronized void playerHandAddedCardUpdate(String nickname, String drawnCardId) {
        ViewPlayCard drawnCard = jsonImporter.getPlayCard(drawnCardId);

        if (board.getPlayerHand().getNickname().equals(nickname)) {
            ViewPlayerHand hand = board.getPlayerHand();
            hand.addCard(drawnCard);
        }
        else{
            ViewOpponentHand hand = board.getOpponentHand(nickname);
            hand.addCard(drawnCard);
        }
    }
    public synchronized void playerHandRemoveCard(String nickname, String playCardId) {
        ViewPlayCard card = jsonImporter.getPlayCard(playCardId);

        if (board.getPlayerHand().getNickname().equals(nickname)) {
            ViewPlayerHand hand = board.getPlayerHand();
            hand.removeCard(card);
        }
        else{
            ViewOpponentHand hand = board.getOpponentHand(nickname);
            hand.removeCard(card);
        }
    }

    public synchronized void playerHandAddObjective(String nickname, String objectiveCardID) {
        ViewObjectiveCard objective = jsonImporter.getObjectiveCard(objectiveCardID);

        if (board.getPlayerHand().getNickname().equals(nickname)) {
            ViewPlayerHand hand = board.getPlayerHand();
            hand.addSecretObjectiveCard(objective);
        }
        else{
            ViewOpponentHand hand = board.getOpponentHand(nickname);
            hand.addSecretObjectiveCard(objective);
        }
    }
    public synchronized void playerHandChooseObject(String nickname, String chosenObjectiveId) {
        if (board.getPlayerHand().getNickname().equals(nickname)) {
            ViewPlayerHand hand = board.getPlayerHand();
            hand.chooseObjective(chosenObjectiveId);
        }
        else{
            ViewOpponentHand hand = board.getOpponentHand(nickname);
            hand.chooseObjective(chosenObjectiveId);
        }
    }
    public synchronized void playerHandSetStartingCard(String nickname, String startingCardId) {
        ViewStartCard startCard = jsonImporter.getStartCard(startingCardId);

        if (board.getPlayerHand().getNickname().equals(nickname)) {
            ViewPlayerHand hand = board.getPlayerHand();
            hand.setStartCard(startCard);
        }
        else{
            ViewOpponentHand hand = board.getOpponentHand(nickname);
            hand.setStartCard(startCard);
        }
    }

    public synchronized void setPlayAreaState(String nickname, List<CardPosition> cardPositions,
                                              Map<GameResource, Integer> visibleResources,
                                              List<SerializableCorner> freeSerializableCorners) {
        ViewPlayArea playArea = board.getPlayerArea(nickname);
        playArea.clearFreeCorners();
        playArea.clearCardMatrix();

        for(CardPosition pos : cardPositions){
            ViewPlaceableCard card = (ViewPlaceableCard) jsonImporter.getCard(pos.cardId());
            if(card != null) {
                if(pos.isFaceUp()) card.turnFaceUp();
                else card.turnFaceDown();

                pos.isCornerVisible().keySet().stream()
                    .filter(dir -> !pos.isCornerVisible().get(dir))
                    .forEach(dir -> card.getCorner(dir).cover());

                playArea.setCard(new GamePoint(pos.row(), pos.col()), card);
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
            board.notifyView(SceneID.getMyAreaSceneID(),
                    new DisplayAreaState(nickname, true,
                            playArea.getCardMatrix(), playArea.getVisibleResources(), playArea.getFreeCorners()));
        else
            board.notifyView(SceneID.getOpponentAreaSceneID(nickname),
                    new DisplayAreaState(nickname, false,
                            playArea.getCardMatrix(), playArea.getVisibleResources(), playArea.getFreeCorners()));
    }
    public synchronized void updatePlaceCard(String nickname, String placedCardId, int row, int col, boolean placeOnFront) {
        ViewPlayArea playArea = board.getPlayerArea(nickname);
        ViewPlaceableCard card = (ViewPlaceableCard) jsonImporter.getCard(placedCardId);
        if(card != null) {
            if (placeOnFront)
                card.turnFaceUp();
            else
                card.turnFaceDown();

            playArea.placeCard(new GamePoint(row, col), card);
        }
    }
    public synchronized void visibleResourcesUpdate(String nickname, Map<GameResource, Integer> visibleResources) {
        ViewPlayArea playArea = board.getPlayerArea(nickname);
        playArea.setVisibleResources(visibleResources);

    }
    private boolean serializableCornerEqualsCorner(SerializableCorner serCorner, ViewCorner cardCorner){
        return serCorner.cardCornerId().equals(cardCorner.getCardRef().getCardID())
                && serCorner.getCornerDirection().equals(cardCorner.getDirection());
    }
    public synchronized void freeCornersUpdate(String nickname, List<SerializableCorner> freeSerializableCorners) {
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
                GamePoint pos = playArea.getPositionByID(serCorner.cardCornerId());
                ViewCorner corner = playArea.getCardAt(pos).getCorner(serCorner.getCornerDirection());
                newCorners.add(corner);
            }catch (IllegalArgumentException ignored){}
        }
        playArea.addFreeCorners(newCorners);
    }
}
