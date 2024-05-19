package it.polimi.ingsw.model.listener.remote.events.playarea;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.PlaceableCard;
import it.polimi.ingsw.model.enums.GameResource;
import it.polimi.ingsw.model.listener.remote.events.player.PlayerEvent;
import it.polimi.ingsw.server.VirtualClient;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PlayAreaStateUpdate extends PlayerEvent {
    private final List<CardPosition> cardPositions;
    private final Map<GameResource, Integer> visibleResources;
    private final List<SerializableCorner> freeSerializableCorners;
    public PlayAreaStateUpdate(String nickname, Map<Point, PlaceableCard> cardMap, Map<GameResource, Integer> visibleResources, List<Corner> freeCorners) {
        super(nickname);
        this.cardPositions = new LinkedList<>();
        cardMap.keySet().forEach(
                (p) ->{
                    cardPositions.add(new CardPosition(p.row(), p.col(), cardMap.get(p).getCardID()));
                }
        );

        this.visibleResources = visibleResources;
        this.freeSerializableCorners = new LinkedList<>();
        freeCorners.forEach(
                (corner) -> {
                    freeSerializableCorners.add(
                            new SerializableCorner(
                                    corner.getCardRef().getCardID(),
                                    corner.getDirection().toString()
                            )
                    );
                }
        );
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.createPlayArea(nickname, cardPositions, visibleResources, freeSerializableCorners);
    }
}
