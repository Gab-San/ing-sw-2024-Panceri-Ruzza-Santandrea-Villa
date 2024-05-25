package it.polimi.ingsw.stub;

import com.diogonunes.jcolor.Attribute;
import it.polimi.ingsw.Point;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.GameResource;
import it.polimi.ingsw.model.enums.PlayerColor;
import it.polimi.ingsw.model.listener.remote.events.playarea.CardPosition;
import it.polimi.ingsw.model.listener.remote.events.playarea.SerializableCorner;
import it.polimi.ingsw.network.CommandPassthrough;
import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import static com.diogonunes.jcolor.Ansi.colorize;

public class PuppetClient implements CommandPassthrough, VirtualClient {

    private final Attribute textColorFormat = Attribute.BRIGHT_CYAN_TEXT();

    @Override
    public void sendMsg(String addressee, String msg) {
        System.out.println(colorize(msg.toUpperCase() + " SENT TO " + addressee.toUpperCase(), textColorFormat));
    }

    @Override
    public void connect(String nickname) throws IllegalStateException {
        System.out.println(colorize("CONNECT COMMAND CALLED WITH ARGS:\n"
                + nickname, textColorFormat));
    }

    @Override
    public void setNumOfPlayers(int num) throws IllegalStateException {
        System.out.println(colorize("SET NUM OF PLAYERS COMMAND CALLED WITH ARGS:\n"
                + num , textColorFormat));
    }

    @Override
    public void disconnect() throws IllegalStateException {
        System.out.println(colorize("DISCONNECT COMMAND CALLED", textColorFormat));
    }

    @Override
    public void placeStartCard(boolean placeOnFront) throws IllegalStateException {
        System.out.println(colorize("PLACE STARTING CARD COMMAND CALLED WITH ARGS:\n"
                + placeOnFront, textColorFormat));
    }

    @Override
    public void chooseColor(char color) throws IllegalStateException {
        System.out.println(colorize("CHOOSE COLOR COMMAND CALLED WITH ARGS:\n"
                + color, textColorFormat));
    }

    @Override
    public void chooseObjective(int choice) throws IllegalStateException {
        System.out.println(colorize("CHOOSE OBJECTIVE COMMAND CALLED WITH ARGS:\n"
                + choice, textColorFormat));
    }

    @Override
    public void placeCard(String cardID, Point placePos, String cornerDir, boolean placeOnFront) throws IllegalStateException, RemoteException {
        System.out.println(colorize("PLACE CARD COMMAND CALLED WITH ARGS:\n"
                + cardID + "\n" +
                placePos + "\n" +
                cornerDir + "\n" +
                placeOnFront, textColorFormat));
    }

    @Override
    public void draw(char deck, int card) throws IllegalStateException {
        System.out.println(colorize("DRAW COMMAND CALLED WITH ARGS:\n"
                + deck + "\n"
                + card , textColorFormat));
    }

    @Override
    public void restartGame(int numOfPlayers) throws IllegalStateException {
        System.out.println(colorize("START GAME COMMAND CALLED WITH ARGS:\n"
                + numOfPlayers, textColorFormat));
    }

    @Override
    public void displayMessage(String messenger, String msg) throws RemoteException {

    }

    @Override
    public void ping() throws RemoteException {
    }

    @Override
    public void updatePlayer(String nickname, PlayerColor colour) throws RemoteException {

    }

    @Override
    public void updatePlayer(String nickname, int playerTurn) throws RemoteException {

    }

    @Override
    public void updatePlayer(String nickname, boolean isConnected) throws RemoteException {

    }

    /**
     * Notifies about the current state of the player.
     *
     * @param nickname    the unique nickname identifier of the player
     * @param isConnected the connection status as for the moment of the displayMessage
     * @param turn        the given player's turn
     * @param colour      the colour the player has chosen for the match
     * @throws RemoteException if a connection error occurs
     */
    @Override
    public void setPlayerState(String nickname, boolean isConnected, int turn, PlayerColor colour) throws RemoteException {

    }

    @Override
    public void deckUpdate(char deck, String revealedId, int cardPosition) throws RemoteException {

    }

    @Override
    public void setDeckState(char deck, String topId, String firstId, String secondId) throws RemoteException {

    }

    @Override
    public void setDeckState(char deck, String revealedId, int cardPosition) throws RemoteException {

    }

    @Override
    public void setDeckState(char deck, String firstCardId, String secondCardId) throws RemoteException {

    }

    @Override
    public void emptyFaceDownPile(char deck) throws RemoteException {

    }

    /**
     * Updates the current state of a deck.
     * <p>
     * This displayMessage can be triggered iff a deck is empty during initialization.
     * </p>
     *
     * @param deck the deck identifier
     * @throws RemoteException if a connection error is detected
     */
    @Override
    public void setEmptyDeckState(char deck) throws RemoteException {

    }

    @Override
    public void updatePhase(GamePhase gamePhase) throws RemoteException {

    }

    @Override
    public void updateScore(String nickname, int score) throws RemoteException {

    }

    @Override
    public void updateTurn(int currentTurn) throws RemoteException {

    }

    @Override
    public void emptyReveal(char deck, int cardPosition) throws RemoteException {

    }

    @Override
    public void setPlayerHandState(String nickname, List<String> playCards, List<String> objectiveCards, String startingCard) throws RemoteException {

    }

    @Override
    public void playerHandAddedCardUpdate(String nickname, String drawnCardId) throws RemoteException {

    }

    @Override
    public void playerHandRemoveCard(String nickname, String playCardId) throws RemoteException {

    }

    @Override
    public void playerHandAddObjective(String nickname, String objectiveCard) throws RemoteException {

    }

    @Override
    public void playerHandChooseObject(String nickname, String chosenObjectiveId) throws RemoteException {

    }

    @Override
    public void playerHandSetStartingCard(String nickname, String startingCardId) throws RemoteException {

    }

    @Override
    public void setPlayAreaState(String nickname, List<CardPosition> cardPositions, Map<GameResource, Integer> visibleResources, List<SerializableCorner> freeSerializableCorners) throws RemoteException {

    }

    @Override
    public void updatePlaceCard(String nickname, String placedCardId, int row, int col) throws RemoteException {

    }

    @Override
    public void visibleResourcesUpdate(String nickname, Map<GameResource, Integer> visibleResources) throws RemoteException {

    }

    @Override
    public void freeCornersUpdate(String nickname, List<SerializableCorner> freeSerialableCorners) throws RemoteException {

    }

    @Override
    public void setBoardState(int currentTurn, Map<String, Integer> scoreboard, GamePhase gamePhase, Map<String, Boolean> playerDeadLock) throws RemoteException {

    }

    @Override
    public void playerDeadLockUpdate(String nickname, boolean isDeadLocked) throws RemoteException {

    }

    @Override
    public void reportError(String errorMessage) throws RemoteException {

    }

    @Override
    public void notifyTimeoutDisconnect() {

    }

    @Override
    public void notifyEndgame() throws RemoteException {

    }

    @Override
    public void notifyEndgame(String nickname, int score) throws RemoteException {

    }

    @Override
    public void removePlayer(String nickname) throws RemoteException {

    }
}
