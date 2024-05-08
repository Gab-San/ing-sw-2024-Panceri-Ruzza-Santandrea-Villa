package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;


public class JoinStateTest {
    Board board;
    GameState creationState;
    GameState joinState;
    GameState nextGS;
    Player[] players;
    @BeforeEach
    public void Setup(){
        players[0]=new Player("Player1");
        board=new Board("TestCreationState", players[0]);
        creationState=new CreationState(board);
        nextGS=new JoinState(new Board("ignore"));
    }

    @ParameterizedTest
    @ValueSource(ints={2,3,4})
    public void ConstructorTest(int i){
        joinState= creationState.setNumOfPlayers(players[0].getNickname(), i);
        for(int j=2; j<1+i; j++)
            nextGS=joinState.join("Player"+j, null);
        //assertEquals(nextGS.getClass(),);
    }
    @ParameterizedTest
    @ValueSource(ints={2,3,4})
    public void JoinTest(){}
}
