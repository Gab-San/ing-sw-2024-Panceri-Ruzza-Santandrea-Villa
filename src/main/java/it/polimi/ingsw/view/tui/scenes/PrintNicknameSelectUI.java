package it.polimi.ingsw.view.tui.scenes;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.Point;
import it.polimi.ingsw.view.tui.TUI_Scene;

import java.io.PrintWriter;
import java.util.Scanner;

import static it.polimi.ingsw.view.tui.ConsoleTextColors.RED_TEXT;
import static it.polimi.ingsw.view.tui.ConsoleTextColors.RESET;

public class PrintNicknameSelectUI extends TUI_Scene {

    public PrintNicknameSelectUI(){
        super();
    }

    @Override
    public void print() {
        if(notificationBacklog != null)
            notificationBacklog.forEach(out::println);
        out.print("Choose your nickname: ");
        out.flush();
    }
    @Override
    public void displayError(String msg){
        synchronized (System.out){
            cls();
            out.println(RED_TEXT + msg + RESET);
            print();
        }
    }
}
