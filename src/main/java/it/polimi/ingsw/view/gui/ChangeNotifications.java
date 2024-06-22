package it.polimi.ingsw.view.gui;

/**
 * This class acts as a database for all the events notified to the gui.
 */
public class ChangeNotifications {
    //Players updates
    /**
     * When a player is added to the lobby.
     */
    public static final String ADDED_PLAYER = "ADDED_PLAYER";
    /**
     * When a player is removed from the lobby.
     */
    public static final String REMOVE_PLAYER = "REMOVE_PLAYER";
    /**
     * When a player's color is set.
     */
    public static final String COLOR_CHANGE = "COLOR_CHANGE";
    /**
     * When a player's connection changes.
     */
    public static final String CONNECTION_CHANGE = "CONNECTION_CHANGE";
    /**
     * When the board current turn changes.
     */
    public static final String CURRENT_TURN_UPDATE = "BOARD_TURN_UPDATE";
    /**
     * When a player's turn is set.
     */
    public static final String PLAYER_TURN_UPDATE = "PLAYER_TURN_EVENT";
    /**
     * When a player becomes deadlocked.
     */
    public static final String PLAYER_DEADLOCK_UPDATE = "PLAYER_DEADLOCK_UPDATE";
    // Area updates
    /**
     * When a play area is added on the board. (Usually called after a player addition)
     */
    public static final String ADDED_AREA = "ADDED_AREA";
    /**
     * When a play area is removed from the board. (Usually called after a player removal)
     */
    public static final String REMOVE_AREA = "REMOVE_AREA";
    /**
     * When the visible resources in a play area change.
     */
    public static final String VIS_RES_CHANGE = "VISIBLE_RESOURCES_CHANGE";
    /**
     * When a card is placed in the play area
     */
    public static final String PLACED_CARD = "PLACED_CARD";
    // Card updates
    /**
     * When the starting card is set.
     */
    public static final String SET_STARTING_CARD = "SET_STARTING_CARD";
    /**
     * When the starting card has to be cleared.
     */
    public static final String CLEAR_STARTING_CARD = "CLEAR_STARTING_CARD";
    /**
     * When a play card is added to the player's hand.
     */
    public static final String ADD_CARD_HAND = "ADD_CARD_HAND";
    /**
     * When a secret card is added to the player's hand.
     */
    public static final String ADD_SECRET_CARD = "ADD_SECRET_CARD";
    /**
     * When a secret card is chosen.
     */
    public static final String CHOSEN_OBJECTIVE_CARD = "CHOSEN_OBJECTIVE_CARD";
    /**
     * When the objectives card have to be cleared.
     */
    public static final String CLEAR_OBJECTIVES = "CLEAR_OBJECTIVES";
    /**
     * When the play cards have to be cleared.
     */
    public static final String CLEAR_PLAY_CARDS = "CLEAR_PLAY_CARDS";
    /**
     * When a play card is removed from a player's hand.
     */
    public static final String REMOVE_CARD_HAND = "REMOVE_CARD_HAND";
    /**
     * Use with oldValue=null and newValue=ViewPlaceableCard
     */
    public static final String CARD_LAYER_CHANGE = "CARD_LAYER_CHANGE";
    /**
     * When a player's score changes.
     */
    public static final String SCORE_CHANGE = "SCORE_CHANGE";
}
