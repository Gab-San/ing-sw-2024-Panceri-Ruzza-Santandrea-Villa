package it.polimi.ingsw.controller;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.PlayerColor;
import it.polimi.ingsw.model.exceptions.DeckInstantiationException;
import it.polimi.ingsw.server.VirtualClient;

import java.util.ArrayList;

public class BoardController {
    private GameState gameState;
    public BoardController (String gameID) throws DeckInstantiationException {
        this.gameState = new CreationState(new Board(gameID), this, new ArrayList<>());
    }

    public synchronized void join(String nickname, VirtualClient client)
            throws IllegalStateException, IllegalArgumentException{
        gameState.join(nickname, client);
    }

    public synchronized void setNumOfPlayers(String nickname, int num)
            throws IllegalStateException, IllegalArgumentException{
        gameState.setNumOfPlayers(nickname, num);
    }
    public void disconnect(String nickname)
            throws IllegalStateException, IllegalArgumentException{
        synchronized(gameState.disconnectingPlayers){
            gameState.disconnectingPlayers.add(nickname);
        }
        synchronized(this){
            gameState.disconnect(nickname);
        }
    }

    public synchronized void placeStartingCard(String nickname, boolean placeOnFront)
            throws IllegalStateException, IllegalArgumentException{
        gameState.placeStartingCard(nickname, placeOnFront);
    }
    public synchronized void chooseYourColor(String nickname, PlayerColor color)
            throws IllegalStateException, IllegalArgumentException{
        gameState.chooseYourColor(nickname, color);
    }

    public synchronized void chooseSecretObjective(String nickname, int choice)
            throws IllegalStateException{
        gameState.chooseSecretObjective(nickname, choice);
    }
    public synchronized void draw(String nickname, char deckFrom, int cardPos)
            throws IllegalStateException, IllegalArgumentException{
        gameState.draw(nickname, deckFrom, cardPos);
    }
    public synchronized void placeCard(String nickname, String cardID,Point cardPos,
                                       CornerDirection cornerDir, boolean placeOnFront) throws IllegalStateException, IllegalArgumentException{
        gameState.placeCard(nickname, cardID, cardPos, cornerDir, placeOnFront);
    }


    public synchronized void restartGame(String nickname, int numOfPlayers) throws IllegalStateException{
            gameState.restartGame(nickname, numOfPlayers);
    }

    //FIXME: In realtà tutte queste azioni si potrebbero mettere nella join di ciascuno stato
    public void replaceClient(String nickname, VirtualClient oldClient, VirtualClient newClient)
            throws IllegalStateException {
        synchronized (gameState.board){
            if(!gameState.board.containsPlayer(nickname))
                throw new IllegalStateException(nickname + " isn't connected to this game.");
            gameState.board.getGameInfo().removeClient(oldClient);
            gameState.board.getGameInfo().addClient(newClient);
            //TODO: push game update to the client that just reconnected
        }
    }

    public String getGameID(){
        return gameState.board.getGameInfo().getGameID();
    }

    synchronized GameState getGameState(){
        return gameState;
    }
    synchronized void setGameState(GameState nextState){
        gameState = nextState;
    }


}
