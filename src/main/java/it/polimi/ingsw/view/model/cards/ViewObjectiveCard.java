package it.polimi.ingsw.view.model.cards;

import it.polimi.ingsw.GameResource;

import java.awt.*;

/**
 * The objective card representation in the ViewModel. <br>
 * This class is more of a "record" of information regarding the
 * objective card, as it does not hold the information on how to calculate
 * the objective solves.
 */
public class ViewObjectiveCard extends ViewCard {
    /**
     * The string identifying a "pattern strategy" type of objective card
     */
    public static final String PATTERN_TYPE = "PATTERNTYPE";
    /**
     * The string identifying a "resource strategy" type of objective card
     */
    public static final String RESOURCE_TYPE = "RESOURCETYPE";
    /**
     * Either "PATTERNTYPE" or "RESOURCETYPE"
     */
    private final String objectiveStrategyType;
    /**
     * String representation of this objective's strategy
     */
    private final String objectiveStrategyAsString;
    /**
     * Points gained per solve of this objective
     */
    private final int pointsPerSolve;

    /**
     * Constructs the view objective card.
     * @param cardID card's ID
     * @param imageFrontName front image filename
     * @param imageBackName back image filename
     * @param objectiveStrategyType must be "PATTERNTYPE" or "RESOURCETYPE"
     * @param pointsPerSolve amount of points gained per solve of this objective
     * @param objectiveStrategyAsString the pattern (if PATTERNTYPE) or the resources as string of initials e.g. "MLW" (if RESOURCETYPE)
     */
    public ViewObjectiveCard(String cardID, String imageFrontName, String imageBackName, String objectiveStrategyType, int pointsPerSolve, String objectiveStrategyAsString) {
        super(cardID, imageFrontName, imageBackName);
        this.objectiveStrategyType = objectiveStrategyType;
        this.objectiveStrategyAsString = objectiveStrategyAsString;
        this.pointsPerSolve = pointsPerSolve;
    }

    /**
     * The objective card has no color, so null is returned.
     * @return null
     */
    public GameResource getCardColour(){
        return null;
    }

    /**
     * Getter for the string representation of this objective's strategy.
     * @return the string representation of this objective's strategy.
     */
    public String getObjectiveStrategyAsString() {
        return objectiveStrategyAsString;
    }

    /**
     * Compares the objective strategy type with "PATTERNTYPE".
     * @return true if the strategyType equals "PATTERNTYPE", false otherwise
     */
    public boolean isPatternType(){
        return objectiveStrategyType.equals(PATTERN_TYPE);
    }

    /**
     * Getter for the points gained per solve of this objective
     * @return the points gained per solve of this objective
     */
    public int getPointsPerSolve() {
        return pointsPerSolve;
    }

    @Override
    public Dimension getPreferredSize() {
        return super.getPreferredSize();
    }
}
