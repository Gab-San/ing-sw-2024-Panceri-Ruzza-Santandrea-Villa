package it.polimi.ingsw.view.tui.scenes;

import it.polimi.ingsw.view.Scene;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

public class PrintNicknameUITest {
    Scene nicknameUI;
    List<String> backlog;

    @BeforeEach
    void setUp(){
        nicknameUI = new PrintNicknameSelectUI();
        backlog = new LinkedList<>();
    }

    @Test
    void printUI(){
        nicknameUI.display();
    }
    @Test
    void printError(){
        nicknameUI.displayError("Error test.");
    }
    @Test
    void printNotification(){
        backlog.add("Notify test.");
        nicknameUI.displayNotification(backlog);
    }
    @Test
    void printAll(){
        printUI();
        printNotification();
        printError();
    }
}
