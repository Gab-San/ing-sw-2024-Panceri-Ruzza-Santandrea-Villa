package it.polimi.ingsw.model.enums;

/**
 * Enumeration of all phases of the game (as in particular states the game can be in at a given time)
 */
public enum GamePhase {
    CREATE("CREATION PHASE"),
    SNP("SET NUMBER OF PLAYERS PHASE"),
    JOIN("JOIN PHASE"),
    SETUP("SETUP PHASE"),
    PSCP("PLACE STARTING CARD PHASE"),
    CHOOSECOLOR("CHOOSE PLAYER COLOR PHASE"),
    DEALCARDS("DEAL FIRST HAND PHASE"),
    CHOOSEOBJECTIVE("CHOOSE SECRET OBJECTIVE CARD PHASE"),
    CFPP("CHOOSE THE FIRST PLAYER PHASE"),
    PCP("PLACE CARD PHASE"),
    DCP("DRAW CARD PHASE"),
    EVALOBJ("EVALUATE SECRET OBJECTIVE CARD PHASE"),
    SHOWWIN("SHOW THE WINNER PHASE");

    private final String gamePhaseExtended;

    GamePhase(String gamePhase) {
        this.gamePhaseExtended = gamePhase;
    }

    @Override
    public String toString() {
        return gamePhaseExtended;
    }
    public boolean equals(String phase){
        return (this.gamePhaseExtended.equals(phase));
    }
    public boolean equals(GamePhase phase){
        return this == phase;
    }
}
