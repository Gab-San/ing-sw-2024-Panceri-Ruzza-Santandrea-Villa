package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Point;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.deck.cardfactory.CardFactory;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.PlayerColor;
import it.polimi.ingsw.model.exceptions.DeckException;
import it.polimi.ingsw.model.json.deserializers.ResourceCardJSON;
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
    public void placeCard(String nickname, String cardID, Point cardPos, CornerDirection cornerDir) throws IllegalStateException {
        if(!board.getGamePhase().equals("PLACE CARD PHASE"))
            throw new IllegalStateException("IMPOSSIBLE TO PLACE A CARD IN THIS PHASE");
        //FIXME: controlled: if it's player turn, if player has already placed
        if(board.getPlayersByTurn().get(board.getCurrentTurn()).getNickname().equals(nickname))
            throw new IllegalStateException("It's not your turn to place the card yet");
        if (currentPlayerHasPlacedCard)
            throw new IllegalStateException("Player had already placed a card!");


        Player player=board.getPlayerByNickname(nickname);
        PlayCard card = (PlayCard) board.getPlayerAreas().get(player).getCardMatrix().get(cardPos);
        Corner corner = card.getCorner(cornerDir);
        /*PlayCard cardToPlace=new ;
        for(int i=0; i<3; i++){
            PlaceableCard tmp=player.getHand().getCard(i);
            if(tmp.getCardID().equals(cardID)){
                cardToPlace=tmp;
                break;
            }
        }
        if(cardToPlace==null)
            throw new IllegalArgumentException();*/
        board.placeCard(player, card, corner);


        currentPlayerHasPlacedCard = true;
        board.setGamePhase(GamePhase.DCP);
    }

    @Override
    public GameState draw(String nickname, String cardToDraw) throws IllegalStateException {
        if(!board.getGamePhase().equals("DRAW CARD PHASE"))
            throw new IllegalStateException("IMPOSSIBLE TO DRAW A CARD IN THIS PHASE");
        //FIXME: controlled: if it's player turn, if player has already placed
        if(board.getPlayersByTurn().get(board.getCurrentTurn()).getNickname().equals(nickname))
            throw new IllegalStateException("It's not your turn to draw yet");
        if (!currentPlayerHasPlacedCard)
            throw new IllegalStateException("Player had to place a card yet!");
        Player player=board.getPlayersByTurn().get(board.getCurrentTurn());
        //FIXME: if(player.getHand().size()==3)?

        if(cardToDraw.length()!=2)
            throw new IllegalArgumentException();

        switch(cardToDraw.charAt(1)) {
            case ('0'):
                try {board.drawTop(cardToDraw.charAt(0), player.getHand());}
                catch (DeckException e) {/*TODO: handling exception */}
                break;
            case '1':
                try {board.drawFirst(cardToDraw.charAt(0), player.getHand());}
                catch (DeckException e) {/*TODO: handling exception */}
                break;
            case ('2'):
                try {board.drawSecond(cardToDraw.charAt(0), player.getHand());}
                catch (DeckException e) {/*TODO: handling exception */}
                break;
            default:
                throw new IllegalArgumentException();
        }

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
        return nextState();
    }

    private GameState nextState() throws IllegalStateException {
        return new EndgameState(board);
    }

    @Override
    public GameState startGame (String gameID, int numOfPlayers) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO START GAME DURING PLAY STATE");
    }
}
