package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.view.ViewCardGenerator;
import it.polimi.ingsw.view.model.cards.ViewObjectiveCard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;

import java.util.Random;

public class PrintObjectiveCardTest {
    static PrintCard printCard;
    static final int REPETITIONS = 8;

    @BeforeAll
    static void setUp(){
        printCard = new PrintCard();
    }
    @AfterEach
    void println(){
        System.out.println();
    }

    @RepeatedTest(REPETITIONS)
    void patternObjectivePrintTest(){
        ViewObjectiveCard objectiveCard = ViewCardGenerator.getRandomObjectiveCard(true);
        objectiveCard.turnFaceUp();
        printCard.printCard(objectiveCard);
    }
    @RepeatedTest(REPETITIONS)
    void resourceObjectivePrintTest(){
        ViewObjectiveCard objectiveCard = ViewCardGenerator.getRandomObjectiveCard(false);
        objectiveCard.turnFaceUp();
        printCard.printCard(objectiveCard);
    }
    @RepeatedTest(REPETITIONS)
    void objectiveBackPrintTest(){
        ViewObjectiveCard objectiveCard = ViewCardGenerator.getRandomObjectiveCard(new Random().nextBoolean());
        objectiveCard.turnFaceDown();
        printCard.printCard(objectiveCard);
    }
}
