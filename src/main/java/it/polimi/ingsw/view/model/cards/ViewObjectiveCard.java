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
    //DOCS add attribute javadoc
    public static final String PATTERN_TYPE = "PATTERNTYPE";
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
     * @param cardID card's ID
     * @param imageFrontName front image filename
     * @param imageBackName back image filename
     * @param objectiveStrategyType must be "PATTERNTYPE" or "RESOURCETYPE"
     * @param objectiveStrategyAsString the pattern (if PATTERNTYPE) or the resources as string of initials e.g. "MLW" (if RESOURCETYPE)
     */
    public ViewObjectiveCard(String cardID, String imageFrontName, String imageBackName, String objectiveStrategyType, int pointsPerSolve, String objectiveStrategyAsString) {
        super(cardID, imageFrontName, imageBackName);
        this.objectiveStrategyType = objectiveStrategyType;
        this.objectiveStrategyAsString = objectiveStrategyAsString;
        this.pointsPerSolve = pointsPerSolve;
    }

    /**
     * @return null as the objective cards do not have a color.
     */
    public GameResource getCardColour(){
        return null;
    }

    public String getObjectiveStrategyAsString() {
        return objectiveStrategyAsString;
    }

    /**
     * @return true if the strategyType is "PATTERNTYPE"
     */
    public boolean isPatternType(){
        return objectiveStrategyType.equals(PATTERN_TYPE);
    }
    public int getPointsPerSolve() {
        return pointsPerSolve;
    }

    @Override
    public Dimension getPreferredSize() {
        return super.getPreferredSize();
    }
}
