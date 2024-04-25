package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.StartingCard;

public class BoardController {
    private GameState gameState;
    public BoardController (String gameID){
        this.gameState=new JoinState(new Board(gameID));
    }
    public BoardController (String gameID, Player... players){
        this.gameState=new JoinState(new Board(gameID, players));
    }
    public void join (String nickname, String gameID) throws Exception{
        this.gameState.join(nickname, gameID);
    }
    public void disconnect (String nickname, VirtualClient client) throws Exception{
        this.gameState.disconnect(nickname, client);
    }
    void startGame () throws Exception{
        this.gameState.startGame();
    }
    public void placeStartingCard(String nickname, StartingCard card, Boolean placeOnFront) throws Exception{
        this.gameState.placeStartingCard(nickname, card, placeOnFront);
    }
    public void chooseSecreteObjective (String nickname, ObjectiveCard card, Boolean placeOnFront) throws Exception{
        this.gameState.chooseSecreteObjective(nickname, card, placeOnFront);
    }
    public void draw (String nickname, int deck, int card) throws Exception{
        this.gameState.draw(nickname, deck, card);
    }
    public void placeCard (String nickname, PlayCard card, Corner corner) throws Exception{
        this.gameState.placeCard(nickname, card, corner);
    }
    public GameState nextState () throws Exception{
        return this.gameState.nextState();
    }
}
