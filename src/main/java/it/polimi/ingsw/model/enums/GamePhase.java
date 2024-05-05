package it.polimi.ingsw.model.enums;

public enum GamePhase {
    CP("CREATION PHASE"), SNOFP("SET NUMBER OF PLAYERS PHASE"),
    JP("JOIN PHASE"),
    SP("SETUP PHASE"), CBP("CREATE BOARD PHASE"), GSCP("GIVE STARTING CARD PHASE"), CPCP("CHOOSE PLAYER COLOR PHASE"),
        DFHP("DEAL FIST HAND PHASE"), DOCP("DEAL OBJECTIVE CARDS PHASE"), CSOCP("CHOOSE SECRET OBJECTIVE CARD PHASE"),
        CFPP("CHOOSE THE FIRST PLAYER PHASE"),
    PP("PLAY PHASE"), PCP("PLACE CARD PHASE"), DCP("DRAW CARD PHASE"),
    EP("ENDGAME PHASE"), ESOCP("EVALUATE SECRETE OBJECTIVE CARD PHASE"), STWP("SHOW THE WINNER PHASE");

    private final String gamePhaseExtended;

    GamePhase(String gamePhase) {
        this.gamePhaseExtended = gamePhase;
    }

    @Override
    public String toString() {
        return gamePhaseExtended;
    }

}
