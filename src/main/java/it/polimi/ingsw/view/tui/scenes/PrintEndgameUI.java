package it.polimi.ingsw.view.tui.scenes;

import it.polimi.ingsw.view.model.ViewBoard;
import it.polimi.ingsw.view.model.ViewHand;
import it.polimi.ingsw.view.tui.TUI_Scene;

import java.util.Comparator;
import java.util.List;

import static it.polimi.ingsw.view.tui.ConsoleTextColors.*;
import static it.polimi.ingsw.view.tui.ConsoleBackgroundColors.getColorFromEnum;
import static it.polimi.ingsw.view.tui.ConsoleColorsCombiner.combine;

public class PrintEndgameUI extends TUI_Scene {
    private final ViewBoard board;
    private static final int LEFT_SPACING = 16;

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
    private String padScore(ViewHand h, int len) {
        String score = Integer.toString(board.getScore(h.getNickname()));
        return padLeft(score, len);
    }
    private String colorNickname(ViewHand hand){
        return combine(BLACK_TEXT, getColorFromEnum(hand.getColor()))
                + " " + hand.getNickname() + " " + RESET;
    }
    private void printSeparator(){
        out.println("\n" + "-".repeat(LEFT_SPACING*4) + "\n");
    }
    private String getPadSpaces(){
        return " ".repeat(LEFT_SPACING);
    }

    @Override
    protected void print() {
        out.println(getPadSpaces() + YELLOW_TEXT + "The game has ended!" + RESET);
        out.println(getPadSpaces() + "Here are the final scores: ");
        printSeparator();

        List<ViewHand> playersByScore = board.getAllPlayerHands().stream()
                .sorted(Comparator.comparingInt(h -> -board.getScore(h.getNickname())))
                .toList();

        final int maxNumLen = playersByScore.stream()
                .map(h -> board.getScore(h.getNickname()))
                .mapToInt(score -> Integer.toString(score).length())
                .max().orElse(0);



        List<String> leaderboard = playersByScore.stream()
                    .map(h -> colorNickname(h) + " <--> " + padScore(h, maxNumLen))
                    .toList();

        final int maxLen = Math.max(
                leaderboard.stream().mapToInt(String::length)
                        .max().orElse(0) + LEFT_SPACING/4
                ,LEFT_SPACING);

        leaderboard.stream()
                .map(l -> padLeft(l, maxLen))
                .forEach(out::println);

        out.println();
        out.println(getPadSpaces() + YELLOW_TEXT + "THE WINNER IS:   " + colorNickname(playersByScore.get(0)));

        printSeparator();
    }

}
