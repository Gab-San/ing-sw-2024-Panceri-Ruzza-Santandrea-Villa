package it.polimi.ingsw.view.gui;


public class ChangeNotifications {
    // Player updates
    public static final String ADDED_PLAYER = "ADDED_PLAYER";

    public static final String REMOVE_PLAYER = "REMOVE_PLAYER";

    public static final String COLOR_CHANGE = "COLOR_CHANGE";
    public static final String CONNECTION_CHANGE = "CONNECTION_CHANGE";
    public static final String CURRENT_TURN_UPDATE = "BOARD_TURN_UPDATE";

    public static final String PLAYER_TURN_UPDATE = "PLAYER_TURN_EVENT";

    public static final String PLAYER_DEADLOCK_UPDATE = "PLAYER_DEADLOCK_UPDATE";
    // Area updates

    public static final String ADDED_AREA = "ADDED_AREA";
    public static final String REMOVE_AREA = "REMOVE_AREA";
    public static final String VIS_RES_CHANGE = "VISIBLE_RESOURCES_CHANGE";
    public static final String PLACED_CARD = "PLACED_CARD";
    // Card updates
    public static final String SET_STARTING_CARD = "SET_STARTING_CARD";
    public static final String CLEAR_STARTING_CARD = "CLEAR_STARTING_CARD";
    public static final String ADD_CARD_HAND = "ADD_CARD_HAND";
    public static final String ADD_SECRET_CARD = "ADD_SECRET_CARD";
    public static final String CHOSEN_OBJECTIVE_CARD = "CHOSEN_OBJECTIVE_CARD";
    public static final String CLEAR_OBJECTIVES = "CLEAR_OBJECTIVES";
    public static final String CLEAR_PLAY_CARDS = "CLEAR_PLAY_CARDS";
    public static final String REMOVE_CARD_HAND = "REMOVE_CARD_HAND";
    /**
     * Use with oldValue=null and newValue=ViewPlaceableCard
     */
    public static final String CARD_LAYER_CHANGE = "CARD_LAYER_CHANGE";
    public static final String SCORE_CHANGE = "SCORE_CHANGE";
}
