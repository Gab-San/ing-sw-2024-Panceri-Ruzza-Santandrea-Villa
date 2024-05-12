package it.polimi.ingsw.controller;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.PlayerColor;
import it.polimi.ingsw.model.exceptions.DeckException;
import it.polimi.ingsw.server.VirtualClient;

import java.util.List;

public abstract class GameState {
    protected final Board board;
    protected final BoardController controller;
    protected final List<String> disconnectingPlayers;
    //FIXME Reimplementare il thread che controlla i disconnecting players.
    // Va tutto gestito sul gamestate, quindi per esempio si può riscrivere
    // la funzione di nextState, TENENDO CONTO DEI CAMBIAMENTI FATTI e sostituire
    // la chiamata a nextState() con l'avvio del thread (magari utilizzando un executor)
    // e mettere un Thread.sleep di qualche secondo e poi controllare se sia vuota la coda
    // delle disconnesioni.
    /* problema di metterlo all'interno*/
    /*[FLAVIO] da discutere, è necessario un thread che continui a controllare se si può passare allo stato successivo
    * per via delle cadute di connessioni, avevo preferito metterlo nel BoardController per evirtare
    * di ricrearlo ad ogni stato, anche perché devo avere il return del cambio di stato in caso che
    * risulti che debba cambiare stato, in ogni caso dovrebbe essere un thread deamon
    * esempio: nel setupState se un giocatore ha una caduta di connessione posso decidere se aspettarlo o andare
    * al PlayState quando è tempo e poi tornare al SetupStare quando si riconnette.
    * Per il PlayState è la stessa cosa, se il gioco finisce (last turn of last player)durante il place del currPlayer
    * che ha caduta di connessione subito dopo e quindi non può terminare il turno con una draw (perché disconnesso),
    * bisogna passare all'EndgameState, senza reale necessità di aspettare che si riconnetti*/
    public GameState(Board board, BoardController controller, List<String> disconnectingPlayers) {
        this.board = board;
        this.controller = controller;
        this.disconnectingPlayers = disconnectingPlayers;
        //aggiungo il thread per next state
        new Thread().setDaemon(true);
    }

    abstract public void join (String nickname, VirtualClient client) throws IllegalStateException;
    abstract public void setNumOfPlayers(String nickname, int num) throws IllegalStateException, IllegalArgumentException;
    abstract public void disconnect (String nickname, VirtualClient client) throws IllegalStateException, IllegalArgumentException;
    abstract public void placeStartingCard(String nickname, boolean placeOnFront) throws IllegalStateException, IllegalArgumentException;
    abstract public void chooseYourColor(String nickname, PlayerColor color) throws IllegalStateException, IllegalArgumentException, InterruptedException, DeckException;
    abstract public void chooseSecretObjective(String nickname, int choice) throws IllegalStateException, IllegalArgumentException;
    abstract public void placeCard(String nickname, String cardID, Point cardPos, CornerDirection cornerDir, boolean placeOnFront) throws IllegalStateException, IllegalArgumentException;
    abstract public void draw (String nickname, char deckFrom, int cardPos) throws IllegalStateException, IllegalArgumentException;
    abstract public void startGame (String nickname, int numOfPlayers) throws IllegalStateException, IllegalArgumentException;
    protected void transition(GameState nextState){
        controller.setGameState(nextState);
    }
    protected boolean isDisconnecting(String nickname){
        return disconnectingPlayers.contains(nickname);
    }
}
