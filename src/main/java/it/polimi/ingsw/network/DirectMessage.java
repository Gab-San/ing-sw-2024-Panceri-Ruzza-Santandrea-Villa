package it.polimi.ingsw.network;

import java.util.Map;

public record DirectMessage(String messenger, String addressee, String message) {
    public VirtualClient getAddresseeClient(Map<String, VirtualClient> playerClients){
        return playerClients.get(addressee);
    }
}
