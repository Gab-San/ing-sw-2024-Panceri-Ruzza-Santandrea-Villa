package it.polimi.ingsw.view;

public class PrintCard {

    public static void printCardBackground(String cardId, String color) {
        String colorCode;

        switch (color.toLowerCase()) {
            case "red": //mushroom
                colorCode = "\u001B[41m";
                break;
            case "purple": //butterfly
                colorCode = "\u001B[45m";
                break;
            case "green": //leaf
                colorCode = "\u001B[42m";
                break;
            case "blue": //wolf
                colorCode = "\033[44m";
                break;
            case "yellow": //starting + objective
                colorCode = "\033[43m";
                break;
            default:
                colorCode = "\u001B[0m";
                System.out.println("Color not recognized. Defaulting to no color.");
        }

        String white = "\u001B[47m"; // Colore bianco per i bordi
        String reset = "\u001B[0m";  // Resetta il colore

        String[] card = new String[5];
        card[0] = white + " M " + reset + colorCode + "                " + reset + white + " M " + reset;
        card[1] = colorCode + "                      " + reset;
        card[2] = colorCode + "          M           " + reset;
        card[3] = colorCode + "                      " + reset;
        card[4] = white + " M " + reset + colorCode + "                " + reset + white + " M " + reset;

        for (String line : card) {
            System.out.println(line);
        }
    }

    public static void main(String[] args) {
        // Esempio di utilizzo
        printCardBackground("G0", "blue");
    }
}
