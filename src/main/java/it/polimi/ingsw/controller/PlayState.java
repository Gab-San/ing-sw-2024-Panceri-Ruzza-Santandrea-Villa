package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Point;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.PlayerColor;
import it.polimi.ingsw.model.exceptions.DeckException;
import it.polimi.ingsw.server.VirtualClient;

public class PlayState extends GameState {
    private boolean currentPlayerHasPlacedCard;
    private boolean lastRound;

    public PlayState(Board board) {
        super(board);
        currentPlayerHasPlacedCard = false;
        lastRound = false;
        board.setGamePhase(GamePhase.PCP);
    }

    @Override
    public GameState join(String nickname, VirtualClient client) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO JOIN A GAME DURING PLAY STATE");
    }

    @Override
    public GameState setNumOfPlayers(String nickname, int num) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO CHANGE THE NUMBER OF PLAYERS DURING PLAY STATE");
    }

    @Override
    public void placeStartingCard(String nickname, boolean placeOnFront) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO PLACE STARTING CARD DURING PLAY STATE");
    }
    public void chooseYourColor(String nickname, PlayerColor color) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO CHOOSE OR CHANGE YOUR COLOR DURING PLAY STATE");
    }

    @Override
    public GameState chooseSecretObjective(String nickname, int choice) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO CHOOSE SECRET OBJECTIVE DURING PLAY STATE");
    }

    @Override
    public GameState placeCard(String nickname, String cardID, Point cardPos, CornerDirection cornerDir, boolean placeOnFront) throws IllegalStateException, IllegalArgumentException {
        if(board.getGamePhase() != GamePhase.PCP)
            throw new IllegalStateException("IMPOSSIBLE TO PLACE A CARD IN THIS PHASE");
        if (currentPlayerHasPlacedCard)
            throw new IllegalStateException("Player had already placed a card!");

        Player player = board.getPlayerByNickname(nickname); // throws if player isn't in game

        if(!board.getPlayersByTurn().get(board.getCurrentTurn()).equals(player))
            throw new IllegalStateException("It's not your turn to place the card yet");

        Corner corner = board.getPlayerAreas().get(player).getCardMatrix().get(cardPos).getCorner(cornerDir);

         if(!player.getHand().isHandFull())
            throw new IllegalStateException("IMPOSSIBLE TO PLACE A CARD IN THIS PHASE");

        PlayCard cardToPlace = player.getHand().getCardByID(cardID);
        if(placeOnFront) cardToPlace.turnFaceUp();
        else cardToPlace.turnFaceDown();
        board.placeCard(player, cardToPlace, corner);

        if(board.canDraw()){
            currentPlayerHasPlacedCard = true;
            board.setGamePhase(GamePhase.DCP);
            return this;
        }
        else{
            return postDrawChecks();
        }
    }

    @Override
    public GameState draw(String nickname, char deckFrom, int cardPos) throws IllegalStateException, IllegalArgumentException {
        if(board.getGamePhase() != GamePhase.DCP)
            throw new IllegalStateException("IMPOSSIBLE TO DRAW A CARD IN THIS PHASE");
        if(!board.canDraw()) // should never happen as placeCard already checks for this
            throw new IllegalStateException("IMPOSSIBLE TO DRAW A CARD: THERE IS NO CARD TO DRAW");
        if (!currentPlayerHasPlacedCard)
            throw new IllegalStateException("Player has not placed a card yet!");

        Player player = board.getPlayerByNickname(nickname);
        if(!board.getPlayersByTurn().get(board.getCurrentTurn()).equals(player))
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

        return postDrawChecks();
    }

    private GameState postDrawChecks(){
        boolean isLastPlayerTurn = board.getCurrentTurn()==board.getPlayerAreas().size();
        if(lastRound && isLastPlayerTurn)
            return nextState();
        if(isLastPlayerTurn && board.checkEndgame())
            lastRound=true;
        if(board.nextTurn()) {
            currentPlayerHasPlacedCard = false;
            board.setGamePhase(GamePhase.PCP);
            return this;
        }
        else return nextState();
    }

    private GameState nextState() throws IllegalStateException {
        return new EndgameState(board);
    }

    @Override
    public GameState startGame (String nickname, int numOfPlayers) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO START GAME DURING PLAY STATE");
    }
}
