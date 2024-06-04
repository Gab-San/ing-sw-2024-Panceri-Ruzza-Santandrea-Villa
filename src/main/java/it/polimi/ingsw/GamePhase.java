package it.polimi.ingsw;

/**
 * Enumeration of all phases of the game (as in particular states the game can be in at a given time)
 */
public enum GamePhase {
    CREATE("CREATION PHASE"),
    SETNUMPLAYERS("SET NUMBER OF PLAYERS PHASE"),
    JOIN("JOIN PHASE"),
    SETUP("SETUP PHASE"),
    PLACESTARTING("PLACE STARTING CARD PHASE"),
    CHOOSECOLOR("CHOOSE PLAYER COLOR PHASE"),
    DEALCARDS("DEAL FIRST HAND PHASE"),
    CHOOSEOBJECTIVE("CHOOSE SECRET OBJECTIVE CARD PHASE"),
    CHOOSEFIRSTPLAYER("CHOOSE THE FIRST PLAYER PHASE"),
    PLACECARD("PLACE CARD PHASE"),
    DRAWCARD("DRAW CARD PHASE"),
    WAITING_FOR_REJOIN("WAITING REJOIN OF DISCONNECTED PLAYERS"),
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
