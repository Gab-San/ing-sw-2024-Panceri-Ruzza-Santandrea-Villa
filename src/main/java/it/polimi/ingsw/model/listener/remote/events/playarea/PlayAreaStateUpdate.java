package it.polimi.ingsw.model.listener.remote.events.playarea;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.Point;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.PlaceableCard;
import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.model.listener.remote.events.player.PlayerEvent;
import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;
import java.util.*;

public class PlayAreaStateUpdate extends PlayerEvent {
    private final List<CardPosition> cardPositions;
    private final Map<GameResource, Integer> visibleResources;
    private final List<SerializableCorner> freeSerializableCorners;
    public PlayAreaStateUpdate(String nickname, Map<Point, PlaceableCard> cardMap, Map<GameResource, Integer> visibleResources, List<Corner> freeCorners) {
        super(nickname);
        this.cardPositions = new LinkedList<>();
        for(Point pos : cardMap.keySet()){
            PlaceableCard card = cardMap.get(pos);
            Map<CornerDirection, Boolean> cornerVisibility = new Hashtable<>();
            for(CornerDirection dir : CornerDirection.values()){
                cornerVisibility.put(dir, card.getCorner(dir).isVisible());
            }
            cardPositions.add(new CardPosition(pos.row(), pos.col(),
                    card.getCardID(), card.isFaceUp(), cornerVisibility));
        }

        this.visibleResources = visibleResources;
        this.freeSerializableCorners = new LinkedList<>();
        freeCorners.forEach(
                (corner) -> freeSerializableCorners.add(
                        new SerializableCorner(
                                corner.getCardRef().getCardID(),
                                corner.getDirection().toString()
                        )
                )
        );
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.setPlayAreaState(nickname, cardPositions, visibleResources, freeSerializableCorners);
    }

    @Override
    public String toString() {
        return "PlayAreaStateUpdate{" +
                "cardPositions=" + cardPositions +
                ", visibleResources=" + visibleResources +
                ", freeSerializableCorners=" + freeSerializableCorners +
                '}';
    }
}
