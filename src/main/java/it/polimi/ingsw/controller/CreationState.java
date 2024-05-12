package it.polimi.ingsw.controller;

import com.diogonunes.jcolor.Attribute;
import it.polimi.ingsw.Point;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.GamePhase;
import it.polimi.ingsw.model.enums.PlayerColor;
import it.polimi.ingsw.server.VirtualClient;
import java.util.List;
import static com.diogonunes.jcolor.Ansi.colorize;


public class CreationState extends GameState{
    public CreationState(Board board, BoardController controller, List<String> disconnectingPlayers) {
        super(board, controller, disconnectingPlayers);
        board.setGamePhase(GamePhase.CREATE);
    }

    @Override
    public void join(String nickname, VirtualClient client) throws IllegalStateException {
        if(!board.getPlayerAreas().isEmpty()) {
            throw new IllegalStateException("WAITING FOR THE CONNECTED PLAYER TO START THE LOBBY...");
        }
        System.out.println(colorize("Controller: Player joined the game!", Attribute.YELLOW_BACK()));
        board.addPlayer(new Player(nickname));
        //TODO subscribe player to clients
        board.setGamePhase(GamePhase.SETNUMPLAYERS);
    }

    @Override
    public void setNumOfPlayers(String nickname, int num) throws IllegalStateException, IllegalArgumentException {
        if(board.getGamePhase()!=GamePhase.SETNUMPLAYERS)
            throw new IllegalStateException("IMPOSSIBLE TO SET THE NUMBER OF PLAYERS IN THIS PHASE");
        board.getPlayerByNickname(nickname); // throws on player not in game
        if(num<2 || num>4) {
            System.out.println(colorize("Controller: UPDATING ERROR IDX OUT OF BOUNDS!", Attribute.YELLOW_BACK()));
            throw new IllegalArgumentException("NUMBER OF PLAYERS IN THE GAME MUST BE BETWEEN 2 AND 4 INCLUDED, YOU INSERTED " + num + " PLAYERS");
        }
        System.out.println(colorize("Controller: Number of players set", Attribute.YELLOW_BACK()));
        transition(new JoinState(board, controller, disconnectingPlayers, num));
    }

    @Override
    public void disconnect(String nickname) throws IllegalStateException, IllegalArgumentException {
        board.removePlayer(nickname);
        //TODO unsubscribe player's client from observers
        //   and push current state to client (possibly done in board.replaceClient())
        board.setGamePhase(GamePhase.CREATE);
    }

    @Override
    public void placeStartingCard(String nickname, boolean placeOnFront) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO PLACE A STARTING CARD DURING CREATION STATE");
    }

    @Override
    public void chooseYourColor(String nickname, PlayerColor color) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO CHOOSE YOUR COLOR DURING CREATION STATE");
    }

    @Override
    public void chooseSecretObjective(String nickname, int choice) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO CHOOSE A SECRET OBJECTIVE DURING CREATION STATE");
    }

    @Override
    public void draw(String nickname, char deckFrom, int cardPos) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO DRAW DURING CREATION STATE");
    }

    @Override
    public void placeCard(String nickname, String cardID, Point cardPos, CornerDirection cornerDir, boolean placeOnFront) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO PLACE A CARD DURING CREATION STATE");
    }

    @Override
    public void restartGame (String nickname, int numOfPlayers) throws IllegalStateException {
        throw new IllegalStateException("IMPOSSIBLE TO START ANOTHER GAME DURING CREATION STATE");
    }

}
