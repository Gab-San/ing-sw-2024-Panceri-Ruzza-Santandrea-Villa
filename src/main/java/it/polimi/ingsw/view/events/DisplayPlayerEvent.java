package it.polimi.ingsw.view.events;

/**
 * This class represents an event dealing with a player
 */
abstract public class DisplayPlayerEvent implements TUIEvent, GUIEvent{
    /**
     * Player's nickname (unique identifier)
     */
    protected final String nickname;
    protected final boolean isLocalPlayer;

    /**
     * Constructs player event.
     * @param nickname player's nickname who caused event to be triggered
     * @param isLocalPlayer true if the event was triggered due to local player action, false otherwise.
     */
    protected DisplayPlayerEvent(String nickname, boolean isLocalPlayer) {
        this.nickname = nickname;
        this.isLocalPlayer = isLocalPlayer;
    }

}
