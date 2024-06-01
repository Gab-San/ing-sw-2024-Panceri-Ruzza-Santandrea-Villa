package it.polimi.ingsw.controller;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.Point;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.PlayArea;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.exceptions.DeckException;
import it.polimi.ingsw.model.exceptions.PlayerHandException;
import it.polimi.ingsw.model.listener.remote.errors.IllegalActionError;
import it.polimi.ingsw.model.listener.remote.errors.IllegalParameterError;
import it.polimi.ingsw.network.VirtualClient;

import java.util.List;
import java.util.stream.Collectors;

public class PlayState extends GameState {
    private boolean lastRound;
    private boolean currentPlayerHasPlacedCard;
    private static final int TURN_TIME = 302;

    public PlayState(Board board, BoardController controller, List<String> disconnectingPlayers) {
        super(board, controller, disconnectingPlayers);
        int startingTurn = board.getPlayersByTurn().stream()
                .filter(Player::isConnected)
                .mapToInt(Player::getTurn)
                .min().orElse(-1); // there should always be at least one connected player at this point
        board.setCurrentTurn(startingTurn);
        lastRound = false;
        currentPlayerHasPlacedCard = false;
        board.setGamePhase(GamePhase.PLACECARD);
        Player player = board.getCurrentPlayer();
        timers.startTimer(player, TURN_TIME);
    }

    @Override
    public void join(String nickname, VirtualClient client) throws IllegalStateException {
        try{
            board.reconnectPlayer(nickname);
        }catch (IllegalStateException e){
            throw new IllegalStateException(e.getMessage());
        }

        board.subscribeClientToUpdates(nickname, client);
    }

    @Override
    public void disconnect(String nickname) throws IllegalStateException, IllegalArgumentException {
        disconnectingPlayers.remove(nickname);

        board.getPlayerByNickname(nickname); // throws IllegalArgumentException if player not in game

        board.unsubscribeClientFromUpdates(nickname);
        board.disconnectPlayer(nickname);
        Player player = board.getCurrentPlayer();

        timers.stopTimer(player);

        if(player.getNickname().equals(nickname)){
            postDrawChecks();
        }

        if(board.getPlayerAreas().keySet().stream()
                // only look at players that are connected
                .filter(Player:: isConnected)
                .collect(Collectors.toSet())
                .isEmpty())
        {
            //FIXME: [Ale] returning to CreationState on empty game as Endgame doesn't allow joining (no player disconnects and game is never reset)
            //      If Endgame allows joining, then restore nextState();
            transition(new CreationState(new Board(), controller, disconnectingPlayers));
//            nextState();
        }
    }

    @Override
    public void setNumOfPlayers(String nickname, int num) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO CHANGE THE NUMBER OF PLAYERS DURING PLAY STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO CHANGE THE NUMBER OF PLAYERS DURING PLAY STATE");
    }

    @Override
    public void placeStartingCard(String nickname, boolean placeOnFront) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname,"IMPOSSIBLE TO PLACE STARTING CARD DURING PLAY STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO PLACE STARTING CARD DURING PLAY STATE");
    }
    public void chooseYourColor(String nickname, PlayerColor color) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO CHOOSE OR CHANGE YOUR COLOR DURING PLAY STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO CHOOSE OR CHANGE YOUR COLOR DURING PLAY STATE");
    }

    @Override
    public void chooseSecretObjective(String nickname, int choice) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO CHOOSE SECRET OBJECTIVE DURING PLAY STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO CHOOSE SECRET OBJECTIVE DURING PLAY STATE");
    }

    @Override
    public void placeCard(String nickname, String cardID, Point cardPos, CornerDirection cornerDir, boolean placeOnFront)
            throws IllegalStateException, IllegalArgumentException {

        Player player;
        player = board.getPlayerByNickname(nickname); // throws if player isn't in game

        if(board.getGamePhase() != GamePhase.PLACECARD) {
            board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO PLACE A CARD IN THIS PHASE"));
            throw new IllegalStateException("IMPOSSIBLE TO PLACE A CARD IN THIS PHASE");
        }
        // In theory this if should never be called
        if (currentPlayerHasPlacedCard) {
            board.notifyAllListeners(new IllegalActionError(nickname, "Player had already placed a card!".toUpperCase()));
            throw new IllegalStateException("Player had already placed a card!");
        }

        if(!board.getCurrentPlayer().equals(player)) {
            board.notifyAllListeners(new IllegalActionError(nickname, "It's not your turn to place the card yet".toUpperCase() ));
            throw new IllegalStateException("It's not your turn to place the card yet");
        }

        PlayArea playerPlayArea = board.getPlayerAreas().get(player);
        Corner corner;
        try {
             corner = playerPlayArea.getCardMatrix().get(cardPos).getCorner(cornerDir);
        } catch (NullPointerException pointerException){
            board.notifyAllListeners(new IllegalParameterError(nickname, "TRYING TO PLACE ON A NON EXISTENT CORNER"));
            throw new IllegalArgumentException("THE DESIRED CORNER WAS NOT FOUND");
        }

        PlayCard cardToPlace = player.getHand().getCardByID(cardID);

        if(placeOnFront) cardToPlace.turnFaceUp();
        else cardToPlace.turnFaceDown();

        board.placeCard(player, cardToPlace, corner);

        if(board.canDraw()){
            currentPlayerHasPlacedCard = true;
            board.setGamePhase(GamePhase.DRAWCARD);
        }
        else{
            timers.stopTimer(player);
            postDrawChecks();
        }
    }

    @Override
    public void draw(String nickname, char deckFrom, int cardPos)
            throws IllegalStateException, IllegalArgumentException {

        if(board.getGamePhase() != GamePhase.DRAWCARD) {
            board.notifyAllListeners(new IllegalActionError(nickname, "Player has not placed a card yet!".toUpperCase()));
            throw new IllegalStateException("Player has not placed a card yet!");
        }

        Player player = board.getPlayerByNickname(nickname);

        if(!board.getCurrentPlayer().equals(player)) {
            board.notifyAllListeners(new IllegalActionError(nickname, "It's not your turn to draw yet".toUpperCase()));
            throw new IllegalStateException("It's not your turn to draw yet");
        }

        timers.stopTimer(player);

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
                    board.notifyAllListeners(new IllegalParameterError(nickname, "Invalid card position for this draw command.".toUpperCase()));
                    throw new IllegalArgumentException("Invalid card position for this draw command.");
            }
        }catch (DeckException |PlayerHandException | IllegalStateException e){
            throw new IllegalArgumentException("Can't draw from position " + cardPos, e);
        }

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

        if(board.checkEndgame() && isLastPlayerTurn)
            lastRound=true;
        // if a player disconnected after place card but before draw:
        //      that player skips the updatePlaceCard step (if he can draw)
        if(board.nextTurn()) {
            currentPlayerHasPlacedCard = board.canDraw() && !board.getCurrentPlayer().getHand().isHandFull();
            if(currentPlayerHasPlacedCard)
                board.setGamePhase(GamePhase.DRAWCARD);
            else
                board.setGamePhase(GamePhase.PLACECARD);
            timers.startTimer(board.getCurrentPlayer(), TURN_TIME);
        }
        else nextState();
    }
    private void nextState() throws IllegalStateException {
        board.squashHistory();
        transition( new EndgameState(board, controller, disconnectingPlayers) );
    }

    @Override
    public void restartGame(String nickname, int numOfPlayers) throws IllegalStateException {
        board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO START GAME DURING PLAY STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO START GAME DURING PLAY STATE");
    }
}
