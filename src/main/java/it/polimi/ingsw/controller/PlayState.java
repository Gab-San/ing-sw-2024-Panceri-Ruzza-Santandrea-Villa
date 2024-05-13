package it.polimi.ingsw.controller;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.PlayArea;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.PlayerColor;
import it.polimi.ingsw.model.exceptions.DeckException;
import it.polimi.ingsw.server.VirtualClient;

import java.util.List;

public class PlayState extends GameState {
    private boolean lastRound;
    private boolean currentPlayerHasPlacedCard;

    public PlayState(Board board, BoardController controller, List<String> disconnectingPlayers) {
        super(board, controller, disconnectingPlayers);
        lastRound = false;
        currentPlayerHasPlacedCard = false;
        board.setGamePhase(GamePhase.PLACECARD);
    }

    @Override
    public void join(String nickname, VirtualClient client) throws IllegalStateException {
        try{
            board.reconnectPlayer(nickname);
            //TODO resubscribe player's client to observers
            //   and push current state to client (possibly done in board.replaceClient())
        }catch (IllegalStateException e){
            throw new IllegalStateException("IMPOSSIBLE TO JOIN A GAME DURING SETUP STATE", e);
        }
    }

    @Override
    public void setNumOfPlayers(String nickname, int num) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO CHANGE THE NUMBER OF PLAYERS DURING PLAY STATE");
    }

    @Override
    public void disconnect(String nickname, VirtualClient client) throws IllegalStateException, IllegalArgumentException {
        //TODO implement disconnect

        Player player=board.getPlayersByTurn().get(board.getCurrentTurn());
        if(player.getNickname().equals(nickname)){
            if(board.nextTurn()) {
                currentPlayerHasPlacedCard = false;
                board.setGamePhase(GamePhase.PLACECARD);
            }
        }
        board.disconnectPlayer(nickname);
        //TODO unsubscribe player's client from observers
        //   and push current state to client (possibly done in board.replaceClient())
        if(board.getCurrentPlayer().getNickname().equals(nickname))
            postDrawChecks();
    }

    @Override
    public void placeStartingCard(String nickname, boolean placeOnFront) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO PLACE STARTING CARD DURING PLAY STATE");
    }
    public void chooseYourColor(String nickname, PlayerColor color) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO CHOOSE OR CHANGE YOUR COLOR DURING PLAY STATE");
    }

    @Override
    public void chooseSecretObjective(String nickname, int choice) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO CHOOSE SECRET OBJECTIVE DURING PLAY STATE");
    }

    @Override
    public void placeCard(String nickname, String cardID, Point cardPos, CornerDirection cornerDir, boolean placeOnFront) throws IllegalStateException, IllegalArgumentException {

        Player player = board.getPlayerByNickname(nickname); // throws if player isn't in game
        //FIXME: [Ale] Questo check dovrebbe essere inutile,
        //                  la phase è settata correttamente già in postDrawChecks()
        if (!player.getHand().isHandFull()) {
            //serve per quando un giocatore si disconnette e si riconnette in un successivo momento
            board.setGamePhase(GamePhase.DRAWCARD); //giocatore non ha tutte le carte in mano, allora deve pescare
            currentPlayerHasPlacedCard=true; //serve per poter pescare la carta: se non ho 3 carte in mano, allora ne ho piazzata almeno 1
            throw new IllegalStateException("IMPOSSIBLE TO PLACE A CARD IN THIS PHASE, YOUR HAND AIN'T FULL");
        }

        if(board.getGamePhase() != GamePhase.PLACECARD)
            throw new IllegalStateException("IMPOSSIBLE TO PLACE A CARD IN THIS PHASE");
        if (currentPlayerHasPlacedCard)
            throw new IllegalStateException("Player had already placed a card!");

        int currentTurn = board.getCurrentTurn();
        if(!board.getPlayersByTurn().get(currentTurn).equals(player))
            throw new IllegalStateException("It's not your turn to place the card yet");

        PlayArea playerPlayArea = board.getPlayerAreas().get(player);
        Corner corner = playerPlayArea.getCardMatrix().get(cardPos).getCorner(cornerDir);

        PlayCard cardToPlace = player.getHand().getCardByID(cardID);
        if(placeOnFront) cardToPlace.turnFaceUp();
        else cardToPlace.turnFaceDown();

        board.placeCard(player, cardToPlace, corner);

        if(board.canDraw()){
            currentPlayerHasPlacedCard = true;
            board.setGamePhase(GamePhase.DRAWCARD);
        }
        else{
            postDrawChecks();
        }
    }

    @Override
    public void draw(String nickname, char deckFrom, int cardPos)
            throws IllegalStateException, IllegalArgumentException {

        if(board.getGamePhase() != GamePhase.DRAWCARD)
            throw new IllegalStateException("Player has not placed a card yet!");

        Player player = board.getPlayerByNickname(nickname);
        
        if (player.getHand().isHandFull())
            throw new IllegalStateException("IMPOSSIBLE TO DRAW A CARD IN THIS PHASE, YOUR HAND IS FULL");
        if(board.getGamePhase() != GamePhase.DRAWCARD)
            throw new IllegalStateException("Player has not placed a card yet!");
        if(!board.canDraw()) // should never happen as placeCard already checks for this
            throw new IllegalStateException("IMPOSSIBLE TO DRAW A CARD: THERE IS NO CARD TO DRAW");
        if(!board.getCurrentPlayer().equals(player))
            throw new IllegalStateException("It's not your turn to draw yet");

        //FIXME Com'è possibile capitare in questa situazione? (mano piena in draw phase? [Ale])
        /*[FLAVIO] dovrebbe essere impossibile, ci penso e poi in caso lo tolgo,*/
        if (player.getHand().isHandFull())
            throw new IllegalStateException("IMPOSSIBLE TO DRAW A CARD IN THIS PHASE, YOUR HAND IS FULL");

        switch(cardPos) {
            case 0:
                try {board.drawTop(deckFrom, player.getHand());}
                catch (DeckException e) {System.out.println(e.getMessage()); }
                break;
            case 1:
                try {board.drawFirst(deckFrom, player.getHand());}
                catch (DeckException e) {System.out.println(e.getMessage());}
                break;
            case 2:
                try {board.drawSecond(deckFrom, player.getHand());}
                catch (DeckException e) {System.out.println(e.getMessage());}
                break;
            default:
                throw new IllegalArgumentException("Invalid card position for this draw command.");
        }

        postDrawChecks();
    }

    private void postDrawChecks(){
        boolean isLastPlayerTurn = board.getCurrentTurn() == board.getPlayerAreas().size();
        if(lastRound && isLastPlayerTurn)
            nextState();
        //FIXME: last round non deve essere alzato appena un giocatore raggiunge i 20?
        // quindi non devi fare il controllo senza isLastPlayerTurn?
        // Altrimenti dovremmo mettere qualcosa in checkEndGame che
        // almeno si occupi di notificare
        // [Ale] Potremmo anche delegare alla view la notifica immediata, siccome ha accesso alla scoreboard.
        //          La view può controllare l'endgame ad ogni piazzamento di carta.
        //      Questo sistema nel controller mi sembra valido e funziona.

        /*[FLAVIO] si, va alzato subito ma è un controllo che possono fare gli observer: se lo fa il controller cade nel
        * problema che è impossibile conprendere se è stato svolto l'ultimo turno o meno:
        * se il quarto giocatore arriva a 20 punti quando piazza la carta, allora se segno subito lastRound quando
        * poi il giocatore pesca risulta che lastRound è vero e che essendo l'ultimo giocatore la patita finisce
        * senza fare il giro finale.
        * ne avevo già parlato con qualcuno, che la notifica del last turn andrebbe fatta dalla view
        * indipendentemente dal controller, al tempo non avevamo ancora deciso per gli observer, adesso possiamo far
        * fare la notifica agli observer
        *
        * eventualmente se così non andasse bene, basta aggiungere un latro attributo sul controller che tiene
        * conto dei casi come nell'esempio*/
        if(isLastPlayerTurn && board.checkEndgame())
            lastRound=true;
        // if a player disconnected after place card but before draw:
        //      that player skips the placeCard step (if he can draw)
        if(board.nextTurn()) {
            currentPlayerHasPlacedCard = board.canDraw() && !board.getCurrentPlayer().getHand().isHandFull();
            if(currentPlayerHasPlacedCard)
                board.setGamePhase(GamePhase.DRAWCARD);
            else
                board.setGamePhase(GamePhase.PLACECARD);
        }
        else nextState();
    }

    private void nextState() throws IllegalStateException {
        transition( new EndgameState(board, controller, disconnectingPlayers) );
    }

    @Override
    public void startGame (String nickname, int numOfPlayers) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO START GAME DURING PLAY STATE");
    }
}
