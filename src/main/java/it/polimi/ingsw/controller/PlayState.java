package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerHand;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.deck.PlayableDeck;
import it.polimi.ingsw.model.enums.PlayerColor;
import it.polimi.ingsw.server.VirtualClient;

public class PlayState extends GameState {
    private boolean currentPlayerHasPlacedCard;
    private boolean lastRound;

    public PlayState(Board board) {
        super(board);
        currentPlayerHasPlacedCard = false;
        lastRound = false;
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
    public GameState startGame(String nickname) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO START GAME DURING PLAY STATE");
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


    public GameState draw(String nickname, int deck, int card) throws IllegalStateException {
        //TODO: this.board.draw(nickname, deck, card);
        boolean isLastPlayerTurn = board.getCurrentTurn()==board.getPlayerAreas().size();
        if(lastRound && isLastPlayerTurn)
            return nextState();
        if(board.checkEndgame() && isLastPlayerTurn)
            lastRound = true;

        if(board.nextTurn()) {
            currentPlayerHasPlacedCard = false;
            return this;
        }
        else return nextState(); // if game can't continue (nextTurn returns false), go to endgame
    }

    @Override
    public void placeCard(String nickname, PlayCard card, Corner corner) throws IllegalStateException {
        //FIXME: controlled: if it's player turn, if player has already placed
        if(board.getPlayersByTurn().get(board.getCurrentTurn()).getNickname().equals(nickname))
            throw new IllegalStateException("It's not your turn to place the card yet");
        if (currentPlayerHasPlacedCard)
            throw new IllegalStateException("Player had already placed a card!");

        Player player=board.getPlayerByNickname(nickname);
        board.placeCard(player, card, corner);

        currentPlayerHasPlacedCard = true;
    }

    private GameState nextState() throws IllegalStateException {
        return new EndgameState(board);
    }
    @Override
    public GameState draw(String nickname, String cardToDraw) throws IllegalStateException {
        //FIXME: controlled: if it's player turn, if player has already placed
        if(board.getPlayersByTurn().get(board.getCurrentTurn()).getNickname().equals(nickname))
            throw new IllegalStateException("It's not your turn to draw yet");
        if (!currentPlayerHasPlacedCard)
            throw new IllegalStateException("Player had to place a card yet!");
        Player player=board.getPlayersByTurn().get(board.getCurrentTurn());
        //if()

        if(cardToDraw.length()!=2)
            throw new IllegalArgumentException();

        switch(cardToDraw.charAt(1)) {
            case ('0'):
                board.drawTop(cardToDraw.charAt(0), board.getPlayersByTurn().get(board.getCurrentTurn()).getHand());
                break;
            case '1':
                board.drawFirst(cardToDraw.charAt(0), board.getPlayersByTurn().get(board.getCurrentTurn()).getHand());
                break;
            case ('2'):
                board.drawSecond(cardToDraw.charAt(0), board.getPlayersByTurn().get(board.getCurrentTurn()).getHand());
                break;
            default:
                throw new IllegalArgumentException();
        }

        boolean isLastPlayerTurn = board.getCurrentTurn()==board.getPlayerAreas().size();
        if(lastRound && board.getCurrentTurn()==board.getPlayerAreas().size())
            return nextState();
        if(isLastPlayerTurn && board.checkEndgame())
            lastRound=true;
        if(board.nextTurn()) {
            currentPlayerHasPlacedCard = false;
            return this;
        }
        return nextState();
    }

}
