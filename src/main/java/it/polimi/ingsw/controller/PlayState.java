package it.polimi.ingsw.controller;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.controller.timer.TurnTimerController;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.PlayArea;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.PlayerColor;
import it.polimi.ingsw.model.exceptions.DeckException;
import it.polimi.ingsw.server.VirtualClient;

import java.util.List;
import java.util.stream.Collectors;

public class PlayState extends GameState {
    private boolean lastRound;
    private boolean currentPlayerHasPlacedCard;
    private final TurnTimerController timerCurrPlayer;

    public PlayState(Board board, BoardController controller, List<String> disconnectingPlayers) {
        super(board, controller, disconnectingPlayers);
        lastRound = false;
        currentPlayerHasPlacedCard = false;
        timerCurrPlayer=new TurnTimerController(controller);
        board.setGamePhase(GamePhase.PLACECARD);
        Player player=board.getCurrentPlayer();
        timerCurrPlayer.startTimer(player, 302);
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

        //FIXME: [FLAVIO] non dovrebbe mai succedere in quanto il turno dei giocatori disconnessi viene saltato
        Player player=board.getPlayerByNickname(nickname);
        if(board.getCurrentPlayer().equals(player))
            postDrawChecks();
    }

    @Override
    public void setNumOfPlayers(String nickname, int num) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO CHANGE THE NUMBER OF PLAYERS DURING PLAY STATE");
    }

    @Override
    public void disconnect(String nickname) throws IllegalStateException, IllegalArgumentException {
        //TODO implement disconnect

        if(board.getPlayerAreas().keySet().stream()
                .map(Player::getNickname).filter((s)-> s.equals(nickname)).findAny().isEmpty())
            //se non esiste un giocatore con quel nickname
            throw new IllegalArgumentException(nickname+" non fa parte della fartita, non può disconnettersi");
        board.disconnectPlayer(nickname);
        //TODO unsubscribe player's client from observers
        //   and push current state to client (possibly done in board.replaceClient())

        Player player=board.getCurrentPlayer();
        if(player.getNickname().equals(nickname)){
            postDrawChecks();
        }
        if(board.getPlayerAreas().keySet().stream()
                // only look at players that are connected
                .filter(Player:: isConnected)
                .collect(Collectors.toSet())
                .isEmpty())
            nextState();
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
    public void placeCard(String nickname, String cardID, Point cardPos, CornerDirection cornerDir, boolean placeOnFront)
            throws IllegalStateException, IllegalArgumentException {

        Player player = board.getPlayerByNickname(nickname); // throws if player isn't in game

        if(board.getGamePhase() != GamePhase.PLACECARD)
            throw new IllegalStateException("IMPOSSIBLE TO PLACE A CARD IN THIS PHASE");
        if (currentPlayerHasPlacedCard)
            throw new IllegalStateException("Player had already placed a card!");

        if(!board.getCurrentPlayer().equals(player))
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
            timerCurrPlayer.stopTimer(player);
            postDrawChecks();
        }
    }

    @Override
    public void draw(String nickname, char deckFrom, int cardPos)
            throws IllegalStateException, IllegalArgumentException {

        if(board.getGamePhase() != GamePhase.DRAWCARD)
            throw new IllegalStateException("Player has not placed a card yet!");

        Player player = board.getPlayerByNickname(nickname);
        if(!board.getCurrentPlayer().equals(player))
            throw new IllegalStateException("It's not your turn to draw yet");

        //TODO handle PlayerHandException
        try {
            switch (cardPos) {
                case 0:
                    board.drawTop(deckFrom, player.getHand());
                    break;
                case 1:
                    board.drawFirst(deckFrom, player.getHand());
                    break;
                case 2:
                    board.drawSecond(deckFrom, player.getHand());
                    break;
                default:
                    throw new IllegalArgumentException("Invalid card position for this draw command.");
            }
        }catch (DeckException e){
            throw new IllegalArgumentException(e.getMessage() + "Can't draw from position " + cardPos);
        }
        timerCurrPlayer.stopTimer(player);
        postDrawChecks();
    }

    private void postDrawChecks(){
        int lastPlayerTurn = board.getPlayerAreas().keySet().stream()
                // only look at players that are connected and NOT deadlocked
                .filter(p -> p.isConnected() && !board.getPlayerDeadlocks().get(p))
                .mapToInt(Player::getTurn)
                .max()
                .orElse(0); // if all players are deadlocked/disconnected, this doesn't matter
        boolean isLastPlayerTurn = board.getCurrentTurn() >= lastPlayerTurn;
        if(lastRound && isLastPlayerTurn) {
            nextState();
            return;
        }

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
            timerCurrPlayer.startTimer(board.getCurrentPlayer(), 302);
        }
        else nextState();
    }
    private void nextState() throws IllegalStateException {
        transition( new EndgameState(board, controller, disconnectingPlayers) );
    }

    @Override
    public void restartGame(String nickname, int numOfPlayers) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO START GAME DURING PLAY STATE");
    }
}
