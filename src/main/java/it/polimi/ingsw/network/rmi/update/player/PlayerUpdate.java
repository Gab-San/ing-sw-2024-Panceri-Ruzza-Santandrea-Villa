package it.polimi.ingsw.network.rmi.update.player;

import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.view.ModelUpdater;


/**
 * This class represents a rmi update.
 * <p>
 *     Notifies about player update.
 * </p>
 */
public class PlayerUpdate extends PlayerTypeUpdate{
    private final PlayerColor color;
    private final Boolean isConnected;
    private final Integer playerTurn;

    private PlayerUpdate(ModelUpdater modelUpdater, String nickname, PlayerColor color, Boolean isConnected, Integer playerTurn) {
        super(modelUpdater, nickname);
        this.color = color;
        this.isConnected = isConnected;
        this.playerTurn = playerTurn;
    }


    /**
     * Constructs the update, when a color was bound to the player
     * @param modelUpdater instance of the model updater referenced to execute updates
     * @param nickname unique id of the player
     * @param color color bound to the player
     */
    public PlayerUpdate(ModelUpdater modelUpdater, String nickname, PlayerColor color){
        this(modelUpdater, nickname, color, null, null);
    }

    /**
     * Constructs the update, when a player connection change is detected.
     * @param modelUpdater instance of the model updater referenced to execute updates
     * @param nickname unique id of the player
     * @param isConnected true if the player is connected, false otherwise
     */
    public PlayerUpdate(ModelUpdater modelUpdater, String nickname, Boolean isConnected){
        this(modelUpdater, nickname, null, isConnected, null);
    }

    /**
     * Constructs the update, when a player turn is assigned.
     * @param modelUpdater instance of the model updater referenced to execute updates
     * @param nickname unique id of the player
     * @param playerTurn turn assigned to the player
     */
    public PlayerUpdate(ModelUpdater modelUpdater, String nickname, Integer playerTurn){
        this(modelUpdater, nickname, null, null, playerTurn);
    }


    /**
     * Updates the view notifying of a player update.
     */
    @Override
    public void update() {
        if(color != null){
            modelUpdater.updatePlayer(nickname, color);
            return;
        }
        if(isConnected != null){
            modelUpdater.updatePlayer(nickname, isConnected);
            return;
        }

        modelUpdater.updatePlayer(nickname, playerTurn);
    }
}
