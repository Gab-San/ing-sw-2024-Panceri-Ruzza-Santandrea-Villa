package it.polimi.ingsw;

/**
 * Enumeration of all phases of the game (as in particular states the game can be in at a given time)
 */
public enum GamePhase {
    /**
     * Creation phase.
     */
    CREATE("CREATION PHASE"),
    /**
     * Phase in which the starting player sets the number of players.
     */
    SETNUMPLAYERS("SET NUMBER OF PLAYERS PHASE"),
    /**
     * Players wait for the lobby to fill.
     */
    JOIN("JOIN PHASE"),
    /**
     * Setup phase.
     */
    SETUP("SETUP PHASE"),
    /**
     * Starting card placing phase.
     */
    PLACESTARTING("PLACE STARTING CARD PHASE"),
    /**
     * Phase in which each player chooses their color.
     */
    CHOOSECOLOR("CHOOSE PLAYER COLOR PHASE"),
    /**
     * Dealing card phase.
     */
    DEALCARDS("DEAL FIRST HAND PHASE"),
    /**
     * Phase in which each player chooses their secret objective card.
     */
    CHOOSEOBJECTIVE("CHOOSE SECRET OBJECTIVE CARD PHASE"),
    /**
     * Phase in which the first player is determined.
     */
    CHOOSEFIRSTPLAYER("CHOOSE THE FIRST PLAYER PHASE"),
    /**
     * Card placing phase.
     */
    PLACECARD("PLACE CARD PHASE"),
    /**
     * Drawing phase.
     */
    DRAWCARD("DRAW CARD PHASE"),
    /**
     * Intermediate phase during which the last standing player waits for someone to rejoin.
     */
    WAITING_FOR_REJOIN("WAITING REJOIN OF DISCONNECTED PLAYERS"),
    /**
     * Point calculation phase.
     */
    EVALOBJ("EVALUATE SECRET OBJECTIVE CARD PHASE"),
    /**
     * Endgame phase in which the winner is shown.
     */
    SHOWWIN("SHOW THE WINNER PHASE");

    private final String gamePhaseExtended;

    GamePhase(String gamePhase) {
        this.gamePhaseExtended = gamePhase;
    }

    @Override
    public String toString() {
        return gamePhaseExtended;
    }
}
