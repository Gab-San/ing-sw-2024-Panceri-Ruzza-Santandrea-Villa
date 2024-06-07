package it.polimi.ingsw.view.model;

import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.view.SceneID;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.events.DisplayEvent;
import it.polimi.ingsw.view.events.GotoEndgameEvent;
import it.polimi.ingsw.view.events.update.*;
import it.polimi.ingsw.view.model.cards.ViewGoldCard;
import it.polimi.ingsw.view.model.cards.ViewObjectiveCard;
import it.polimi.ingsw.view.model.cards.ViewResourceCard;

import java.util.*;

public class ViewBoard {
    public static final int ENDGAME_SCORE = 20;
    public static final int MAX_PLAYERS = 4;

    public static final char OBJECTIVE_DECK = 'O';
    public static final char RESOURCE_DECK = 'R';
    public static final char GOLD_DECK = 'G';

    private final ViewDeck<ViewResourceCard> resourceCardDeck;
    private final ViewDeck<ViewGoldCard> goldCardDeck;
    private final ViewDeck<ViewObjectiveCard> objectiveCardViewDeck;

    private final Map<String, ViewPlayArea> playerAreas;
    private final Map<String, ViewOpponentHand> opponentHands;
    private ViewPlayerHand playerHand;
    private final Map<String, Boolean> isPlayerDeadlocked;
    private final Map<String, Integer> scoreboard;

    private int currentTurn;
    private GamePhase gamePhase;
    private final View view;

    public ViewBoard(View view){
        this.view = view;
        resourceCardDeck = new ViewDeck<>(ViewBoard.RESOURCE_DECK, view);
        goldCardDeck = new ViewDeck<>(ViewBoard.GOLD_DECK, view);
        objectiveCardViewDeck = new ViewDeck<>(ViewBoard.OBJECTIVE_DECK, view);

        playerAreas = new Hashtable<>();
        opponentHands = new Hashtable<>();
        isPlayerDeadlocked = new Hashtable<>();
        scoreboard = new Hashtable<>();
        currentTurn = 0;
        gamePhase = GamePhase.PLACECARD;

    }

    public ViewDeck<ViewResourceCard> getResourceCardDeck() {
        return resourceCardDeck;
    }
    public ViewDeck<ViewGoldCard> getGoldCardDeck() {
        return goldCardDeck;
    }
    public ViewDeck<ViewObjectiveCard> getObjectiveCardDeck() {
        return objectiveCardViewDeck;
    }

    public synchronized ViewPlayArea getPlayerArea(String nickname) {
        if(!playerAreas.containsKey(nickname))
            addPlayer(nickname);
        return playerAreas.get(nickname);
    }

    public synchronized boolean isPlayerDeadlocked(String nickname) {
        return isPlayerDeadlocked.getOrDefault(nickname, false);
    }
    public synchronized   ViewPlayerHand getPlayerHand() {
        return playerHand;
    }
    public synchronized ViewOpponentHand getOpponentHand(String nickname){
        if(!opponentHands.containsKey(nickname))
            addPlayer(nickname);
        return opponentHands.get(nickname);
    }

    /**
     * @return list of all OpponentHands ordered by turn (ascending)
     */
    public synchronized List<ViewOpponentHand> getOpponents(){
        List<ViewOpponentHand> list = new LinkedList<>(opponentHands.values());
        list.sort(Comparator.comparingInt(ViewHand::getTurn));
        return list;
    }
    public synchronized List<ViewHand> getAllPlayerHands(){
        List<ViewHand> hands = new LinkedList<>();
        hands.add(playerHand);
        hands.addAll(getOpponents());
        return hands;
    }

    public synchronized int getScore(String nickname){
        return scoreboard.get(nickname);
    }
    public synchronized void setScore(String nickname, int score){
        scoreboard.put(nickname, score);
        notifyView(SceneID.getBoardSceneID(), new DisplayScore(nickname, score));
    }
    public synchronized boolean isEndgame(){
        int maxScore = scoreboard.values().stream().max(Integer::compare).orElse(0);
        boolean decksEmpty = resourceCardDeck.isEmpty() && goldCardDeck.isEmpty();
        return maxScore >= ENDGAME_SCORE || decksEmpty;
    }

    public synchronized int getCurrentTurn() {
        return currentTurn;
    }
    public synchronized boolean setCurrentTurn(int currentTurn) {
        boolean changed = this.currentTurn != currentTurn;
        this.currentTurn = currentTurn;
        //TODO check if 'if' is correct
        if(changed) notifyView(SceneID.getNotificationSceneID(), new DisplayTurn(currentTurn));
        return changed;
    }

    public synchronized GamePhase getGamePhase() {
        return gamePhase;
    }
    public synchronized boolean setGamePhase(GamePhase gamePhase) {
        boolean changed = this.gamePhase != gamePhase;
        this.gamePhase = gamePhase;
        if(changed) {
            if (gamePhase != GamePhase.SHOWWIN)
                notifyView(SceneID.getNotificationSceneID(), new DisplayGamePhase(gamePhase));
            else
                notifyView(SceneID.getEndgameSceneID(), new GotoEndgameEvent());
        }
        return changed;
    }

    public synchronized void addPlayer(String nickname){
        playerAreas.put(nickname, new ViewPlayArea(nickname, this));
        opponentHands.put(nickname, new ViewOpponentHand(nickname, view));
        isPlayerDeadlocked.put(nickname, false);
        scoreboard.put(nickname, 0);
    }

    public synchronized void addLocalPlayer(String nickname){
        // Since it is launched before connect if user inputs
        // different nicknames, board will be clean if previous
        // connection attempts failed
        scoreboard.clear();
        playerAreas.clear();
        isPlayerDeadlocked.clear();
        playerHand = new ViewPlayerHand(nickname, view);
        scoreboard.put(nickname, 0);
        playerAreas.put(nickname, new ViewPlayArea(nickname, this));
        isPlayerDeadlocked.put(nickname, false);
    }
    public synchronized void removePlayer(String nickname){
        playerAreas.remove(nickname);
        opponentHands.remove(nickname);
        isPlayerDeadlocked.remove(nickname);
        scoreboard.remove(nickname);
        notifyView(SceneID.getNotificationSceneID(), new DisplayPlayerRemove(nickname));
    }

    public synchronized void setPlayerDeadlock(String nickname, boolean isDeadLocked) {
        isPlayerDeadlocked.put(nickname, isDeadLocked);
        if(isDeadLocked){
            if(getPlayerHand().getNickname().equals(nickname)){
                //COMMENT FOR "ALE": this notification should be displayed even if
                // player is looking at itself. Are we displaying dead locks in some other way?
                notifyView(SceneID.getMyAreaSceneID(), new DisplayDeadLock(nickname, true));
            } else {
                notifyView(SceneID.getOpponentAreaSceneID(nickname), new DisplayDeadLock(nickname, false));
            }
        }
    }

    public synchronized void notifyView(SceneID scene, DisplayEvent event){
        view.update(scene, event);
    }
}
