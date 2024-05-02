package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.StartingCard;
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
            this.gameState = new JoinState(new Board(gameID, players));
        }
    }

    public void join(String nickname, VirtualClient client) throws IllegalStateException{
        synchronized (this) {
            gameState=gameState.join(nickname, client);
        }
    }
    public void disconnect(String nickname, VirtualClient client) throws IllegalStateException{
        synchronized (this) {
            gameState.disconnect(nickname, client);
        }
    }
    void startGame(String nickname) throws IllegalStateException{
        synchronized (this) {
            gameState = gameState.startGame(nickname);
        }
    }
    public void placeStartingCard(String nickname, boolean placeOnFront) throws IllegalStateException{
        synchronized (this) {
            gameState.placeStartingCard(nickname, placeOnFront);
        }
    }
    public void chooseSecretObjective(String nickname, int choice) throws IllegalStateException{
        synchronized (this) {
            gameState=gameState.chooseSecretObjective(nickname, choice);
        }
    }
    public void draw(String nickname, int deck, int card) throws IllegalStateException{
        synchronized (this) {
            gameState = gameState.draw(nickname, deck, card);
        }
    }
    public void placeCard(String nickname, PlayCard card, Corner corner) throws IllegalStateException{
        synchronized (this) {
            gameState.placeCard(nickname, card, corner);
        }
    }

    public void replaceClient(String nickname, VirtualClient oldClient, VirtualClient newClient) throws IllegalStateException {
        synchronized (gameState.board){
            if(gameState.board.containsPlayer(nickname))
                throw new IllegalStateException(nickname + " isn't connected to this game.");
            gameState.board.getGameInfo().removeClient(oldClient);
            gameState.board.getGameInfo().addClient(newClient);
            //TODO: push game update to the client that just reconnected
        }
    }
}
