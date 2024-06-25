package it.polimi.ingsw.view.model;

import it.polimi.ingsw.stub.StubView;
import it.polimi.ingsw.view.ViewBoardGenerator;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.fail;

public class ViewPlayAreaLayersTest {
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 5, 10, 20, 40, 80})
    //size does not include the starting card
    //80 is well above the maximum reachable size (~40) of the playArea in a normal game
    void testZLayer(int size){
        ViewBoard board = new ViewBoard(new StubView());
        board.addLocalPlayer("TEST_LOCAL");
        ViewPlayArea playArea = board.getPlayerArea("TEST_LOCAL");
        ViewBoardGenerator.fillPlayAreaRandomly(playArea, size);
        playArea.calculateZLayers();
    }


//    use this to test repeatedly (eliminate randomness from fillPlayAreaRandomly)
//    @RepeatedTest(10000)
//    void testZLayerRepeated(){
//        int[] sizes = new int[]{
//                1, 2, 5, 10, 20,
//                50, 80, 200, 400
//        };
//
//        for(int i : sizes){
//            try{
//                testZLayer(i);
//            }catch (IllegalArgumentException e){
//                e.printStackTrace();
//                fail("\nFAILED AT size = " + i + " \n" + e.getMessage());
//            }
//        }
//    }
}
