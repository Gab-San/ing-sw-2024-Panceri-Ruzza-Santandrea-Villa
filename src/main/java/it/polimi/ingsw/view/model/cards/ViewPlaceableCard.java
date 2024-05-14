package it.polimi.ingsw.view.model.cards;

import it.polimi.ingsw.CornerDirection;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/*Necessari

placementCost (stringa singola)
pointsOnPlace
backResource
corners Mappa direction-resource

 */

public class ViewPlaceableCard extends ViewCard{
    Map<CornerDirection, ViewCorner> corners;

    public ViewPlaceableCard(String cardID, List<ViewCorner> corners) throws IllegalArgumentException {
        super(cardID);
        this.corners = new Hashtable<>();
        for(ViewCorner c : corners){
            if(this.corners.put(c.getDirection(), c) != null)
                throw new IllegalArgumentException("Corners with duplicate directions were passed in ViewPlaceableCard constructor");
        }
    }
}
