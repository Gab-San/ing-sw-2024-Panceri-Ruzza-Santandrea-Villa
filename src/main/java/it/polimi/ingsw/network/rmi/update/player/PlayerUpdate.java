package it.polimi.ingsw.network.rmi.update.player;

import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.view.ModelUpdater;

public class PlayerUpdate extends PlayerTypeUpdate{
    private final PlayerColor color;
    private final Boolean isConnected;
    private final Integer playerTurn;
    protected PlayerUpdate(ModelUpdater modelUpdater, String nickname, PlayerColor color, Boolean isConnected, Integer playerTurn) {
        super(modelUpdater, nickname);
        this.color = color;
        this.isConnected = isConnected;
        this.playerTurn = playerTurn;
    }


    public PlayerUpdate(ModelUpdater modelUpdater, String nickname, PlayerColor color){
        this(modelUpdater, nickname, color, null, null);
    }
    public PlayerUpdate(ModelUpdater modelUpdater, String nickname, Boolean isConnected){
        this(modelUpdater, nickname, null, isConnected, null);
    }
    public PlayerUpdate(ModelUpdater modelUpdater, String nickname, Integer playerTurn){
        this(modelUpdater, nickname, null, null, playerTurn);
    }



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
