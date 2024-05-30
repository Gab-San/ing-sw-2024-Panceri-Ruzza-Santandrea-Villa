package it.polimi.ingsw.network.rmi.update.playarea;

import it.polimi.ingsw.network.rmi.update.player.PlayerTypeUpdate;
import it.polimi.ingsw.view.ModelUpdater;

public class PlaceCardUpdate extends PlayerTypeUpdate {
    private final String placeCardId;
    private final boolean placeOnFront;
    private final int row;
    private final int col;
    public PlaceCardUpdate(ModelUpdater modelUpdater, String nickname, String placeCardId, int row, int col, boolean placeOnFront) {
        super(modelUpdater, nickname);
        this.placeCardId = placeCardId;
        this.placeOnFront = placeOnFront;
        this.row = row;
        this.col = col;
    }

    @Override
    public void update() {
        modelUpdater.updatePlaceCard(nickname, placeCardId, row, col, placeOnFront);
    }
}
