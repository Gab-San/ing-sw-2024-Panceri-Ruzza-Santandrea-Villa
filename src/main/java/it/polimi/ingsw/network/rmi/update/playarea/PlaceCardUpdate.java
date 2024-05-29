package it.polimi.ingsw.network.rmi.update.playarea;

import it.polimi.ingsw.network.rmi.update.player.PlayerTypeUpdate;
import it.polimi.ingsw.view.ModelUpdater;

public class PlaceCardUpdate extends PlayerTypeUpdate {
    private final String placeCardId;
    private final int row;
    private final int col;
    public PlaceCardUpdate(ModelUpdater modelUpdater, String nickname, String placeCardId, int row, int col) {
        super(modelUpdater, nickname);
        this.placeCardId = placeCardId;
        this.row = row;
        this.col = col;
    }

    @Override
    public void update() {
        modelUpdater.updatePlaceCard(nickname, placeCardId, row, col);
    }
}
