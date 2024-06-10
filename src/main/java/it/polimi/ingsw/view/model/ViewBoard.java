package it.polimi.ingsw.view.model;

import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.view.SceneID;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.events.DisplayEvent;
import it.polimi.ingsw.view.events.GotoEndgameEvent;
import it.polimi.ingsw.view.events.update.*;
import it.polimi.ingsw.view.gui.ChangeNotifications;
import it.polimi.ingsw.view.model.cards.ViewGoldCard;
import it.polimi.ingsw.view.model.cards.ViewObjectiveCard;
import it.polimi.ingsw.view.model.cards.ViewResourceCard;

import javax.swing.*;
import java.util.*;

/**
 * Represents the main game board. <br>
 * This class is the main access point to the ViewModel.
 */
public class ViewBoard extends JComponent {
    /**
     * Score that triggers the endgame when reached.
     */
    public static final int ENDGAME_SCORE = 20;

    /**
     * Character initial associated with the objective deck.
     */
    public static final char OBJECTIVE_DECK = 'O';
    /**
     * Character initial associated with the resource deck.
     */
    public static final char RESOURCE_DECK = 'R';
    /**
     * Character initial associated with the gold deck.
     */
    public static final char GOLD_DECK = 'G';

    private final ViewDeck<ViewResourceCard> resourceCardDeck;
    private final ViewDeck<ViewGoldCard> goldCardDeck;
    private final ViewDeck<ViewObjectiveCard> objectiveCardViewDeck;

    private final Map<String, ViewPlayArea> playerAreas;
    private final Map<String, ViewOpponentHand> opponentHands;
    private ViewPlayerHand playerHand;
    private final Map<String, Integer> scoreboard;
    private int currentTurn;
    private GamePhase gamePhase;
    private final View view;

    /**
     * Constructs the ViewBoard.
     * @param view the View this board should notify events to.
     */
    public ViewBoard(View view){
        this.view = view;
        resourceCardDeck = new ViewDeck<>(ViewBoard.RESOURCE_DECK, view);
        goldCardDeck = new ViewDeck<>(ViewBoard.GOLD_DECK, view);
        objectiveCardViewDeck = new ViewDeck<>(ViewBoard.OBJECTIVE_DECK, view);

        playerAreas = new Hashtable<>();
        opponentHands = new Hashtable<>();
        scoreboard = new Hashtable<>();
        currentTurn = 0;
        gamePhase = GamePhase.CREATE; //initialise gamePhase to the first phase
    }

    /**
     * @return a reference to the resource deck.
     */
    public ViewDeck<ViewResourceCard> getResourceCardDeck() {
        return resourceCardDeck;
    }
    /**
     * @return a reference to the gold deck.
     */
    public ViewDeck<ViewGoldCard> getGoldCardDeck() {
        return goldCardDeck;
    }
    /**
     * @return a reference to the objective deck.
     */
    public ViewDeck<ViewObjectiveCard> getObjectiveCardDeck() {
        return objectiveCardViewDeck;
    }

    /**
     * If there is no player with given nickname in this game,
     * then that player is added to the game.
     * @param nickname player's nickname
     * @return the playArea of the player with given nickname.
     */
    public synchronized ViewPlayArea getPlayerArea(String nickname) {
        if(!playerAreas.containsKey(nickname))
            addOpponent(nickname);
        return playerAreas.get(nickname);
    }

    /**
     * @return the local player's hand.
     */
    public synchronized ViewPlayerHand getPlayerHand() {
        return playerHand;
    }

    /**
     * If there is no opponent with given nickname in this game,
     * then that opponent is added to the game.
     * @param nickname the opponent nickname
     * @return the hand of the opponent with given nickname
     */
    public synchronized ViewOpponentHand getOpponentHand(String nickname){
        if(!opponentHands.containsKey(nickname))
            addOpponent(nickname);
        return opponentHands.get(nickname);
    }

    /**
     * @return list of all opponent hands ordered by turn (ascending)
     */
    public synchronized List<ViewOpponentHand> getOpponents(){
        List<ViewOpponentHand> list = new LinkedList<>(opponentHands.values());
        list.sort(Comparator.comparingInt(ViewHand::getTurn));
        return list;
    }
    /**
     * @return list of all player hands with local player at index 0
     * and opponents at subsequent indexes ordered by turn (ascending)
     */
    public synchronized List<ViewHand> getAllPlayerHands(){
        List<ViewHand> hands = new LinkedList<>();
        hands.add(playerHand);
        hands.addAll(getOpponents());
        return hands;
    }

    /**
     * @param nickname the player's nickname
     * @return the score of the player with given nickname
     */
    public synchronized int getScore(String nickname){
        return scoreboard.get(nickname);
    }

    /**
     * Sets the player's score, overriding the previous value.
     * Also notifies the scoreChange event to the Board scene.
     * @param nickname the player's nickname
     * @param score the new score
     */
    public synchronized void setScore(String nickname, int score){
        scoreboard.put(nickname, score);
        notifyView(SceneID.getBoardSceneID(), new DisplayScore(nickname, score));
    }

    /**
     * @return true if any player is above ENDGAME_SCORE,
     *        or if both deck's top cards are empty.
     */
    public synchronized boolean isEndgame(){
        int maxScore = scoreboard.values().stream().max(Integer::compare).orElse(0);
        boolean decksEmpty = resourceCardDeck.isEmpty() && goldCardDeck.isEmpty();
        return maxScore >= ENDGAME_SCORE || decksEmpty;
    }

    /**
     * @return this game's current turn.
     */
    public synchronized int getCurrentTurn() {
        return currentTurn;
    }

    /**
     * Sets this game's current turn. <br>
     * Also notifies the turn change event to the View
     * if the turn has changed with this assignment.
     * @param currentTurn the new turn
     */
    public synchronized void setCurrentTurn(int currentTurn) {
        if(this.currentTurn != currentTurn) notifyView(SceneID.getNotificationSceneID(), new DisplayTurn(currentTurn));
        this.currentTurn = currentTurn;
    }

    /**
     * @return the current gamePhase.
     */
    public synchronized GamePhase getGamePhase() {
        return gamePhase;
    }

    /**
     * Sets the current gamePhase. <br>
     * Also notifies the gamePhase change event to the View
     * if the gamePhase has changed with this assignment. <br>
     * If the new gamePhase is SHOW_WINNERS,
     * then the "go to endgame" event is triggered instead.
     * @param gamePhase the new gamePhase
     */
    public synchronized void setGamePhase(GamePhase gamePhase) {
        if(this.gamePhase != gamePhase) {
            if (gamePhase != GamePhase.SHOWWIN)
                notifyView(SceneID.getBoardSceneID(), new DisplayGamePhase(gamePhase));
            else
                notifyView(SceneID.getBoardSceneID(), new GotoEndgameEvent(this));
            // must notify GotoEndgameEvent to an existing scene (the Board scene is fine)
        }
        this.gamePhase = gamePhase;
    }

    /**
     * Adds a new opponent to the game.
     * @param nickname the opponent's nickname
     */
    public synchronized void addOpponent(String nickname){
        ViewOpponentHand opponentHand = new ViewOpponentHand(nickname, view);
        ViewPlayArea playArea =  new ViewPlayArea(nickname, this);
        playerAreas.put(nickname,playArea);
        opponentHands.put(nickname, opponentHand);
        scoreboard.put(nickname, 0);
        firePropertyChange(ChangeNotifications.ADDED_PLAYER, null, opponentHand);
        firePropertyChange(ChangeNotifications.ADDED_AREA, null, playArea);
    }

    /**
     * Resets the game status and sets the information on the local player
     * @param nickname local player's nickname
     */
    public synchronized void addLocalPlayer(String nickname){
        // Since it is launched before connect if user inputs
        // different nicknames, board will be clean if previous
        // connection attempts failed
        scoreboard.clear();
        playerAreas.clear();
        ViewPlayArea playArea;
        if(playerHand != null){
            firePropertyChange(ChangeNotifications.REMOVE_PLAYER, playerHand, null);
            playArea = getPlayerArea(playerHand.getNickname());
            firePropertyChange(ChangeNotifications.REMOVE_AREA, playArea, null);
        }
        playerHand = new ViewPlayerHand(nickname, view);
        playArea = new ViewPlayArea(nickname, this);
        scoreboard.put(nickname, 0);
        playerAreas.put(nickname, playArea);
        firePropertyChange(ChangeNotifications.ADDED_PLAYER, null, playerHand);
        playArea = getPlayerArea(playerHand.getNickname());
        firePropertyChange(ChangeNotifications.ADDED_AREA, null, playArea);
    }

    public void notifyLocalPlayerAddition(){

    }

    /**
     * Removes an opponent from the game, deleting their hand and playArea. <br>
     * Also notifies the View of the opponent removal event.
     * @param nickname the opponent's nickname
     */
    public synchronized void removeOpponent(String nickname){
        ViewOpponentHand opponentHand = opponentHands.get(nickname);
        ViewPlayArea playArea = playerAreas.get(nickname);
        playerAreas.remove(nickname);
        opponentHands.remove(nickname);
        scoreboard.remove(nickname);
        firePropertyChange(ChangeNotifications.REMOVE_PLAYER, opponentHand, null);
        firePropertyChange(ChangeNotifications.REMOVE_AREA, playArea, null);
        notifyView(SceneID.getNotificationSceneID(), new DisplayPlayerRemove(nickname));
    }

    /**
     * Notifies the view of an event being triggered on a scene
     * @param scene the scene ID of the scene concerned by the event
     * @param event the event to notify to the scene
     */
    public synchronized void notifyView(SceneID scene, DisplayEvent event){
        view.update(scene, event);
    }
}
