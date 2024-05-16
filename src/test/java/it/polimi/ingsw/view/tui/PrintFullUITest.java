package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.view.model.*;
import it.polimi.ingsw.view.model.cards.*;
import org.junit.jupiter.api.BeforeEach;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

import static it.polimi.ingsw.CornerDirection.*;
import static it.polimi.ingsw.view.ViewCardGenerator.*;
import static it.polimi.ingsw.view.tui.ConsoleTextColors.*;
import static it.polimi.ingsw.view.tui.PrintPlayerHandTest.getRandomObjectiveCardList;
import static it.polimi.ingsw.view.tui.PrintPlayerHandTest.getRandomPlayCardList;

public class PrintFullUITest {
    PrintPlayerUI printPlayerUI;
    Random random;
    ViewBoard board;

    PrintFullUITest(){
        setUp();
    }

    void fillHandRandomly(ViewPlayerHand hand){
        hand.setStartCard(getRandomStartingCard());
        hand.setCards(getRandomPlayCardList(3, true));
        hand.setSecretObjectiveCards(getRandomObjectiveCardList());
    }
    void fillPlayAreaRandomly(ViewPlayArea playArea){
        playArea.placeStarting(getRandomStartingCard());
        Queue<ViewPlaceableCard> cards = new LinkedList<>(getRandomCards(40, false));
        for(ViewPlaceableCard card : cards){
            int randomCornerIdx = random.nextInt(playArea.getFreeCorners().size());
            ViewCorner corner = playArea.getFreeCorners().get(randomCornerIdx);
            Point position = corner.getCardRef().getPosition().move(corner.getDirection());
            playArea.placeCard(position, card);
        }
    }

    @BeforeEach
    void setUp(){
        random = new Random();
        board = new ViewBoard("Test_Player");

        ViewPlayerHand myHand = board.getPlayerHand();
        fillHandRandomly(myHand);

        ViewPlayArea myPlayArea = board.getPlayerAreas().get(board.getPlayerHand().getNickname());
        fillPlayAreaRandomly(myPlayArea);

        printPlayerUI = new PrintPlayerUI(myHand, myPlayArea);
    }

    static void cls(){
        //TODO: delete \n screen before release (needed for IDE console cls)
        System.out.print("\n".repeat(50)); // cls for IDE

        //source: https://stackoverflow.com/questions/2979383/how-to-clear-the-console-using-java
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        }catch (Exception ignore){}
    }
    static void printCommandLegend(){
        System.out.println("Valid commands: ");
        System.out.print("-\t Move <up|right|left|down|center>");
        System.out.println("\t\t-\t Flip <1-3 | cardID | starting>");
        System.out.print("-\t Legend (shows this list)");
        System.out.println("\t\t\t\t-\t Flush <hand | objective | starting>");
    }
    public static void main(String[] args) {
        System.out.println("TEST UI movement");
        PrintFullUITest test = new PrintFullUITest();
        Scanner scanner = new Scanner(System.in);

        String input = ""; String error = "";
        test.printPlayerUI.printUI();
        while (!input.matches("[qQ]|[qQ]uit")){
            printCommandLegend();
            System.out.print("Input command: ");
            input = scanner.nextLine();
            if(input.matches("[mM]ove [a-zA-Z]+")){
                String direction = input.substring("move ".length());
                cls();
                switch (direction.toLowerCase()){
                    case "up": test.printPlayerUI.moveView(TL,TR); break;
                    case "right": test.printPlayerUI.moveView(BR,TR); break;
                    case "left": test.printPlayerUI.moveView(TL,BL); break;
                    case "down": test.printPlayerUI.moveView(BL,BR); break;
                    case "center": test.printPlayerUI.setCenter(0,0); break;
                    default: error = "Invalid move direction."; break;
                }
            }
            else if(input.matches("[fF]lip [1-3]")){
                int index = Integer.parseInt(input.substring("flip ".length()));
                try{ test.board.getPlayerHand().flipCard(index); }
                catch (IndexOutOfBoundsException e){ error = "Index too high!"; }
                if(error.isEmpty()) {
                    cls();
                    test.printPlayerUI.printUI();
                }
            }
            else if(input.matches("[fF]lip [rRgG][0-9]?[0-9]|[fF]lip [sS]tarting")){
                try{ test.board.getPlayerHand().flipCard(input.substring("flip ".length()).toUpperCase()); }
                catch (IllegalArgumentException e){ error = e.getMessage(); }
                if(error.isEmpty()) {
                    cls();
                    test.printPlayerUI.printUI();
                }
            }
            else if(input.matches("[lL]egend")){
                printCommandLegend();
            }
            else if(input.matches("[fF]lush [a-zA-Z]+")){
                String argument = input.substring("flush ".length());
                switch (argument) {
                    case "hand": test.board.getPlayerHand().setCards(new LinkedList<>()); break;
                    case "objective": test.board.getPlayerHand().setSecretObjectiveCards(new LinkedList<>()); break;
                    case "starting": test.board.getPlayerHand().clearStartCard(); break;
                    default: error = "Invalid flush argument"; break;
                }
                if(error.isEmpty()) {
                    cls();
                    test.printPlayerUI.printUI();
                }
            }
            else error = "Invalid command.";

            if(!error.isEmpty()) {
                cls();
                test.printPlayerUI.printUI();
                System.out.println(RED_TEXT + error + RESET);
                error = "";
            }
        }
    }
}
