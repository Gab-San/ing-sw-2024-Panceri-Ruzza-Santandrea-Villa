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

public class PlayState extends GameState {
    private boolean currentPlayerHasPlacedCard;
    private boolean lastRound;

    public PlayState(Board board, BoardController controller) {
        super(board, controller);
        currentPlayerHasPlacedCard = false;
        lastRound = false;
        board.setGamePhase(GamePhase.PCP);
    }

    @Override
    public void join(String nickname, VirtualClient client) throws IllegalStateException {
        //TODO possibly implement join (as reconnection)
        throw new IllegalStateException("IMPOSSIBLE TO JOIN A GAME DURING PLAY STATE");
    }

    @Override
    public void setNumOfPlayers(String nickname, int num) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO CHANGE THE NUMBER OF PLAYERS DURING PLAY STATE");
    }

    @Override
    public void disconnect(String nickname, VirtualClient client) throws IllegalStateException, IllegalArgumentException {
        //TODO implement disconnect
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
        if(board.getGamePhase() != GamePhase.PCP)
            throw new IllegalStateException("IMPOSSIBLE TO PLACE A CARD IN THIS PHASE");
        if (currentPlayerHasPlacedCard)
            throw new IllegalStateException("Player had already placed a card!");

        Player player = board.getPlayerByNickname(nickname); // throws if player isn't in game
        int currentTurn = board.getCurrentTurn();
        if(!board.getPlayersByTurn().get(currentTurn).equals(player))
            throw new IllegalStateException("It's not your turn to place the card yet");

        PlayArea playerPlayArea = board.getPlayerAreas().get(player);
        Corner corner = playerPlayArea.getCardMatrix().get(cardPos).getCorner(cornerDir);

         if(!player.getHand().isHandFull())
            throw new IllegalStateException("PLAYER HAS NOT DRAWN A CARD IN THE LAST ROUND");

        PlayCard cardToPlace = player.getHand().getCardByID(cardID);
        if(placeOnFront) cardToPlace.turnFaceUp();
        else cardToPlace.turnFaceDown();

        board.placeCard(player, cardToPlace, corner);

        if(board.canDraw()){
            currentPlayerHasPlacedCard = true;
            board.setGamePhase(GamePhase.DCP);
        }
        else{
            postDrawChecks();
        }
    }

    @Override
    public void draw(String nickname, char deckFrom, int cardPos) throws IllegalStateException, IllegalArgumentException {
        if(board.getGamePhase() != GamePhase.DCP)
            throw new IllegalStateException("IMPOSSIBLE TO DRAW A CARD IN THIS PHASE");
        if(!board.canDraw()) // should never happen as placeCard already checks for this
            throw new IllegalStateException("IMPOSSIBLE TO DRAW A CARD: THERE IS NO CARD TO DRAW");
        if (!currentPlayerHasPlacedCard)
            throw new IllegalStateException("Player has not placed a card yet!");

        Player player = board.getPlayerByNickname(nickname);
        int currentTurn = board.getCurrentTurn();
        if(!board.getPlayersByTurn().get(currentTurn).equals(player))
            throw new IllegalStateException("It's not your turn to draw yet");

        switch(cardPos) {
            case 0:
                try {board.drawTop(deckFrom, player.getHand());}
                catch (DeckException e) {/*TODO: handling exception */}
                break;
            case 1:
                try {board.drawFirst(deckFrom, player.getHand());}
                catch (DeckException e) {/*TODO: handling exception */}
                break;
            case 2:
                try {board.drawSecond(deckFrom, player.getHand());}
                catch (DeckException e) {/*TODO: handling exception */}
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
        if(isLastPlayerTurn && board.checkEndgame())
            lastRound=true;
        if(board.nextTurn()) {
            currentPlayerHasPlacedCard = false;
            board.setGamePhase(GamePhase.PCP);
        }
        else nextState();
    }

    private void nextState() throws IllegalStateException {
        transition( new EndgameState(board, controller) );
    }

    @Override
    public void startGame (String nickname, int numOfPlayers) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO START GAME DURING PLAY STATE");
    }
}
