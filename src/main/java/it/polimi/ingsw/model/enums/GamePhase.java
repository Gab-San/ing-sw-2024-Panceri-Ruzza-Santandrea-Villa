package it.polimi.ingsw.model.enums;

public enum GamePhase {
    CP("CREATION PHASE"),
    SNOFP("SET NUMBER OF PLAYERS PHASE"),
    CBP("CREATE BOARD PHASE"),
    JP("JOIN PHASE"),
    SP("SETUP PHASE"),
    GSCP("GIVE STARTING CARD PHASE"),
    PSCP("PLACE STARTING CARD PHASE"),
    CPCP("CHOOSE PLAYER COLOR PHASE"),
    DFHP("DEAL FIRST HAND PHASE"),
    DSOCP("DEAL SECRET OBJECTIVE CARDS PHASE"),
    CSOCP("CHOOSE SECRET OBJECTIVE CARD PHASE"),
    CFPP("CHOOSE THE FIRST PLAYER PHASE"),
    PP("PLAY PHASE"),
    PCP("PLACE CARD PHASE"),
    DCP("DRAW CARD PHASE"),
    EP("ENDGAME PHASE"),
    ESOCP("EVALUATE SECRET OBJECTIVE CARD PHASE"),
    STWP("SHOW THE WINNER PHASE");

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
