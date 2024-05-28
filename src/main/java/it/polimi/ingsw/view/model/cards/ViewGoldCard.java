package it.polimi.ingsw.view.model.cards;

import it.polimi.ingsw.GameResource;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ViewGoldCard extends ViewPlayCard{
    private final List<GameResource> placementCost;
    private final String strategyAsString;

    public ViewGoldCard(String cardID, String imageFrontName, String imageBackName, List<ViewCorner> corners,
                        int pointsOnPlace, GameResource backResource, List<GameResource> placementCost, String strategyAsString) {

        super(cardID, imageFrontName, imageBackName, corners, pointsOnPlace, backResource);
        this.placementCost = placementCost.stream().sorted(Comparator.comparingInt(GameResource::getResourceIndex)).toList(); // unmodifiable == thread-safe
        this.strategyAsString = strategyAsString;
    }

    @Override
    public String getPointsOnPlaceAsString() {
        if(!isFaceUp() || pointsOnPlace <= 0) return "";
        else return super.getPointsOnPlaceAsString() + " | " + strategyAsString;
    }

    public String getPlacementCostAsString(){
        if(!isFaceUp()) return "";
        else return placementCost.stream()
                .map(GameResource::toString)
                .collect(Collectors.joining());
    }
}
