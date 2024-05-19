package it.polimi.ingsw.view.model.cards;

import it.polimi.ingsw.GameResource;

public class ViewObjectiveCard extends ViewCard {
    public static final String PATTERN_TYPE = "PATTERNTYPE";
    public static final String RESOURCE_TYPE = "RESOURCETYPE";
    private final String objectiveStrategyType;
    private final String objectiveStrategyAsString;
    private final int pointsPerSolve;

    /**
     * @param cardID card's ID
     * @param imageFrontName front image filename
     * @param imageBackName back image filename
     * @param objectiveStrategyType must be PATTERNTYPE or RESOURCETYPE
     * @param objectiveStrategyAsString the pattern (if PATTERNTYPE) or the resources as string of initials e.g. "MLW" (if RESOURCETYPE)
     */
    public ViewObjectiveCard(String cardID, String imageFrontName, String imageBackName, String objectiveStrategyType, int pointsPerSolve, String objectiveStrategyAsString) {
        super(cardID, imageFrontName, imageBackName);
        this.objectiveStrategyType = objectiveStrategyType;
        this.objectiveStrategyAsString = objectiveStrategyAsString;
        this.pointsPerSolve = pointsPerSolve;
    }

    public GameResource getCardColour(){
        return null;
    }

    public String getObjectiveStrategyAsString() {
        return objectiveStrategyAsString;
    }
    public boolean isPatternType(){
        return objectiveStrategyType.equals(PATTERN_TYPE);
    }
    public int getPointsPerSolve() {
        return pointsPerSolve;
    }
}
