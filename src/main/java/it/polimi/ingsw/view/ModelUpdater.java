package it.polimi.ingsw.view;

import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.view.model.ViewBoard;
import it.polimi.ingsw.view.model.ViewOpponentHand;

// synchronized is on all model methods
public class ModelUpdater {
    private final ViewBoard board;

    public ModelUpdater(ViewBoard board) {
        this.board = board;
    }
    //TODO: UI synchronize and refresh on all update methods

    void createPlayer(String nickname, boolean isConnected, int turn, PlayerColor color){
        board.addPlayer(nickname);
        ViewOpponentHand hand = board.getOpponentHand(nickname);
        hand.setConnected(isConnected);
        hand.setTurn(turn);
        hand.setColor(color);
    }
    void updatePlayer(String nickname, PlayerColor color){
        if(board.getPlayerHand().getNickname().equals(nickname))
            board.getPlayerHand().setColor(color);
        else board.getOpponentHand(nickname).setColor(color);
    }
    void updatePlayer(String nickname, int playerTurn){
        if(board.getPlayerHand().getNickname().equals(nickname))
            board.getPlayerHand().setTurn(playerTurn);
        else board.getOpponentHand(nickname).setTurn(playerTurn);
    }
    void updatePlayer(String nickname, boolean isConnected){
        if(!board.getPlayerHand().getNickname().equals(nickname))
            board.getOpponentHand(nickname).setConnected(isConnected);
    }
}
