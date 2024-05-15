package it.polimi.ingsw.view.tui;

class ConsoleBackgroundColors {
    static final String RESET = "\u001B[00m";  //Resetta il colore

    // Normal intensity backgrounds
    static final String WHITE = "\u001B[47m"; //Colore bianco per i bordi
    static final String RED = "\u001B[41m";
    static final String PURPLE = "\u001B[45m";
    static final String GREEN = "\u001B[42m";
    static final String BLUE = "\033[44m";
    static final String YELLOW = "\033[43m";

    // High Intensity backgrounds
    static final String BLACK_BRIGHT = "\033[0;100m";
    static final String RED_BRIGHT = "\033[0;101m";
    static final String GREEN_BRIGHT = "\033[0;102m";
    static final String YELLOW_BRIGHT = "\033[0;103m";
    static final String BLUE_BRIGHT = "\033[0;104m";
    static final String PURPLE_BRIGHT = "\033[0;105m";
    static final String CYAN_BRIGHT = "\033[0;106m";
    static final String WHITE_BRIGHT = "\033[0;107m";
}
