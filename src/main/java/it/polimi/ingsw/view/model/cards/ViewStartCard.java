package it.polimi.ingsw.view.model.cards;

import it.polimi.ingsw.GameResource;

import java.util.List;

/**
 * The starting card representation in the ViewModel.
 */
public class ViewStartCard extends ViewPlaceableCard {
    private final List<GameResource> centralFrontResources;

    /**
     * Constructs the placeable card base.
     * @param cardID this card's ID
     * @param imageFrontName the front image file name
     * @param imageBackName the back image file name
     * @param corners list of corners (not attached to another card)
     * @param centralFrontResources a list of resources that appear at the center
     *                              on the front face of this start card
     */
    public ViewStartCard(String cardID, String imageFrontName, String imageBackName, List<ViewCorner> corners, List<GameResource> centralFrontResources) {
        super(cardID, imageFrontName, imageBackName, corners);
        this.centralFrontResources = centralFrontResources;
    }

    /**
     * @return null as the starting cards do not have a color.
     */
    @Override
    public GameResource getCardColour() {
        return null;
    }

    /**
     * @return the centralFrontResources list as a GameResource[3] in no particular order.
     *          Some cells of the array may be null if the start card has less than
     *          3 central front resources
     */
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
