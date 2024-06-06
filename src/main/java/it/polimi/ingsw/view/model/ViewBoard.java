package it.polimi.ingsw.view.model;

import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.view.events.DisplayEvent;
import it.polimi.ingsw.view.SceneID;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.model.cards.*;

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
        resourceCardDeck = new ViewDeck<>(ViewBoard.RESOURCE_DECK);
        goldCardDeck = new ViewDeck<>(ViewBoard.GOLD_DECK);
        objectiveCardViewDeck = new ViewDeck<>(ViewBoard.OBJECTIVE_DECK);

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
    public ViewPlayerHand getPlayerHand() {
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
        view.notifyBoardUpdate(nickname + "'s score is now " + score);
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
        if(changed) view.showNotification("Turn advanced to " + currentTurn);
        return changed;
    }

    public synchronized GamePhase getGamePhase() {
        return gamePhase;
    }
    public synchronized boolean setGamePhase(GamePhase gamePhase) {
        boolean changed = this.gamePhase != gamePhase;
        this.gamePhase = gamePhase;
        if(gamePhase != GamePhase.SHOWWIN)
            view.showNotification("Game phase updated to " + gamePhase);
        else
            view.setScene(SceneID.getEndgameSceneID());
        return changed;
    }

    public synchronized void addPlayer(String nickname){
        playerAreas.put(nickname, new ViewPlayArea(nickname));
        opponentHands.put(nickname, new ViewOpponentHand(nickname));
        isPlayerDeadlocked.put(nickname, false);
        scoreboard.put(nickname, 0);
    }

    public synchronized void addLocalPlayer(String nickname){
        playerHand = new ViewPlayerHand(nickname);
        scoreboard.put(nickname, 0);
        playerAreas.put(nickname, new ViewPlayArea(nickname));
        isPlayerDeadlocked.put(nickname, false);
    }
    public synchronized void removePlayer(String nickname){
        playerAreas.remove(nickname);
        opponentHands.remove(nickname);
        isPlayerDeadlocked.remove(nickname);
        scoreboard.remove(nickname);
        view.notifyView(nickname + " has been removed.");
    }
    public synchronized void setPlayerDeadlock(String nickname, boolean isDeadLocked) {
        isPlayerDeadlocked.put(nickname, isDeadLocked);
        if(isDeadLocked){
            if(getPlayerHand().getNickname().equals(nickname)){
                view.notifyMyAreaUpdate("You are deadlocked!");
            } else {
                view.notifyOpponentUpdate(nickname, nickname + " is deadlocked!");
            }
        }
    }

    //TODO maybe these methods can both be called by notifyView(Event event)
    public synchronized void notifyView(SceneID scene, DisplayEvent event){
        view.update(scene, event);
    }
}
