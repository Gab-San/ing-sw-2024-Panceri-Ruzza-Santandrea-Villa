package it.polimi.ingsw.stub;


import com.diogonunes.jcolor.Attribute;
import it.polimi.ingsw.Point;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.GameResource;
import it.polimi.ingsw.model.enums.PlayerColor;
import it.polimi.ingsw.model.listener.remote.events.playarea.CardPosition;
import it.polimi.ingsw.model.listener.remote.events.playarea.SerializableCorner;
import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import static com.diogonunes.jcolor.Ansi.colorize;

public class StubClient implements VirtualClient {
    private final StubView view;
    private final String nickname;
    public String reportedError;

    public StubClient(String nickname, StubView view) {
        this.view = view;
        this.nickname = nickname;
    }

    @Override
    public synchronized void update(String msg) throws RemoteException {

    }

    @Override
    public synchronized void ping() throws RemoteException {

    }


    @Override
    public synchronized void updatePlayer(String nickname, PlayerColor colour) throws RemoteException {
        System.out.println(colorize("Notifying " + this.nickname + "\nof player update with:\n" +
                "[PLAYER] " + nickname + "\n" +
                "[PLAYER COLOUR] " + colour, Attribute.MAGENTA_TEXT()));
        view.getPlayer(nickname).setColor(colour);
    }

    @Override
    public synchronized void updatePlayer(String nickname, int playerTurn) throws RemoteException {
        System.out.println(colorize("Notifying " + this.nickname + "\nof player update with:\n" +
                "[PLAYER] " + nickname + "\n" +
                "[PLAYER TURN] " + playerTurn, Attribute.MAGENTA_TEXT()));
        view.getPlayer(nickname).setTurn(playerTurn);
    }

    @Override
    public synchronized void updatePlayer(String nickname, boolean isConnected) throws RemoteException {
        System.out.println(colorize("Notifying " + this.nickname + "\nof player update with:\n" +
                "[PLAYER] " + nickname + "\n" +
                "[CONNECTION] " + isConnected, Attribute.MAGENTA_TEXT()));
        view.getPlayer(nickname).setConnected(isConnected);
    }

    @Override
    public synchronized void setDeckState(String nickname, boolean isConnected, int turn, PlayerColor colour) throws RemoteException {
        System.out.println(colorize("Being notified by " + this.nickname + "\nof player creation with:\n" +
                "[PLAYER] " + nickname + "\n" +
                "[CONNECTION] " + isConnected + "\n" +
                "[PLAYER TURN] " + turn + "\n" +
                "[PLAYER COLOUR] " +colour, Attribute.BLUE_TEXT()));
        view.addPlayer(nickname, isConnected, turn, colour);
    }

    @Override
    public synchronized void deckUpdate(char deck, String revealedId, int cardPosition) throws RemoteException {
        if (cardPosition == 0) {
            System.out.println(colorize("Notifing " + this.nickname + "\nof deck draw with:\n" +
                    "[DECK TYPE] " + deck + "\n" +
                    "[TOP CARD] " + revealedId + "\n" +
                    "[CARD POSITION] " + cardPosition, Attribute.BLUE_TEXT()));
        } else {
            System.out.println(colorize("Notifing " + this.nickname + "\nof deck reveal with:\n" +
                    "[DECK TYPE] " + deck + "\n" +
                    "[REVEALED CARD] " + revealedId + "\n" +
                    "[CARD POSITION] " + cardPosition, Attribute.BLUE_TEXT()));
        }

    }

    @Override
    public synchronized void setDeckState(char deck, String topId, String firstId, String secondId) throws RemoteException {
        System.out.println(colorize("Being notified by " + this.nickname + "\nof deck creation with:\n" +
                "[DECK TYPE] " + deck + "\n" +
                "[TOP CARD] " + topId + "\n" +
                "[FIRST CARD] " + firstId + "\n" +
                "[SECOND CARD] " + secondId, Attribute.BLUE_TEXT()));
    }

    @Override
    public synchronized void setDeckState(char deck, String revealedId, int cardPosition) throws RemoteException {
        System.out.println(colorize("Notifying " + this.nickname + "\nof deck revealation with:\n" +
                "[DECK TYPE] " + deck + "\n" +
                "[CARD] " + revealedId + "\n" +
                "[CARD POSITION] " + cardPosition, Attribute.BLUE_TEXT()));
    }

    @Override
    public synchronized void setDeckState(char deck, String firstCardId, String secondCardId) throws RemoteException {
        System.out.println(colorize("Notifying " + this.nickname + "\nof deck creation with:\n" +
                "[DECK TYPE] " + deck + "\n" +
                "[FIRST CARD] " + firstCardId + "\n"
                + "[SECOND CARD] " + secondCardId  , Attribute.BLUE_TEXT()));
    }


    @Override
    public synchronized void emptyDeck(char deck) throws RemoteException {
        System.out.println(colorize("Notifying " + this.nickname + "\nof an empty deck:\n" +
                "[DECK TYPE] " + deck, Attribute.BLUE_TEXT()));
    }

    @Override
    public synchronized void updatePhase(GamePhase gamePhase) throws RemoteException {
        System.out.println(colorize("Notifying " + this.nickname + "\nof game phase update with:\n" +
                "[GAME PHASE] " + gamePhase, Attribute.RED_TEXT()));
        view.setPhase(gamePhase);
    }

    @Override
    public synchronized void updateScore(String nickname, int score) throws RemoteException {
        System.out.println(colorize("Notified " + this.nickname + "\nof score update with:\n" +
                "[PLAYER] " + nickname+ "\n" +
                "[SCORE] " + score, Attribute.RED_TEXT()));
        view.setScoreboard(nickname, score);
    }

    @Override
    public synchronized void updateTurn(int currentTurn) throws RemoteException {
        System.out.println(colorize("Notified " + this.nickname + "\nof turn change update with:\n" +
                "[CURRENT TURN] " + currentTurn, Attribute.RED_TEXT()));
        view.setTurn(currentTurn);
    }

    @Override
    public synchronized void emptyReveal(char deck, int cardPosition) throws RemoteException {
        System.out.println(colorize("Notified " + this.nickname + "\nof empty revealed card with:\n" +
                "[DECK TYPE] " + deck + "\n" +
                "[CARD POSITION] " + cardPosition, Attribute.BLUE_TEXT()));
    }

    @Override
    public synchronized void createEmptyDeck(char deck) throws RemoteException {
        System.out.println(colorize("Notified " + this.nickname + "\nof empty deck creation with:\n" +
                "[DECK TYPE] " + deck + "\n", Attribute.BLUE_TEXT()));
    }

    @Override
    public synchronized void setPlayerHandState(String nickname, List<String> playCards, List<String> objectiveCards, String startingCard) throws RemoteException {
        System.out.println(colorize("Notified " + this.nickname + "\nof player hand creation with:\n" +
                "[PLAYER] " + nickname + "\n" +
                "[PLAYCARDS] " + playCards + "\n" +
                "[OBJECTIVE CARDS] " + objectiveCards + "\n" +
                "[STARTING CARD] " + startingCard, Attribute.YELLOW_TEXT()));
    }

    @Override
    public synchronized void playerHandAddedCardUpdate(String nickname, String drawnCardId) throws RemoteException {
        System.out.println(colorize("Notified " + this.nickname + "\nof added card with:\n" +
                "[PLAYER] " + nickname + "\n" +
                "[ADDED CARD] " + drawnCardId, Attribute.YELLOW_TEXT()));
    }

    @Override
    public synchronized void playerHandRemoveCard(String nickname, String playCardId) throws RemoteException {
        System.out.println(colorize("Being notified by " + this.nickname + "\nof card removal with:\n" +
                "[PLAYER] " + nickname + "\n" +
                "[PLAY CARD] " + playCardId, Attribute.YELLOW_TEXT()));
    }

    @Override
    public synchronized void playerHandAddObjective(String nickname, String objectiveCard) throws RemoteException {
        System.out.println(colorize("Notified by " + this.nickname + "\nof added objective with:\n" +
                "[PLAYER] " + nickname + "\n" +
                "[ADDED OBJECTIVE CARD] " + objectiveCard, Attribute.YELLOW_TEXT()));
    }

    @Override
    public synchronized void playerHandChooseObject(String nickname, String chosenObjectiveId) throws RemoteException {
        System.out.println(colorize("Being notified by " + this.nickname + "\nof chosen objective with:\n" +
                "[PLAYER] " + nickname + "\n" +
                "[CHOSEN OBJECTIVE CARD] " + chosenObjectiveId, Attribute.YELLOW_TEXT()));
    }

    @Override
    public synchronized void playerHandSetStartingCard(String nickname, String startingCardId) throws RemoteException {
        System.out.println(colorize("Being notified by " + this.nickname + "\nof added starting card with:\n" +
                "[PLAYER] " + nickname + "\n" +
                "[STARTING CARD] " + startingCardId , Attribute.YELLOW_TEXT()));
    }

    @Override
    public synchronized void createPlayArea(String nickname, List<CardPosition> cardPositions, Map<GameResource, Integer> visibleResources, List<SerializableCorner> freeSerializableCorners) throws RemoteException {
        System.out.println(colorize("Being notified by " + this.nickname + "\nof play area creation with:\n" +
                "[PLAYER] " + nickname + "\n" +
                "[CARD POSITIONS] " + cardPositions + "\n" +
                "[VISIBLE RESOURCES] " + visibleResources + "\n" +
                "[FREE CORNERS] " + freeSerializableCorners, Attribute.MAGENTA_TEXT()));
    }

    @Override
    public synchronized void placeCard(String nickname, String placedCardId, int row, int col) throws RemoteException {
        System.out.println(colorize("Being notified by " + this.nickname + "\nof card placement with:\n" +
                "[PLAYER] " + nickname + "\n" +
                "[PLACED CARD] " + placedCardId + "\n" +
                "[POINT] " + new Point(row ,col), Attribute.MAGENTA_TEXT()));
    }

    @Override
    public synchronized void visibleResourcesUpdate(String nickname, Map<GameResource, Integer> visibleResources) throws RemoteException {
        System.out.println(colorize("Being notified by " + this.nickname + "\nof visible resources with:\n" +
                "[PLAYER] " + nickname + "\n" +
                "[VISIBLE RESOURCES] " + visibleResources , Attribute.MAGENTA_TEXT()));
        view.setVisibleResources(visibleResources);
    }

    @Override
    public synchronized void freeCornersUpdate(String nickname, List<SerializableCorner> freeSerialableCorners) throws RemoteException {
        System.out.println(colorize("Being notified by " + this.nickname + "\nof free corners update with:\n" +
                "[PLAYER] " + nickname + "\n" +
                "[FREE CORNERS] " + freeSerialableCorners, Attribute.MAGENTA_TEXT()));
        view.setFreeCorners(freeSerialableCorners);
    }

    @Override
    public synchronized void setBoardState(int currentTurn, Map<String, Integer> scoreboard,  GamePhase gamePhase, Map<String, Boolean> playerDeadLock) throws RemoteException {
        System.out.println(colorize("Notified " + this.nickname + "\nof board state update with:\n" +
                "[TURN] " + currentTurn + "\n" +
                "[GAME PHASE] " + gamePhase, Attribute.RED_TEXT()));
    }

    @Override
    public synchronized void playerDeadLockUpdate(String nickname, boolean isDeadLocked) throws RemoteException {
        System.out.println(colorize("Being notified by " + this.nickname + "\nof player update with:\n" +
                "[PLAYER] " + nickname + "\n" +
                "[DEADLOCK] " + isDeadLocked, Attribute.MAGENTA_TEXT()));
    }

    @Override
    public synchronized void reportError(String errorMessage) throws RemoteException {
        reportedError = errorMessage;
        System.out.println(colorize("Notifying " + nickname + " of error", Attribute.BLACK_TEXT(), Attribute.BRIGHT_CYAN_BACK()));
        System.out.println(colorize(errorMessage, Attribute.BLACK_TEXT(), Attribute.RED_BACK()));
    }

    @Override
    public synchronized void notifyEndgame() throws RemoteException {
        view.setEndgame(true);
    }

    @Override
    public synchronized void notifyEndgame(String nickname, int score) throws RemoteException {
        view.setEndgame(true);
    }

    @Override
    public synchronized void removePlayer(String nickname) throws RemoteException {
        System.out.println(colorize("Notifying " + this.nickname + "\nof player removal with:\n" +
                "[PLAYER] " + nickname, Attribute.MAGENTA_TEXT()));
        view.removePlayer(nickname);
    }

}
