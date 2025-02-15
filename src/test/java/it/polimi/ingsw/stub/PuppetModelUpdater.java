package it.polimi.ingsw.stub;

import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.CardPosition;
import it.polimi.ingsw.SerializableCorner;
import it.polimi.ingsw.view.ModelUpdater;

import java.util.List;
import java.util.Map;

/**
 * This class does nothing, and it is used to fill the model updater refs needed in
 * objects instantiations.
 */
public class PuppetModelUpdater extends ModelUpdater {
    public PuppetModelUpdater() {
        super(null);
    }

    @Override
    public void updatePlayer(String nickname, PlayerColor colour){

    }

    @Override
    public void updatePlayer(String nickname, int playerTurn){

    }

    @Override
    public synchronized void displayMessage(String messenger, String msg) {
        
    }

    @Override
    public synchronized void notifyIndirectDisconnect() {
        
    }

    @Override
    public synchronized void removePlayer(String nickname) {
        
    }

    @Override
    public synchronized void notifyEndgame() {
        
    }

    @Override
    public synchronized void notifyEndgame(String nickname, int score) {
        
    }

    @Override
    public synchronized void setBoardState(int currentTurn, Map<String, Integer> scoreboard, GamePhase gamePhase, Map<String, Boolean> playerDeadLock) {
        
    }

    @Override
    public void updatePlayer(String nickname, boolean isConnected){

    }

    @Override
    public void setPlayerState(String nickname, boolean isConnected, int turn, PlayerColor colour){

    }

    @Override
    public void deckUpdate(char deck, String revealedId, int cardPosition){

    }

    @Override
    public void setDeckState(char deck, String topId, String firstId, String secondId){

    }

    @Override
    public void setDeckState(char deck, String revealedId, int cardPosition){

    }

    @Override
    public void setDeckState(char deck, String firstCardId, String secondCardId){

    }

    @Override
    public void emptyReveal(char deck, int cardPosition){

    }

    @Override
    public void emptyFaceDownPile(char deck){

    }

    @Override
    public void setEmptyDeckState(char deck){

    }

    @Override
    public void updatePhase(GamePhase gamePhase){

    }

    @Override
    public void updateScore(String nickname, int score){

    }

    @Override
    public void updateTurn(int currentTurn){

    }

    @Override
    public void setPlayerHandState(String nickname, List<String> playCardIDs, List<String> objectiveCardIDs, String startingCardID){

    }

    @Override
    public void playerHandAddedCardUpdate(String nickname, String drawnCardId){

    }

    @Override
    public void playerHandRemoveCard(String nickname, String playCardId){

    }

    @Override
    public void playerHandAddObjective(String nickname, String objectiveCard){

    }

    @Override
    public void playerHandChooseObject(String nickname, String chosenObjectiveId){

    }

    @Override
    public void playerHandSetStartingCard(String nickname, String startingCardId){

    }

    @Override
    public void setPlayAreaState(String nickname, List<CardPosition> cardPositions, Map<GameResource, Integer> visibleResources, List<SerializableCorner> freeSerializableCorners){

    }

    @Override
    public void updatePlaceCard(String nickname, String placedCardId, int row, int col, boolean placeOnFront){

    }

    @Override
    public void visibleResourcesUpdate(String nickname, Map<GameResource, Integer> visibleResources){

    }

    @Override
    public void freeCornersUpdate(String nickname, List<SerializableCorner> freeSerializableCorners){

    }

    @Override
    public void playerDeadLockUpdate(String nickname, boolean isDeadLocked){

    }

    @Override
    public void reportError(String errorMessage){

    }
}
