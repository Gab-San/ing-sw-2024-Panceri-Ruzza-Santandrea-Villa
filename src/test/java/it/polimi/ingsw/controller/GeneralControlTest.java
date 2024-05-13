package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.testclass.PuppetClient;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.enums.GamePhase;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GeneralControlTest {

    protected BoardController controller;
    protected Board board;
    protected Class<? extends GameState> nexStateClass;


    // public final String playerNickname = "Flavio";

    public void setUp(){
        controller = new BoardController("Flavio's Game");
        board = controller.getGameState().board;
    }

    public void creationStateSetup(){
        //this.setUp();
        controller.join("player 1", new PuppetClient());
        nexStateClass= JoinState.class;
    }

    public void joinStateSetup(int numOfPlayers){
        this.creationStateSetup();
        controller.setNumOfPlayers("player 1", numOfPlayers);
    }
    public void setupStateSetup(int numOfPlayers){
        this.joinStateSetup(numOfPlayers);
        joinUntilSetupState(numOfPlayers);
        nexStateClass= SetupState.class;
    }
    
    public void playStateSetup(int numOfPlayers){
        this.setupStateSetup(numOfPlayers);
        joinUntilSetupState(numOfPlayers);
    }
    
    public void endgameStateSetup(int numOfPlayers){
        this.playStateSetup(numOfPlayers);
        controller.join("Player 1", new PuppetClient());
    }
    
    public void joinUntilSetupState(int numOfPlayers){
        for(int j = 2; j <= numOfPlayers; j++){
            controller.join("Player " + j, new PuppetClient());
        }
    }
    public void chooseAllStarting(int numOfPlayers){
        for(int j = 2; j <= numOfPlayers; j++){
            controller.placeStartingCard("Player "+j, new Random().nextBoolean());
        }
    }

    public void chooseAllColors(int numOfPlayers){
        for(int j = 2; j <= numOfPlayers; j++){
            controller.chooseYourColor("Player " + j, board.getRandomAvailableColor());
        }
    }

    public void chooseAllSecretObjectives(int numOfPlayers){
        for(int j = 2; j <= numOfPlayers; j++){
            controller.chooseSecretObjective("Player " + j, new Random().nextInt(2)+1);
        }
    }
    public void setupUntilPlayState(int numOfPlayers){
        chooseAllStarting(numOfPlayers);
        chooseAllColors(numOfPlayers);
        chooseAllSecretObjectives(numOfPlayers);
    }
}
