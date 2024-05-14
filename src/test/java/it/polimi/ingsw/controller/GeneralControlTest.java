package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.testclass.PuppetClient;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enums.GamePhase;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GeneralControlTest {
    protected BoardController controller;
    protected Board board;
    protected Class<? extends GameState> nextStateClass;
    protected List<String> allPlayers;

    public void setUp(){
        controller = new BoardController("Test Game");
        board = controller.getGameState().board;
    }

    public void creationStateSetup(){
        //this.setUp();
        controller.join("player 1", new PuppetClient());
        nextStateClass= JoinState.class;
        allPlayers=board.getPlayerAreas().keySet().stream().map(Player::getNickname).collect(Collectors.toList());
    }

    public void joinStateSetup(int numOfPlayers){
        this.creationStateSetup();
        controller.setNumOfPlayers("player 1", numOfPlayers);
        nextStateClass= JoinState.class;
        allPlayers=board.getPlayerAreas().keySet().stream().map(Player :: getNickname).collect(Collectors.toList());
    }
    public void setupStateSetup(int numOfPlayers){
        this.joinStateSetup(numOfPlayers);
        joinUntilSetupState(numOfPlayers);
        nextStateClass= SetupState.class;
        allPlayers=board.getPlayerAreas().keySet().stream().map(Player::getNickname).collect(Collectors.toList());
    }
    
    public void playStateSetup(int numOfPlayers){
        this.setupStateSetup(numOfPlayers);
        setupUntilPlayState(numOfPlayers);
        nextStateClass= PlayState.class;
        allPlayers=board.getPlayerAreas().keySet().stream().map(Player::getNickname).collect(Collectors.toList());
    }
    
    public void endgameStateSetup(int numOfPlayers){
        this.playStateSetup(numOfPlayers);
        playUntilEndgame(numOfPlayers);
        controller.join("Player 1", new PuppetClient());
        nextStateClass=null;
        allPlayers=board.getPlayerAreas().keySet().stream().map(Player::getNickname).collect(Collectors.toList());
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
    public void playUntilEndgame(int numOfPlayers){
        //TODO: must be implemented in someway
    }
}
