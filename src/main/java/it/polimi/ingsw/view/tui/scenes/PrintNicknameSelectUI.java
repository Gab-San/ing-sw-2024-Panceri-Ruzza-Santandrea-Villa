package it.polimi.ingsw.view.tui.scenes;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.Point;
import it.polimi.ingsw.view.Scene;

import java.io.PrintWriter;
import java.util.Scanner;

import static it.polimi.ingsw.view.tui.ConsoleTextColors.RED_TEXT;
import static it.polimi.ingsw.view.tui.ConsoleTextColors.RESET;

public class PrintNicknameSelectUI implements Scene {
    PrintWriter out;
    Scanner scanner;

    public PrintNicknameSelectUI(){
        out = new PrintWriter(System.out, true);
        scanner = new Scanner(System.in);
    }

    @Override
    public void displayError(String msg){
        out.println(RED_TEXT + msg + RESET);
    }

    @Override
    public void display() {
        String nickname = "";
        String regex = "[a-zA-Z0-9_]+";
        do {
            out.print("Choose your nickname: ");
            nickname = scanner.nextLine();
            if(nickname.matches(regex)){
                //TODO: attempt client.connect(nickname)
            }
            else displayError("Invalid nickname, only use letters, numbers and _.");
        }while (!nickname.matches(regex));

    }

    @Override
    public void moveView(CornerDirection... cornerDirections) {
        display();
    }

    @Override
    public void setCenter(int row, int col) {
        display();
    }

    @Override
    public void setCenter(Point center) {
        display();
    }
}
