package it.polimi.ingsw.view.model;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.view.model.cards.*;

import java.util.Hashtable;
import java.util.Map;

public class ViewBoard {
    public static final int ENDGAME_SCORE = Board.ENDGAME_SCORE;
    public static final int MAX_PLAYERS = Board.MAX_PLAYERS;

    public static final char STARTING_DECK = Board.STARTING_DECK;
    public static final char OBJECTIVE_DECK = Board.OBJECTIVE_DECK;
    public static final char RESOURCE_DECK = Board.RESOURCE_DECK;
    public static final char GOLD_DECK = Board.GOLD_DECK;

    private final ViewDeck<ViewResourceCard> resourceCardDeck;
    private final ViewDeck<ViewGoldCard> goldCardDeck;
    private final ViewDeck<ViewObjectiveCard> objectiveCardViewDeck;

    private final Map<String, ViewPlayArea> playerAreas;
    private final ViewPlayerHand playerHand;
    private final Map<String, Boolean> isPlayerDeadlocked;

    private int currentTurn;
    private GamePhase gamePhase;

    public ViewBoard(String nickname){
        resourceCardDeck = new ViewDeck<>();
        goldCardDeck = new ViewDeck<>();
        objectiveCardViewDeck = new ViewDeck<>();

        playerAreas = new Hashtable<>();
        playerHand = new ViewPlayerHand(nickname);
        isPlayerDeadlocked = new Hashtable<>();

        addPlayer(nickname); // adds the player of this client
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

    public Map<String, ViewPlayArea> getPlayerAreas() {
        return playerAreas;
    }
    public Map<String, Boolean> getPlayerDeadlocks() {
        return isPlayerDeadlocked;
    }
    public ViewPlayerHand getPlayerHand() {
        return playerHand;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }
    public void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
    }

    public GamePhase getGamePhase() {
        return gamePhase;
    }
    public void setGamePhase(GamePhase gamePhase) {
        this.gamePhase = gamePhase;
    }

    public void addPlayer(String nickname){
        playerAreas.put(nickname, new ViewPlayArea());
        isPlayerDeadlocked.put(nickname, false);
    }
    public void removePlayer(String nickname){
        playerAreas.remove(nickname);
        isPlayerDeadlocked.remove(nickname);
    }
    public void disconnectPlayer(String nickname){
        playerAreas.get(nickname).setConnected(false);
    }
    public void reconnectPlayer(String nickname){
        playerAreas.get(nickname).setConnected(true);
    }
}
