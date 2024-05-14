package it.polimi.ingsw.view.model.cards;

import it.polimi.ingsw.view.model.enums.GameResourceView;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ViewGoldCard extends ViewPlayCard{
    private final List<GameResourceView> placementCost;
    private final String strategyAsString;

    public ViewGoldCard(String cardID, String imageFrontName, String imageBackName, List<ViewCorner> corners,
                        int pointsOnPlace, GameResourceView backResource, List<GameResourceView> placementCost, String strategyAsString) {

        super(cardID, imageFrontName, imageBackName, corners, pointsOnPlace, backResource);
        this.placementCost = placementCost.stream().sorted(Comparator.comparingInt(GameResourceView::getResourceIndex)).toList();
        this.strategyAsString = strategyAsString;
    }

    @Override
    public String getPointsOnPlaceAsString() {
        return super.getPointsOnPlaceAsString() + " | " + strategyAsString;
    }

    public String getPlacementCostAsString(){
        return placementCost.stream()
                .map(GameResourceView::toString)
                .collect(Collectors.joining());
    }
}
