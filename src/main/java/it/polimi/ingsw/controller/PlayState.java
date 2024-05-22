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
import it.polimi.ingsw.model.exceptions.PlayerHandException;
import it.polimi.ingsw.model.listener.remote.errors.IllegalActionError;
import it.polimi.ingsw.model.listener.remote.errors.IllegalParameterError;
import it.polimi.ingsw.network.VirtualClient;

import java.util.List;
import java.util.stream.Collectors;

public class PlayState extends GameState {
    private boolean lastRound;
    private boolean currentPlayerHasPlacedCard;
    private final TurnTimerController timerCurrPlayer;

    public PlayState(Board board, BoardController controller, List<String> disconnectingPlayers) {
        super(board, controller, disconnectingPlayers);
        board.setCurrentTurn(1);
        lastRound = false;
        currentPlayerHasPlacedCard = false;
        timerCurrPlayer=new TurnTimerController(controller);
        board.setGamePhase(GamePhase.PLACECARD);
        Player player = board.getCurrentPlayer();
        timerCurrPlayer.startTimer(player, 302);
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

        if(board.getPlayerAreas().keySet().stream()
                .map(Player::getNickname).filter((s)-> s.equals(nickname)).findAny().isEmpty())
            //se non esiste un giocatore con quel nickname
            throw new IllegalArgumentException(nickname+" non fa parte della fartita, non può disconnettersi");
        board.disconnectPlayer(nickname);
        board.unsubscribeClientFromUpdates(nickname);
        Player player = board.getCurrentPlayer();
        if(player.getNickname().equals(nickname)){
            postDrawChecks();
        }
        if(board.getPlayerAreas().keySet().stream()
                // only look at players that are connected
                .filter(Player:: isConnected)
                .collect(Collectors.toSet())
                .isEmpty())
        {
            nextState();
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
        board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO PLACE STARTING CARD DURING PLAY STATE"));
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
            timerCurrPlayer.stopTimer(player);
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

        if(board.checkEndgame() && isLastPlayerTurn)
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
        board.notifyAllListeners(new IllegalActionError(nickname, "IMPOSSIBLE TO START GAME DURING PLAY STATE"));
        throw new IllegalStateException("IMPOSSIBLE TO START GAME DURING PLAY STATE");
    }
}
