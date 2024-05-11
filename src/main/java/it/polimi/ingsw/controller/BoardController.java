package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.Point;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.PlayerColor;
import it.polimi.ingsw.model.exceptions.DeckException;
import it.polimi.ingsw.model.exceptions.DeckInstantiationException;
import it.polimi.ingsw.server.VirtualClient;

public class BoardController {
    private GameState gameState;
    public BoardController (String gameID) throws DeckInstantiationException {
        synchronized (this) {
            this.gameState = new CreationState(new Board(gameID));
        }
    }
    public BoardController (String gameID, Player... players) throws DeckInstantiationException {
        synchronized (this) {
            this.gameState = new JoinState(new Board(gameID, players), players.length);
        }
    }

    public void join(String nickname, VirtualClient client) throws IllegalStateException, IllegalArgumentException{
        synchronized (this) {
            gameState=gameState.join(nickname, client);
        }
    }

    public void setNumOfPlayers(String nickname, int num) throws IllegalStateException, IllegalArgumentException{
        synchronized (this){
            gameState = gameState.setNumOfPlayers(nickname, num);
        }
    }
    public void disconnect(String nickname, VirtualClient client) throws IllegalStateException, IllegalArgumentException{
        synchronized (this) {
            gameState.disconnect(nickname, client);
        }
    }

    public void placeStartingCard(String nickname, boolean placeOnFront) throws IllegalStateException, IllegalArgumentException{
        synchronized (this) {
            gameState.placeStartingCard(nickname, placeOnFront);
        }
    }
    public void chooseYourColor(String nickname, PlayerColor color) throws IllegalStateException, IllegalArgumentException, DeckException, InterruptedException{
        synchronized (this){
            gameState.chooseYourColor(nickname, color);
        }
    }

    public void chooseSecretObjective(String nickname, int choice) throws IllegalStateException{
        synchronized (this) {
            gameState=gameState.chooseSecretObjective(nickname, choice);
        }
    }
    public void draw(String nickname, char deckFrom, int cardPos) throws IllegalStateException{
        synchronized (this) {
            gameState = gameState.draw(nickname, deckFrom, cardPos);
        }
    }
    public void placeCard(String nickname, String cardID,Point cardPos, CornerDirection cornerDir, boolean placeOnFront) throws IllegalStateException{
        synchronized (this) {
            gameState = gameState.placeCard(nickname, cardID, cardPos, cornerDir, placeOnFront);
        }
    }

    void startGame(String nickname, int numOfPlayers) throws IllegalStateException{
        synchronized (this) {
            gameState = gameState.startGame(nickname, numOfPlayers);
        }
    }

    public void replaceClient(String nickname, VirtualClient oldClient, VirtualClient newClient) throws IllegalStateException {
        synchronized (gameState.board){
            if(!gameState.board.containsPlayer(nickname))
                throw new IllegalStateException(nickname + " isn't connected to this game.");
            gameState.board.getGameInfo().removeClient(oldClient);
            gameState.board.getGameInfo().addClient(newClient);
            //TODO: push game update to the client that just reconnected
        }
    }
}
