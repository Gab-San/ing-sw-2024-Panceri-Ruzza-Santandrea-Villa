package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.GameResource;

import java.util.Hashtable;
import java.util.NoSuchElementException;

public abstract class PlaceableCard extends Card{
    int row;
    int col;
    Hashtable<CornerDirection, Corner> corners;

    /**
     * @param cornDir indicates the selected corner direction;
     * @return the corner in the selected direction;
     * @throws NoSuchElementException if the corner in the selected direction is filled;
     */
    public Corner getCorner(CornerDirection cornDir) throws NoSuchElementException{
        if(!corners.containsKey(cornDir)){
            throw new NoSuchElementException("The search corner is filled");
        }
        return corners.get(cornDir);
    }

    /**
     * @return an array with the count of resources to add to the visible resources count on the play area
     */
    abstract public int[] getCardResources();
    abstract public GameResource getCardColor();

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
}
