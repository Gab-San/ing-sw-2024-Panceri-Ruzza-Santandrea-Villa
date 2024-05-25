package it.polimi.ingsw.model.listener.remote.events.playarea;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.model.cards.PlaceableCard;
import it.polimi.ingsw.model.listener.remote.events.player.PlayerEvent;
import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

public class PlayAreaPlacedCardEvent extends PlayerEvent {
    private final String placedCardId;
    private final int row;
    private final int col;

    public PlayAreaPlacedCardEvent(String nickname, PlaceableCard card, Point cardPosition) {
        super(nickname);
        this.placedCardId = card.getCardID();
        this.row = cardPosition.row();
        this.col = cardPosition.col();
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.updatePlaceCard(nickname, placedCardId, row, col);
    }
}
