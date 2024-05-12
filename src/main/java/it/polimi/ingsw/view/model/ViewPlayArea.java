package it.polimi.ingsw.view.model;

import it.polimi.ingsw.model.Point;
import it.polimi.ingsw.model.enums.GameResource;
import it.polimi.ingsw.view.model.cards.*;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ViewPlayArea {
    private final Map<Point, ViewCard> cardMatrix;
    private final Hashtable<GameResource, Integer> visibleResources;
    private final List<ViewCorner> freeCorners;

    public ViewPlayArea() {
        this.cardMatrix = new Hashtable<>();
        this.visibleResources = new Hashtable<>();
        this.freeCorners = new LinkedList<>();
    }

    public void placeCard(Point p, ViewCard card){
        cardMatrix.put(p, card);
    }


}
