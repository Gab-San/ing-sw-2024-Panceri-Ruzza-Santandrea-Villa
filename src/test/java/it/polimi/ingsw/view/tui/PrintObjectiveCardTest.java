package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.view.ViewCardGenerator;
import it.polimi.ingsw.view.model.cards.ViewObjectiveCard;
import it.polimi.ingsw.view.tui.PrintCard;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class PrintObjectiveCardTest {
    static PrintCard printCard;

    @BeforeAll
    static void setUp(){
        printCard = new PrintCard();
    }

    @RepeatedTest(4)
    void patternObjectivePrintTest(){
        ViewObjectiveCard objectiveCard = ViewCardGenerator.getRandomObjectiveCard(true);
        objectiveCard.turnFaceUp();
        printCard.printCard(objectiveCard);
    }
    @RepeatedTest(4)
    void resourceObjectivePrintTest(){
        ViewObjectiveCard objectiveCard = ViewCardGenerator.getRandomObjectiveCard(false);
        objectiveCard.turnFaceUp();
        printCard.printCard(objectiveCard);
    }
    @RepeatedTest(4)
    void objectiveBackPrintTest(){
        ViewObjectiveCard objectiveCard = ViewCardGenerator.getRandomObjectiveCard(new Random().nextBoolean());
        objectiveCard.turnFaceDown();
        printCard.printCard(objectiveCard);
    }
}
