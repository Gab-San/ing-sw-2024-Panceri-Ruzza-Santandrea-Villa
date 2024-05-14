package it.polimi.ingsw.view.model.cards;

import it.polimi.ingsw.view.model.enums.GameResourceView;

import java.util.List;
import java.util.stream.Collectors;

public class ViewStartCard extends ViewPlaceableCard {
    private final List<GameResourceView> centralFrontResources;

    public ViewStartCard(String cardID, String imageFrontName, String imageBackName, List<ViewCorner> corners, List<GameResourceView> centralFrontResources) {
        super(cardID, imageFrontName, imageBackName, corners);
        this.centralFrontResources = centralFrontResources;
    }

    @Override
    public GameResourceView getCardColour() {
        return null;
    }
    public GameResourceView[] getCentralFrontResourcesAsArray() {
        GameResourceView[] resourceArray = new GameResourceView[3];
        for (int i = 0; i < 3; i++) {
            try{
                resourceArray[i] = centralFrontResources.get(i);
            }catch (IndexOutOfBoundsException e){
                resourceArray[i] = null;
            }
        }
        return resourceArray;
    }
}
