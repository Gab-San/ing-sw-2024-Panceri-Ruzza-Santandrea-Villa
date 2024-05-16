package it.polimi.ingsw.view.model.cards;

import it.polimi.ingsw.GameResource;

public class ViewObjectiveCard extends ViewCard {
    public static String PATTERN_TYPE = "PATTERNTYPE";
    public static String RESOURCE_TYPE = "RESOURCETYPE";
    private final String objectiveStrategyType;
    private final String objectiveStrategyValue;

    /**
     * @param cardID card's ID
     * @param imageFrontName front image filename
     * @param imageBackName back image filename
     * @param objectiveStrategyType must be PATTERNTYPE or RESOURCETYPE
     * @param objectiveStrategyValue the pattern (if PATTERNTYPE) or the resources as string of initials e.g. "MLW" (if RESOURCETYPE)
     */
    public ViewObjectiveCard(String cardID, String imageFrontName, String imageBackName, String objectiveStrategyType, String objectiveStrategyValue) {
        super(cardID, imageFrontName, imageBackName);
        this.objectiveStrategyType = objectiveStrategyType;
        this.objectiveStrategyValue = objectiveStrategyValue;
    }

    public GameResource getCardColour(){
        return null;
    }

    public String getObjectiveStrategyType() {
        return objectiveStrategyType;
    }
    public String getObjectiveStrategyValue() {
        return objectiveStrategyValue;
    }
    public boolean isPatternType(){
        return objectiveStrategyType.equals(PATTERN_TYPE);
    }
}
