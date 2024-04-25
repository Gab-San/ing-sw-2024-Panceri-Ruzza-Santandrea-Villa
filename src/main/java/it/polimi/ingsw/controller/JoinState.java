package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.server.VirtualClient;

import java.util.HashSet;
import java.util.Set;

public class JoinState extends GameState {
    private final Set<String> readyPlayers;
    public JoinState(Board board){
        super(board);
        readyPlayers = new HashSet<>();
    }

    @Override
    public void join(String nickname, VirtualClient client) throws Exception {
        synchronized (board){
            board.addPlayer(new Player(nickname)); // throws exception if player can't be added
            board.getGameInfo().addClient(client);
        }
    }

    @Override
    public GameState startGame(String nickname) throws Exception {
        synchronized (board) {
            if(!board.containsPlayer(nickname)) throw new Exception("Player isn't in this game!");
            // we may want to allow players to remove the "ready" with a second call of this method?
            if (readyPlayers.contains(nickname)){
                return this;
            }

            //TODO: send a message to all players in game that this player said they're ready

            readyPlayers.add(nickname);
            if (readyPlayers.size() == board.getPlayerAreas().size())
                return nextState();
            else
                return this;
        }
    }

    @Override
    public void placeStartingCard(String nickname, boolean placeOnFront) throws Exception {
        throw new Exception("IMPOSSIBLE TO PLACE STARTING CARDS DURING JOIN STATE");
    }

    @Override
    public GameState chooseSecretObjective(String nickname, int choice) throws Exception {
        throw new Exception("IMPOSSIBLE TO CHOOSE SECRETE OBJECTIVE DURING JOIN STATE");
    }

    @Override
    public GameState draw(String nickname, int deck, int card) throws Exception {
        throw new Exception("IMPOSSIBLE TO DRAW STARTING CARDS DURING JOIN STATE");
    }

    @Override
    public void placeCard(String nickname, PlayCard card, Corner corner) throws Exception {
        throw new Exception("IMPOSSIBLE TO PLACE CARDS DURING JOIN STATE");
    }

    private GameState nextState() throws Exception {
        return new SetupState(board);
    }
}
