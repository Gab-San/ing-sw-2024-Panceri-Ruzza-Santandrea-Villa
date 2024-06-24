package it.polimi.ingsw.view;

import it.polimi.ingsw.*;
import it.polimi.ingsw.view.events.DisplayErrorEvent;
import it.polimi.ingsw.view.events.DisplayMessageEvent;
import it.polimi.ingsw.view.events.NotifyTimeoutEvent;
import it.polimi.ingsw.view.events.state.*;
import it.polimi.ingsw.view.model.ViewBoard;
import it.polimi.ingsw.view.model.ViewOpponentHand;
import it.polimi.ingsw.view.model.ViewPlayArea;
import it.polimi.ingsw.view.model.ViewPlayerHand;
import it.polimi.ingsw.view.model.cards.*;
import it.polimi.ingsw.view.model.json.JsonImporter;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.view.tui.ConsoleTextColors.RESET;
import static it.polimi.ingsw.view.tui.ConsoleTextColors.YELLOW_TEXT;

/**
 * This class acts as the view model updater:
 * updates the lightweight representation of the server board.
 */
public class ModelUpdater {
    private final ViewBoard board;
    private final JsonImporter jsonImporter;

    /**
     * Constructs the ModelUpdater
     * @param board the ViewBoard that will receive the updates.
     */
    public ModelUpdater(ViewBoard board) {
        this.board = board;
        jsonImporter = Client.getCardJSONImporter();
    }
    /**
     * Displays chat messages.
     *
     * @param messenger message messenger
     * @param msg       message to display
     */
    public synchronized void displayMessage(String messenger, String msg){
        board.notifyView(SceneID.getNotificationSceneID(), new DisplayMessageEvent(messenger, msg));
    }
    /**
     * Reports an error occurred while evaluating player action.
     * @param errorMessage the message related to the triggered error
     */
    public synchronized void reportError(String errorMessage) {
        board.notifyView(SceneID.getNotificationSceneID(), new DisplayErrorEvent(errorMessage));
    }

    /**
     * Reports an indirect disconnection notification.
     */
    public synchronized void notifyIndirectDisconnect()  {
        System.out.println("DISCONNECTING DUE TO TIMEOUT");
        board.notifyView(SceneID.getNotificationSceneID(), new NotifyTimeoutEvent());
    }

    /**
     * Notifies about the current state of the player.
     * @param nickname the unique nickname identifier of the player
     * @param isConnected the connection status as for the moment of the displayMessage
     * @param turn the given player's turn
     * @param color the colour the player has chosen for the match
     */
    public synchronized void setPlayerState(String nickname, boolean isConnected, int turn, PlayerColor color) {
        // Setting current player check
        if (board.getPlayerHand().getNickname().equals(nickname)) {
            board.getPlayerHand().setTurn(turn);
            board.getPlayerHand().setColor(color);
            board.notifyView(SceneID.getMyAreaSceneID(), new DisplayPlayerState(nickname,
                    turn, color));
        }
        else{
            //getOpponentHand will also run addPlayer if it wasn't run before.
            ViewOpponentHand hand = board.getOpponentHand(nickname);
            hand.setConnected(isConnected);
            hand.setTurn(turn);
            hand.setColor(color);
            System.out.println("RAN DisplayPlayerState EVENT on ModelUpdater");
            board.notifyView(SceneID.getOpponentAreaSceneID(nickname), new DisplayPlayerState(nickname,
                    isConnected, turn, color));
        }
    }
    /**
     * Notifies a change in the player color.
     * @param nickname the unique nickname identifier of the player
     * @param color the colour chosen from the player for the match
     */
    public synchronized void updatePlayer(String nickname, PlayerColor color){
        if(board.getPlayerHand().getNickname().equals(nickname)) {
            board.getPlayerHand().setColor(color);
        }
        else {
            board.getOpponentHand(nickname).setColor(color);
        }
    }
    /**
     * Updates the player turn.
     * @param nickname the unique nickname identifier of the player
     * @param playerTurn the turn randomly given to the player
     */
    public synchronized void updatePlayer(String nickname, int playerTurn){
        if(board.getPlayerHand().getNickname().equals(nickname)){
            board.getPlayerHand().setTurn(playerTurn);
        }
        else{
            board.getOpponentHand(nickname).setTurn(playerTurn);
        }
    }
    /**
     * Updates the connection status of an opponent.
     * @param nickname the unique nickname identifier of the opponent
     * @param isConnected the current connection status of the opponent
     */
    public synchronized void updatePlayer(String nickname, boolean isConnected){
        // Connection needs to be displayed only for opponents
        if(!board.getPlayerHand().getNickname().equals(nickname)){
            board.getOpponentHand(nickname).setConnected(isConnected);
        }
    }
    /**
     * Notifies about the removal of an opponent.
     * @param nickname the opponent's unique nickname identifier
     */
    public synchronized void removePlayer(String nickname)  {
        board.removeOpponent(nickname);
    }
    /**
     * Updates the player's deadlock status.
     * <p>
     *     Complete with dead lock description.
     * </p>
     * @param nickname the player unique id
     * @param isDeadLocked the player deadlock value
     */
    public synchronized void playerDeadLockUpdate(String nickname, boolean isDeadLocked) {
        if(board.getPlayerHand().getNickname().equals(nickname)){
            board.getPlayerHand().setDeadlocked(isDeadLocked);
        } else {
            board.getOpponentHand(nickname).setDeadlocked(isDeadLocked);
        }
    }
    /**
     * Notifies the start of the endgame due to decks emptying.
     */
    public synchronized void notifyEndgame()  {
        board.notifyView(SceneID.getNotificationSceneID(), new DisplayEndgameEvent());
    }
    /**
     * Notifies the start of the endgame because the highlighted player has reached or surpassed
     * the score marker of 20.
     * @param nickname the unique player's nickname identifier
     * @param score the score with which he triggered the endgame
     */
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

    /**
     * Notifies about the current state of the deck.
     *<p>
     * Since all three of the cards are given it means that at least all of the
     * position of the deck have a drawable card.
     *<br>
     * It doesn't give any information about the status of the rest of the deck.
     * </p>
     * @param deck the deck identifier
     * @param topId the top card currently visible on the deck
     * @param firstId the first revealed card of the deck
     * @param secondId the second revealed card of the deck
     */
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
                        new DisplayDeckState(deck
                        ));
        }catch (ClassCastException e){
            deckCardTypeMismatch();
        }
    }
    /**
     * Notifies a change in the specified deck.
     *<p>
     * A deck has three positions to model the three visible cards of a deck: <br>
     * 0 - the top card; <br>
     * 1 - the first revealed card;<br>
     * 2 - the second revealed card. <br>
     *<br>
     * A revealed card is one of the previous, as once a card is drawn another one must be revealed.
     * </p>
     * @param deck the changed deck identifier
     * @param revealedId the identifier of the revealed card
     * @param cardPosition the card which was changed (top = 0, first = 1, second = 2)
     */
    public synchronized void deckUpdate(char deck, String revealedId, int cardPosition) {
        //FIXME: handle argument error, maybe just return (ignore wrong update?)
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
    /**
     * Notifies about the current state of the deck.
     * <p>
     * This displayMessage can be triggered iff in the deck remained just one card.
     * <br>
     * The card can be only one of the revealed ones, due to the decks' rule that obliges
     * to reveal a card after a revealed card has been drawn. This implies that cardPosition will
     * only accept values 1 or 2.
     * </p>
     *
     * @param deck the deck identifier
     * @param revealedId the only revealed card remained in the deck
     * @param cardPosition the revealed card position
     */
    public synchronized void setDeckState(char deck, String revealedId, int cardPosition){
        deckUpdate(deck, revealedId, cardPosition);
    }
    /**
     * Notifies about the current state of the identified deck.
     * <p>
     *     This displayMessage can be triggered iff the deck's face-down card pile is empty
     *     thus displaying only the two revealed cards.
     * </p>
     * @param deck the deck identifier
     * @param firstCardId the first card identifier
     * @param secondCardId the second card identifier
     */
    public synchronized void setDeckState(char deck, String firstCardId, String secondCardId) {
        setDeckState(deck, null, firstCardId, secondCardId);
    }
    /**
     * Updates the current deck's face-down pile to empty.
     * @param deck the deck identifier
     */
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
    /**
     * Updates the current revealed card's position to empty.
     * @param deck the deck identifier
     * @param position the empty revealed card's position (1 or 2)
     */
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
    /**
     * Updates the current state of a deck to an empty state.
     * <p>
     *     This displayMessage can be triggered iff a deck is empty during initialization.
     * </p>
     * @param deck the deck identifier
     */
    public synchronized void setEmptyDeckState(char deck) {
        setDeckState(deck, null,null,null);
    }
    /**
     * Notifies about the board initialization status.
     * @param currentTurn the board current turn at initialization
     * @param gamePhase the game phase at initialization
     * @param scoreboard current scoreboard state
     * @param playerDeadLock current player deadlocks
     */
    public synchronized void setBoardState(int currentTurn, Map<String, Integer> scoreboard,
                                           GamePhase gamePhase, Map<String, Boolean> playerDeadLock){
        for (String nick : playerDeadLock.keySet()) {
            playerDeadLockUpdate(nick, playerDeadLock.get(nick));
            board.setScore(nick, scoreboard.getOrDefault(nick, 0));
        }
        board.setGamePhase(gamePhase);
        board.setCurrentTurn(currentTurn);
        board.notifyView(SceneID.getBoardSceneID(), new DisplayBoardState(currentTurn, scoreboard,
                gamePhase, playerDeadLock));
    }
    /**
     * Notifies about game phase change in match.
     * @param gamePhase current game phase
     */
    public synchronized void updatePhase(GamePhase gamePhase) {
        board.setGamePhase(gamePhase);
    }
    /**
     * Updates current match turn.
     * @param currentTurn the match's current turn
     */
    public synchronized void updateTurn(int currentTurn) {
        board.setCurrentTurn(currentTurn);
    }
    /**
     * Notifies about player's score change.
     * @param nickname the unique player's identifier whose score has changed
     * @param score the player's score change
     */
    public synchronized void updateScore(String nickname, int score) {
        board.setScore(nickname, score);
    }
    /**
     * Notifies about the current player's hand status.
     * @param nickname the unique player's identifier
     * @param playCardIDs the list of IDs of playable cards in the player's hand (can be empty)
     * @param objectiveCardIDs the list of IDs of objective cards in the player's hand (can be empty)
     * @param startingCardID the ID of the starting card in the player's hand (may be null)
     */
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
            board.notifyView(SceneID.getMyAreaSceneID(), new DisplayHandState(nickname, true
            ));
        }
        else{
            ViewOpponentHand hand = board.getOpponentHand(nickname);
            hand.setCards(cardsInHand);
            hand.setSecretObjectiveCards(objectives);
            hand.setStartCard(startCard);
            board.notifyView(SceneID.getOpponentAreaSceneID(nickname), new DisplayHandState(nickname, false
            ));
        }
    }
    /**
     * Updates the current player's hand status after a card was added to it.
     * @param nickname the unique player's identifier
     * @param drawnCardId the id of the card added in the hand
     */
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
    /**
     * Updates the current player's hand status after a card was removed.
     * @param nickname the unique player's id
     * @param playCardId the played card identifier
     */
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
    /**
     * Updates the current player's hand status after an objective card was drawn.
     * @param nickname the unique player's identifier
     * @param objectiveCardID the added objective card's id
     */
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
    /**
     * Updates the current player's hand after an objective card was chosen.
     * @param nickname player's id
     * @param chosenObjectiveId the id of the chosen objective card
     */
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
    /**
     * Updates the current player's hand after the starting card was dealt.
     * @param nickname player's id
     * @param startingCardId the id of the given starting card
     */
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
    /**
     * Notifies about the player's play area status.
     * @param nickname the play area owner's id
     * @param cardPositions a list/set of {@link CardPosition} representing the play area (can be empty)
     * @param visibleResources a map of the current visible resources on the player's play area (can be empty)
     * @param freeSerializableCorners a representation of the free corners in the player's play area (can be empty)
     */
    public synchronized void setPlayAreaState(String nickname, List<CardPosition> cardPositions,
                                              Map<GameResource, Integer> visibleResources,
                                              List<SerializableCorner> freeSerializableCorners) {
        ViewPlayArea playArea = board.getPlayerArea(nickname);
        playArea.clearFreeCorners();
        playArea.clearCardMatrix();

        //then place all other cards
        for(CardPosition pos : cardPositions){
            ViewPlaceableCard card = (ViewPlaceableCard) jsonImporter.getCard(pos.cardId());
            if(card != null) {
                if(pos.isFaceUp()) card.turnFaceUp();
                else card.turnFaceDown();

                playArea.setCard(new GamePoint(pos.row(), pos.col()), card);

                pos.isCornerVisible().keySet().stream()
                    .filter(dir -> !pos.isCornerVisible().get(dir))
                    .forEach(dir -> card.getCorner(dir).cover());

                List<ViewCorner> cardFreeCorners = freeSerializableCorners.stream()
                        .filter(c -> c.cardCornerId().equals(card.getCardID()))
                        .map(SerializableCorner::getCornerDirection)
                        .map(card::getCorner)
                        .distinct()
                        .toList();
                playArea.addFreeCorners(cardFreeCorners);
            }
        }
        playArea.calculateZLayers(); //calculate zLayers for the GUI

        playArea.setVisibleResources(visibleResources);
        if(board.getPlayerHand().getNickname().equals(nickname))
            board.notifyView(SceneID.getMyAreaSceneID(),
                    new DisplayAreaState(nickname, true));
        else
            board.notifyView(SceneID.getOpponentAreaSceneID(nickname),
                    new DisplayAreaState(nickname, false));
    }
    /**
     * Notifies about card's placement.
     * <p>
     *     The parameter (0,0) is strictly associated to the starting card. <br>
     *     Therefore if this parameter is read when a non-starting card is passed that
     *     an error must occur.
     * </p>
     *
     * @param nickname     the play area owner's identifier
     * @param placedCardId the placed card's identifier
     * @param row          the row in which the card was placed
     * @param col          the column in which the card was placed
     * @param placeOnFront the side on which to place the card
     */
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
    /**
     * Updates the visible resources map.
     * @param nickname the play area owner's id
     * @param visibleResources the current visible resources in the play area
     */
    public synchronized void visibleResourcesUpdate(String nickname, Map<GameResource, Integer> visibleResources) {
        ViewPlayArea playArea = board.getPlayerArea(nickname);
        playArea.setVisibleResources(visibleResources);
    }

    /**
     * @param serCorner the SerializableCorner to compare
     * @param cardCorner the ViewCorner to compare
     * @return true if the viewCorner has the same cardRef ID and direction
     *          attributes as the SerializableCorner.
     */
    private boolean serializableCornerEqualsCorner(SerializableCorner serCorner, ViewCorner cardCorner){
        return serCorner.cardCornerId().equals(cardCorner.getCardRef().getCardID())
                && serCorner.getCornerDirection().equals(cardCorner.getDirection());
    }
    /**
     * The list of current free corners represented so that they are serializable.
     * @param nickname the play area owner's id
     * @param freeSerializableCorners the current list of free corners
     */
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
