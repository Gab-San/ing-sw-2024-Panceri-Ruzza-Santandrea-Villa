package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.view.model.*;
import it.polimi.ingsw.view.model.cards.*;

import java.util.*;

import static it.polimi.ingsw.CornerDirection.*;
import static it.polimi.ingsw.view.ViewBoardGenerator.*;
import static it.polimi.ingsw.view.tui.ConsoleTextColors.*;

public class PrintFullUITest {
    final PrintPlayerUI printPlayerUI;
    final Map<String, PrintOpponentUI> printOpponentUIMap;
    final PrintBoardUI printBoardUI;
    final Random random;
    final ViewBoard board;

    PrintFullUITest(){
        random = new Random();
        board = new ViewBoard("Test_Player");

        fillDeckRandomly(board.getResourceCardDeck(), ViewResourceCard.class);
        fillDeckRandomly(board.getGoldCardDeck(), ViewGoldCard.class);
        fillDeckRandomly(board.getObjectiveCardDeck(), ViewObjectiveCard.class);
        printBoardUI = new PrintBoardUI(board);

        ViewPlayerHand myHand = board.getPlayerHand();
        fillHandRandomly(myHand);
        ViewPlayArea myPlayArea = board.getPlayerArea(board.getPlayerHand().getNickname());
        fillPlayAreaRandomly(myPlayArea);
        board.setScore(myHand.getNickname(), getRandomScore());

        printOpponentUIMap = new Hashtable<>();
        for(String opponentNickname : new String[]{"Player2", "Player3", "Player4"}){
            board.addPlayer(opponentNickname);
            board.setScore(opponentNickname, getRandomScore());
            fillHandRandomly(board.getOpponentHand(opponentNickname));
            fillPlayAreaRandomly(board.getPlayerArea(opponentNickname));
            PrintOpponentUI printOpponentUI = new PrintOpponentUI(board.getOpponentHand(opponentNickname), board.getPlayerArea(opponentNickname));
            printOpponentUIMap.put(opponentNickname, printOpponentUI);
        }
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
        System.out.println("\t\t\t\t-\t Flip <1-3 | cardID | starting>");
        System.out.print("-\t View <me | board | any player nickname>");
        //System.out.print("-\t Legend (shows this list)");
        System.out.println("\t\t-\t Flush <hand | objective | starting>");
    }
    public static void main(String[] args) {
        System.out.println("TEST FULL UI");
        PrintFullUITest test = new PrintFullUITest();
        Scanner scanner = new Scanner(System.in);

        String input = ""; String error = "";
        Scene printUI = test.printPlayerUI;
        printUI.display();
        while (!input.matches("[qQ]|[qQ]uit")){
            printCommandLegend();
            System.out.print("Input command: ");
            input = scanner.nextLine();

            if(printUI == test.printBoardUI && input.matches("[mM]ove [a-zA-Z]+")){
                error = "You can't move on the Board UI";
            }
            else if(printUI != test.printPlayerUI && input.matches("[fF]lip [a-zA-Z0-9]+|[fF]lush [a-zA-Z]+")){
                error = "You must be on your UI to flush or flip cards";
            }
            else if(input.matches("[mM]ove [a-zA-Z]+")){
                String direction = input.substring("move ".length());
                cls();
                switch (direction.toLowerCase()){
                    case "up": printUI.moveView(TL,TR); break;
                    case "right": printUI.moveView(BR,TR); break;
                    case "left": printUI.moveView(TL,BL); break;
                    case "down": printUI.moveView(BL,BR); break;
                    case "center": printUI.setCenter(0,0); break;
                    default: error = "Invalid move direction."; break;
                }
            }
            else if(input.matches("[fF]lip [1-3]")){
                int index = Integer.parseInt(input.substring("flip ".length()));
                try{ test.board.getPlayerHand().flipCard(index); }
                catch (IndexOutOfBoundsException e){ error = "Index too high!"; }
                if(error.isEmpty()) {
                    cls();
                    printUI.display();
                }
            }
            else if(input.matches("[fF]lip [rRgGsS][0-9]?[0-9]|[fF]lip [sS]tarting")){
                try{ test.board.getPlayerHand().flipCard(input.substring("flip ".length()).toUpperCase()); }
                catch (IllegalArgumentException e){ error = e.getMessage(); }
                if(error.isEmpty()) {
                    cls();
                    printUI.display();
                }
            }
//            else if(input.matches("[lL]egend")){
//                printCommandLegend();
//            }
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
                    printUI.display();
                }
            }
            else if(input.matches("[vV]iew [a-zA-Z0-9]+")){
                String argument = input.substring("view ".length());
                switch (argument.toLowerCase()) {
                    case "me": printUI = test.printPlayerUI; break;
                    case "board": printUI = test.printBoardUI; break;
                    default:
                        if(test.printOpponentUIMap.get(argument) != null){
                            printUI = test.printOpponentUIMap.get(argument);
                        }
                        else error = "Invalid view argument";
                        break;
                }
                if(error.isEmpty()) {
                    cls();
                    printUI.display();
                }
            }
            else error = "Invalid command.";

            if(!error.isEmpty()) {
                cls();
                printUI.display();
                System.out.println(RED_TEXT + error + RESET);
                error = "";
            }
        }
    }
}
