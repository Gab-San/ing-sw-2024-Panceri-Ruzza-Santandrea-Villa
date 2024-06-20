package it.polimi.ingsw.model.listener.remote.events.playarea;

import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.model.cards.PlaceableCard;
import it.polimi.ingsw.model.listener.remote.events.player.PlayerEvent;
import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

/**
 * This class represents a play area event. Notifies about card placement.
 */
public class PlayAreaPlacedCardEvent extends PlayerEvent {
    private final String placedCardId;
    private final boolean placeOnFront;
    private final int row;
    private final int col;

    /**
     * Constructs the placed card event.
     * @param nickname player area owner's id
     * @param card placed card
     * @param cardPosition placed card position
     */
    public PlayAreaPlacedCardEvent(String nickname, PlaceableCard card, GamePoint cardPosition) {
        super(nickname);
        this.placedCardId = card.getCardID();
        this.placeOnFront = card.isFaceUp();
        this.row = cardPosition.row();
        this.col = cardPosition.col();
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.updatePlaceCard(nickname, placedCardId, row, col, placeOnFront);
    }
}
