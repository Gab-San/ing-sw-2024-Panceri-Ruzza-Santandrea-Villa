package it.polimi.ingsw.view.model.cards;


import it.polimi.ingsw.GameResource;

public class ViewObjectiveCard extends ViewCard {
    String objectiveStrategyType;
    String objectiveStrategyValue;

    public ViewObjectiveCard(String cardID, String imageFrontName, String imageBackName, String objectiveStrategyType, String objectiveStrategyValue) {
        super(cardID, imageFrontName, imageBackName);
        this.objectiveStrategyType = objectiveStrategyType;
        this.objectiveStrategyValue = objectiveStrategyValue;
    }

    public GameResource getCardColour(){
        return null;
    }
    public String[] getObjectiveStrategyAsStringRows(){
        String[] centerRows = new String[3];
        //TODO: represent resource strategy as string rows
        //TODO: represent pattern strategy as string rows
        return centerRows;
    }
}
