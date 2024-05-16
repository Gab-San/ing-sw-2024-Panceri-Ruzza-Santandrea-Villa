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
        System.out.print("\n".repeat(50)); // cls equivalent
    }
    static void printCommandLegend(){
        System.out.println("Valid commands: ");
        System.out.println("-\t Move <up|right|left|down|center>");
        System.out.println("-\t Flip <1-3>");
        System.out.println("-\t Flip <cardID | starting>");
    }
    public static void main(String[] args) {
        System.out.println("TEST UI movement");
        PrintFullUITest test = new PrintFullUITest();
        Scanner scanner = new Scanner(System.in);

        String input = ""; String error = "";
        test.printPlayerUI.printUI();

        while (!input.matches("[qQ]|[qQ]uit")){
            System.out.print("Input command \"Move [direction]\": ");
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
                test.board.getPlayerHand().flipCard(index);
                cls();
                test.printPlayerUI.printUI();
            }
            else if(input.matches("[fF]lip [RG][1-40]|[fF]lip [sS]tarting")){
                test.board.getPlayerHand().flipCard(input.substring("flip ".length()));
                cls();
                test.printPlayerUI.printUI();
            }
            else if(input.matches("[lL]egend")){
                printCommandLegend();
            }
            else error = "Invalid command.";

            if(!error.isEmpty()) {
                test.printPlayerUI.printUI();
                System.out.println(RED_TEXT + error + RESET);
                error = "";
            }
        }
    }
}
