package it.polimi.ingsw.view.model.cards;

import it.polimi.ingsw.GameResource;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ViewGoldCard extends ViewPlayCard{
    private final List<GameResource> placementCost;
    private final String strategyAsString;

    public ViewGoldCard(String cardID, String imageFrontName, String imageBackName, List<ViewCorner> corners,
                        int pointsOnPlace, GameResource backResource, List<GameResource> placementCost, String strategyAsString) {

        super(cardID, imageFrontName, imageBackName, corners, pointsOnPlace, backResource);
        this.placementCost = placementCost.stream().sorted(Comparator.comparingInt(GameResource::getResourceIndex)).toList();
        this.strategyAsString = strategyAsString;
    }

    @Override
    public String getPointsOnPlaceAsString() {
        return super.getPointsOnPlaceAsString() + " | " + strategyAsString;
    }

    public String getPlacementCostAsString(){
        return placementCost.stream()
                .map(GameResource::toString)
                .collect(Collectors.joining());
    }
}
