package it.polimi.ingsw.model;

import it.polimi.ingsw.model.card.PlaceableCard;
import it.polimi.ingsw.model.enums.Resource;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PlayArea {
    private final DoubleMap<Integer, Integer, PlaceableCard> cardMatrix;
    private final Map<Resource, Integer> visibleResources;

    public PlayArea(){
        cardMatrix = new DoubleMap<>();
        visibleResources = new HashMap<>();
    }

    public DoubleMapRO<Integer, Integer, PlaceableCard> getCardMatrix(){
        return cardMatrix;
    }

    public Map<Resource, Integer> getVisibleResources() {
        return Collections.unmodifiableMap(visibleResources);
    }
}
