package it.polimi.ingsw.controller.EnumsController;

public enum StatesEnum {
    JS("JOIN STATE"), SS("SETUP STATE"), PS("PLAY STATE"), ES("ENDGAME STATE");

    private final String gameStateExtended;

    StatesEnum(String gameState) {
        this.gameStateExtended = gameState;
    }

    @Override
    public String toString() {
        return gameStateExtended;
    }

}
