package it.polimi.ingsw.network.testingStub;

import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.model.listener.remote.events.playarea.CardPosition;
import it.polimi.ingsw.model.listener.remote.events.playarea.SerializableCorner;
import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.view.ModelUpdater;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.model.ViewBoard;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public class PuppetModelUpdater extends ModelUpdater {
    public PuppetModelUpdater() {
        super(null, null);
    }

    @Override
    public void update(String msg){

    }

    @Override
    public void ping(){

    }

    @Override
    public void updatePlayer(String nickname, PlayerColor colour){

    }

    @Override
    public void updatePlayer(String nickname, int playerTurn){

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
    public void emptyDeck(char deck){

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
    public void setPlayerHandState(String nickname, List<String> playCards, List<String> objectiveCards, String startingCard){

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
    public void createPlayArea(String nickname, List<CardPosition> cardPositions, Map<GameResource, Integer> visibleResources, List<SerializableCorner> freeSerializableCorners){

    }

    @Override
    public void placeCard(String nickname, String placedCardId, int row, int col){

    }

    @Override
    public void visibleResourcesUpdate(String nickname, Map<GameResource, Integer> visibleResources){

    }

    @Override
    public void freeCornersUpdate(String nickname, List<SerializableCorner> freeSerializableCorners){

    }

    @Override
    public void setBoardState(int currentTurn, GamePhase gamePhase){

    }

    @Override
    public void playerDeadLockUpdate(String nickname, boolean isDeadLocked){

    }

    @Override
    public void reportError(String errorMessage){

    }
}
