package it.polimi.ingsw.view.model.cards;

import it.polimi.ingsw.GameResource;

import java.util.List;

public class ViewStartCard extends ViewPlaceableCard {
    private final List<GameResource> centralFrontResources;

    public ViewStartCard(String cardID, String imageFrontName, String imageBackName, List<ViewCorner> corners, List<GameResource> centralFrontResources) {
        super(cardID, imageFrontName, imageBackName, corners);
        this.centralFrontResources = centralFrontResources;
    }

    @Override
    public GameResource getCardColour() {
        return null;
    }
    public GameResource[] getCentralFrontResourcesAsArray() {
        GameResource[] resourceArray = new GameResource[3];
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
