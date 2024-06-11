package it.polimi.ingsw.view.tui.scenes;

import it.polimi.ingsw.view.model.ViewBoard;
import it.polimi.ingsw.view.model.ViewHand;
import it.polimi.ingsw.view.model.ViewOpponentHand;
import it.polimi.ingsw.view.tui.TUI_Scene;

import java.util.Comparator;
import java.util.List;

import static it.polimi.ingsw.view.tui.ConsoleTextColors.*;
import static it.polimi.ingsw.view.tui.ConsoleBackgroundColors.getColorFromEnum;
import static it.polimi.ingsw.view.tui.ConsoleColorsCombiner.combine;

/**
 * The Endgame UI (leaderboard) scene printer for the TUI
 */
public class PrintEndgameUI extends TUI_Scene {
    private final ViewBoard board;
    /**
     * Spacing of the leaderboard, to not print it too close to console border
     */
    private static final int LEFT_SPACING = 16;
    boolean atLeast2Players;

    /**
     * Constructs the Endgame UI (leaderboard) scene
     * @param board the ViewBoard of the game to be displayed
     * @param atLeast2Players true if the game was not won by default
     */
    public PrintEndgameUI(ViewBoard board, boolean atLeast2Players) {
        this.board = board;
        this.atLeast2Players = atLeast2Players;
    }

    /**
     * @param str string to pad
     * @param len desired string length after padding
     * @return padded string by adding spaces at the start of str <br>
     * Could return str unchanged if str.length >= len
     */
    private String padLeft(String str, int len){
        int diff = len - str.length();
        if(diff > 0)
            return " ".repeat(diff) + str;
        else
            return str;
    }
    /**
     * @param h the hand of the player whose score is to be padded
     * @param len desired string length after padding
     * @return score of player as string padded by adding spaces at the start <br>
     * Could return str unchanged if str.length >= len
     */
    private String padScore(ViewHand h, int len) {
        String score = Integer.toString(board.getScore(h.getNickname()));
        return padLeft(score, len);
    }

    /**
     * @param hand Player's hand
     * @return Player's nickname colored as Black text on player's color background
     */
    private String colorNickname(ViewHand hand){
        return combine(BLACK_TEXT, getColorFromEnum(hand.getColor()))
                + " " + hand.getNickname() + " " + RESET;
    }

    /**
     * Prints a horizontal line of '-'
     */
    private void printSeparator(){
        out.println("\n" + "-".repeat(LEFT_SPACING*4) + "\n");
    }

    /**
     * @return string of LEFT_SPACING (internal constant) spaces
     */
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

        int maxScore = board.getScore(playersByScore.get(0).getNickname());
        List<ViewHand> winners = playersByScore.stream()
                .filter(h -> board.getScore(h.getNickname()) == maxScore)
                .toList();
        if(winners.size() > 1 && atLeast2Players)
            out.print(getPadSpaces() + YELLOW_TEXT + "THE WINNERS ARE:   ");
        else
            out.print(getPadSpaces() + YELLOW_TEXT + "THE WINNER IS:   ");

        //if I'm the only one left in the game, I win automatically, regardless of score
        if(!atLeast2Players) {
            out.println(colorNickname(board.getPlayerHand()) + YELLOW_TEXT + " BY DEFAULT" + RESET);
        }
        else{
            winners.forEach(winner -> out.print(colorNickname(winner) + "  "));
            out.println();
        }

        printSeparator();
    }

}
