package it.polimi.ingsw.view;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.Point;

import java.io.PrintWriter;

import static it.polimi.ingsw.view.tui.ConsoleTextColors.RED_TEXT;
import static it.polimi.ingsw.view.tui.ConsoleTextColors.RESET;

public interface Scene {
    default void cls(){
        boolean runningIntelliJ = false;
        try {
            runningIntelliJ = Scene.class.getClassLoader().loadClass("com.intellij.rt.execution.application.AppMainV2") != null;
        } catch (ClassNotFoundException ignored) {}

        if(runningIntelliJ) System.out.print("\n".repeat(50));
        else{
            System.out.print("\033[H\033[2J");
            //FIXME: [Ale] choose which cls to use
            //source: https://stackoverflow.com/questions/2979383/how-to-clear-the-console-using-java
            try {
                if (System.getProperty("os.name").contains("Windows"))
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                else Runtime.getRuntime().exec("clear");
            } catch (Exception ignored) {}
        }
        System.out.flush();
    }
    void display();
    void displayError(String msg);
    void moveView(CornerDirection ...cornerDirections);
    void setCenter(int row, int col);
    void setCenter(Point center);
}