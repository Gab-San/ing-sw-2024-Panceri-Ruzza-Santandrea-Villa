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
    private int numberOfPlayer;
    public BoardController (String gameID) throws DeckInstantiationException {
            this.gameState = new CreationState(new Board(gameID), this);
    }
    public BoardController (String gameID, Player... players) throws DeckInstantiationException {
            this.gameState = new JoinState(new Board(gameID, players), this, players.length);
    }

    public synchronized void join(String nickname, VirtualClient client)
            throws IllegalStateException, IllegalArgumentException{
        gameState.join(nickname, client);
    }

    public synchronized void setNumOfPlayers(String nickname, int num)
            throws IllegalStateException, IllegalArgumentException{
        gameState.setNumOfPlayers(nickname, num);
    }

    public void disconnect(String nickname, VirtualClient client)
            throws IllegalStateException, IllegalArgumentException{
        gameState.disconnect(nickname, client);

    }

    public void placeStartingCard(String nickname, boolean placeOnFront)
            throws IllegalStateException, IllegalArgumentException{
        gameState.placeStartingCard(nickname, placeOnFront);
    }
    public void chooseYourColor(String nickname, PlayerColor color)
            throws IllegalStateException, IllegalArgumentException, DeckException, InterruptedException{
        gameState.chooseYourColor(nickname, color);
    }

    public void chooseSecretObjective(String nickname, int choice)
            throws IllegalStateException{
        gameState.chooseSecretObjective(nickname, choice);
    }
    public synchronized void draw(String nickname, char deckFrom, int cardPos)
            throws IllegalStateException{
        gameState.draw(nickname, deckFrom, cardPos);
    }
    public synchronized void placeCard(String nickname, String cardID,Point cardPos,
                                       CornerDirection cornerDir, boolean placeOnFront) throws IllegalStateException{
        gameState.placeCard(nickname, cardID, cardPos, cornerDir, placeOnFront);
    }

    synchronized void startGame(String nickname, int numOfPlayers) throws IllegalStateException{
            gameState.startGame(nickname, numOfPlayers);
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
        // don't need to synchronize on board as all fields accessed are final
        return gameState.board.getGameInfo().getGameID();
    }

    //TODO: delete this test method when we're done
    public synchronized void testPrint(String text) throws IllegalStateException {
        System.out.println("Function call received " + text);
        if (text.toLowerCase().contains("throw")) {
            System.out.println("THROWING ILLEGAL STATE TEST");
            System.out.flush();
            throw new IllegalStateException("Testing IllegalStateException thrown in queue thread.");
        }
    }

    void setPlayerNumber(int numberOfPlayer){
        this.numberOfPlayer = numberOfPlayer;
    }
    int getPlayerNumber(){
        return numberOfPlayer;
    }
    void setGameState(GameState nextState){
        gameState = nextState;

    }
}
