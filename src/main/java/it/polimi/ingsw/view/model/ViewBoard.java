package it.polimi.ingsw.view.model;

import it.polimi.ingsw.GamePhase;
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
    private final ViewPlayerHand playerHand;
    private final Map<String, Boolean> isPlayerDeadlocked;
    private final Map<String, Integer> scoreboard;

    private int currentTurn;
    private GamePhase gamePhase;

    public ViewBoard(String nickname){
        resourceCardDeck = new ViewDeck<>();
        goldCardDeck = new ViewDeck<>();
        objectiveCardViewDeck = new ViewDeck<>();

        playerAreas = new Hashtable<>();
        opponentHands = new Hashtable<>();
        playerHand = new ViewPlayerHand(nickname);
        isPlayerDeadlocked = new Hashtable<>();
        scoreboard = new Hashtable<>();
        scoreboard.put(nickname, 0);

        playerAreas.put(nickname, new ViewPlayArea());
        isPlayerDeadlocked.put(nickname, false);
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
        return playerAreas.get(nickname);
    }
    public synchronized boolean isPlayerDeadlocked(String nickname) {
        return isPlayerDeadlocked.get(nickname);
    }
    public ViewPlayerHand getPlayerHand() {
        return playerHand;
    }
    public synchronized ViewOpponentHand getOpponentHand(String nickname){
        return opponentHands.get(nickname);
    }
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
    }
    public synchronized boolean isEndgame(){
        int maxScore = scoreboard.values().stream().max(Integer::compare).orElse(0);
        return maxScore >= ENDGAME_SCORE;
    }

    public synchronized int getCurrentTurn() {
        return currentTurn;
    }
    public synchronized void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
    }

    public synchronized GamePhase getGamePhase() {
        return gamePhase;
    }
    public synchronized void setGamePhase(GamePhase gamePhase) {
        this.gamePhase = gamePhase;
    }

    public synchronized void addPlayer(String nickname){
        playerAreas.put(nickname, new ViewPlayArea());
        opponentHands.put(nickname, new ViewOpponentHand(nickname));
        isPlayerDeadlocked.put(nickname, false);
        scoreboard.put(nickname, 0);
    }
    public synchronized void removePlayer(String nickname){
        playerAreas.remove(nickname);
        opponentHands.remove(nickname);
        isPlayerDeadlocked.remove(nickname);
        scoreboard.remove(nickname);
    }
    public synchronized void setPlayerDeadlock(String nickname, boolean isDeadLocked) {
        isPlayerDeadlocked.put(nickname, isDeadLocked);
    }
}
