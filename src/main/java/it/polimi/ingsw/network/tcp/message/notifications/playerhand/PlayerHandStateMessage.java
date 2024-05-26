package it.polimi.ingsw.network.tcp.message.notifications.playerhand;

import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.tcp.message.notifications.player.PlayerMessage;

import java.io.Serial;
import java.rmi.RemoteException;
import java.util.List;

public class PlayerHandStateMessage extends PlayerMessage {
    @Serial
    private static final long serialVersionUID = 1561927360L;
    private final List<String> playCards;
    private final List<String> objectiveCards;
    private final String startingCard;
    public PlayerHandStateMessage(String nickname, List<String> playCards,
                                  List<String> objectiveCards, String startingCard) {
        super(nickname);
        this.playCards = playCards;
        this.objectiveCards = objectiveCards;
        this.startingCard = startingCard;
    }

    @Override
    public void execute(VirtualClient virtualClient) throws RemoteException {
        virtualClient.setPlayerHandState(nickname, playCards, objectiveCards, startingCard);
    }
}
