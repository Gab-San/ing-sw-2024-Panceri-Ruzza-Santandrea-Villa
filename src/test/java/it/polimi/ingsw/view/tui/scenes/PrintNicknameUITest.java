package it.polimi.ingsw.view.tui.scenes;

import it.polimi.ingsw.view.Scene;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PrintNicknameUITest {
    Scene nicknameUI;

    @BeforeEach
    void setUp(){
        nicknameUI = new PrintNicknameSelectUI();
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
        nicknameUI.displayNotification("Notify test.");
    }
    @Test
    void printAll(){
        printUI();
        printNotification();
        printError();
    }
}
