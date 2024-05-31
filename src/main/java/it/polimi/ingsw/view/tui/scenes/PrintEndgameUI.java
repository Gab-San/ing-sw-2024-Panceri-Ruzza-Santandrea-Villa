package it.polimi.ingsw.view.tui.scenes;

import it.polimi.ingsw.view.model.ViewBoard;
import it.polimi.ingsw.view.model.ViewHand;
import it.polimi.ingsw.view.tui.ConsoleBackgroundColors;
import it.polimi.ingsw.view.tui.TUI_Scene;

import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.view.tui.ConsoleTextColors.*;

public class PrintEndgameUI extends TUI_Scene {
    private final ViewBoard board;
    private static final int LEFT_SPACING = 10;

    public PrintEndgameUI(ViewBoard board) {
        this.board = board;
    }

    private String padLeft(String str, int len){
        int diff = len - str.length();
        if(diff > 0)
            return " ".repeat(diff) + str;
        else
            return str;
    }
    private String colorNickname(ViewHand hand){
        return ConsoleBackgroundColors.getColorFromEnum(hand.getColor())
                + " " + hand.getNickname() + " " + RESET;
    }
    private void printSeparator(){
        out.println("\n" + "-".repeat(50) + "\n");
    }
    private String getPadSpaces(){
        return " ".repeat(LEFT_SPACING);
    }

    @Override
    protected void print() {
        out.println(getPadSpaces() + YELLOW_TEXT + "The game has ended!" + RESET);
        out.println(getPadSpaces() + "Here are the final scores: ");
        printSeparator();

        List<String> leaderboard = board.getAllPlayerHands().stream()
                    .sorted(Comparator.comparingInt(h -> -board.getScore(h.getNickname())))
                    .map(h -> colorNickname(h) + " <--> " + board.getScore(h.getNickname()))
                    .toList();

        final int maxLen = leaderboard.stream().mapToInt(String::length).max().orElse(0)
                + LEFT_SPACING;

        leaderboard.stream()
                .map(str -> padLeft(str, maxLen))
                .forEach(out::println);


        out.print(getPadSpaces() + YELLOW_TEXT + "THE WINNER IS: " + RESET);
        out.println(getPadSpaces() + leaderboard.get(0).split(" <--> ")[0]);

        printSeparator();
    }
}
