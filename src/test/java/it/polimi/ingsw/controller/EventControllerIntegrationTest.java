package it.polimi.ingsw.controller;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.PlayerColor;
import it.polimi.ingsw.stub.StubClient;
import it.polimi.ingsw.stub.StubView;
import org.junit.jupiter.api.RepeatedTest;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventControllerIntegrationTest {

    StubView view = new StubView();
    StubClient[] clients = new StubClient[4];
    BoardController controller = new BoardController();

    private Point getRandomPoint(){
        Random random = new Random();
        return new Point(random.nextInt(80), random.nextInt(80));
    }

    private CornerDirection getRandomDir(){
        return CornerDirection.values()[new Random().nextInt(4)];
    }

    @RepeatedTest(100)
    void creationStateTest(){
        String nick = "Gamba";
        clients[0] = new StubClient(nick, view);
        controller.join(nick, clients[0]);
        try {
            controller.restartGame(nick, 2);
        } catch (IllegalStateException e){
            assertEquals(clients[0].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }

        try {
            controller.placeCard("Gamba", "S0", getRandomPoint(), getRandomDir(), new Random().nextBoolean());
        } catch (IllegalStateException e){
            assertEquals(clients[0].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }


        try{
            controller.placeStartingCard(nick, true);
        } catch (IllegalStateException e){
            assertEquals(clients[0].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }

        try{
            controller.chooseYourColor(nick, PlayerColor.BLUE);
        } catch (IllegalStateException e){
            assertEquals(clients[0].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }

        try{
            controller.chooseSecretObjective(nick, 1);
        } catch (IllegalStateException e){
            assertEquals(clients[0].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }

        try{
            controller.draw(nick, 'R', 0);
        } catch (IllegalStateException e){
            assertEquals(clients[0].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }

        try{
            controller.setNumOfPlayers(nick, 1);
        } catch (IllegalArgumentException e){
            assertEquals(clients[0].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }

        try{
            controller.setNumOfPlayers(nick, 5);
        } catch (IllegalArgumentException e){
            assertEquals(clients[0].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }

        String notConnectedNick = "JOJO";

        try{
            controller.setNumOfPlayers(notConnectedNick, 2);
        } catch (IllegalArgumentException e){
            assertEquals(clients[0].reportedError.split(":")[1].trim(), e.getMessage().toUpperCase());
        }
    }
}
