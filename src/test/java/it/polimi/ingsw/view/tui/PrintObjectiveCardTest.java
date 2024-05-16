package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.view.ViewCardGenerator;
import it.polimi.ingsw.view.model.cards.ViewObjectiveCard;
import it.polimi.ingsw.view.tui.PrintCard;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

public class PrintObjectiveCardTest {
    static PrintCard printCard;

    @BeforeAll
    static void setUp(){
        printCard = new PrintCard();
    }

    @RepeatedTest(4)
    void patternObjectivePrintTest(){
        ViewObjectiveCard objectiveCard = ViewCardGenerator.getRandomObjectiveCard(true);
        printCard.printCard(objectiveCard);
    }
    @RepeatedTest(4)
    void resourceObjectivePrintTest(){
        ViewObjectiveCard objectiveCard = ViewCardGenerator.getRandomObjectiveCard(false);
        printCard.printCard(objectiveCard);
    }
}
