package it.polimi.ingsw.view.tui;

public class ConsoleTextColors {
    public static final String PREFIX = "\u001B[";
    public static final String POSTFIX = "m";
    // Reset
    public static final String RESET = PREFIX + "00" + POSTFIX;  // Text Reset

    // Regular Colors
    public static final String BLACK_TEXT = PREFIX + "0;30" + POSTFIX;  
    public static final String RED_TEXT = PREFIX + "0;31" + POSTFIX;    
    public static final String GREEN_TEXT = PREFIX + "0;32" + POSTFIX;  
    public static final String YELLOW_TEXT = PREFIX + "0;33" + POSTFIX; 
    public static final String BLUE_TEXT = PREFIX + "0;34" + POSTFIX;   
    public static final String PURPLE_TEXT = PREFIX + "0;35" + POSTFIX; 
    public static final String CYAN_TEXT = PREFIX + "0;36" + POSTFIX;   
    public static final String WHITE_TEXT = PREFIX + "0;37" + POSTFIX;  

    // Bold
    public static final String BLACK_BOLD_TEXT = PREFIX + "1;30" + POSTFIX; 
    public static final String RED_BOLD_TEXT = PREFIX + "1;31" + POSTFIX;   
    public static final String GREEN_BOLD_TEXT = PREFIX + "1;32" + POSTFIX; 
    public static final String YELLOW_BOLD_TEXT = PREFIX + "1;33" + POSTFIX;
    public static final String BLUE_BOLD_TEXT = PREFIX + "1;34" + POSTFIX;  
    public static final String PURPLE_BOLD_TEXT = PREFIX + "1;35" + POSTFIX;
    public static final String CYAN_BOLD_TEXT = PREFIX + "1;36" + POSTFIX;  
    public static final String WHITE_BOLD_TEXT = PREFIX + "1;37" + POSTFIX; 

    // Underline
    public static final String BLACK_UNDERLINED_TEXT = PREFIX + "4;30" + POSTFIX; 
    public static final String RED_UNDERLINED_TEXT = PREFIX + "4;31" + POSTFIX;   
    public static final String GREEN_UNDERLINED_TEXT = PREFIX + "4;32" + POSTFIX; 
    public static final String YELLOW_UNDERLINED_TEXT = PREFIX + "4;33" + POSTFIX;
    public static final String BLUE_UNDERLINED_TEXT = PREFIX + "4;34" + POSTFIX;  
    public static final String PURPLE_UNDERLINED_TEXT = PREFIX + "4;35" + POSTFIX;
    public static final String CYAN_UNDERLINED_TEXT = PREFIX + "4;36" + POSTFIX;  
    public static final String WHITE_UNDERLINED_TEXT = PREFIX + "4;37" + POSTFIX; 

    // High Intensity
    public static final String BLACK_BRIGHT_TEXT = PREFIX + "0;90" + POSTFIX; 
    public static final String RED_BRIGHT_TEXT = PREFIX + "0;91" + POSTFIX;   
    public static final String GREEN_BRIGHT_TEXT = PREFIX + "0;92" + POSTFIX; 
    public static final String YELLOW_BRIGHT_TEXT = PREFIX + "0;93" + POSTFIX;
    public static final String BLUE_BRIGHT_TEXT = PREFIX + "0;94" + POSTFIX;  
    public static final String PURPLE_BRIGHT_TEXT = PREFIX + "0;95" + POSTFIX;
    public static final String CYAN_BRIGHT_TEXT = PREFIX + "0;96" + POSTFIX;  
    public static final String WHITE_BRIGHT_TEXT = PREFIX + "0;97" + POSTFIX; 

    // Bold High Intensity
    public static final String BLACK_BOLD_BRIGHT_TEXT = PREFIX + "1;90" + POSTFIX;
    public static final String RED_BOLD_BRIGHT_TEXT = PREFIX + "1;91" + POSTFIX;  
    public static final String GREEN_BOLD_BRIGHT_TEXT = PREFIX + "1;92" + POSTFIX;
    public static final String YELLOW_BOLD_BRIGHT_TEXT = PREFIX + "1;93" + POSTFIX;
    public static final String BLUE_BOLD_BRIGHT_TEXT = PREFIX + "1;94" + POSTFIX; 
    public static final String PURPLE_BOLD_BRIGHT_TEXT = PREFIX + "1;95" + POSTFIX;
    public static final String CYAN_BOLD_BRIGHT_TEXT = PREFIX + "1;96" + POSTFIX; 
    public static final String WHITE_BOLD_BRIGHT_TEXT = PREFIX + "1;97" + POSTFIX;
}
