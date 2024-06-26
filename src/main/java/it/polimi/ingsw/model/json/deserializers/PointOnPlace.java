package it.polimi.ingsw.model.json.deserializers;

/**
 * This class represents a json version of cards placement value.
 * <p>
 *     Placement value is defined by the scoring method and the amount of points
 *     scored per solve.
 * </p>
 */
public class PointOnPlace {
    private String type;
    private int amount;

    /**
     * Default constructor.
     */
    public PointOnPlace(){
        type = null;
        amount = 0;
    }

    /**
     * Returns the card's scoring method.
     * @return card's scoring method
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the card's scoring method.
     * @param type card's scoring method
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Returns the amount of points per solve.
     * @return points value
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Sets the amount of points per solve.
     * @param amount points value
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }
}