package it.polimi.ingsw.server;

import it.polimi.ingsw.model.Point;
import it.polimi.ingsw.model.enums.CornerDirection;

import java.rmi.Remote;

public interface VirtualServer {
    void connect(String nickname, VirtualClient client) throws IllegalStateException;
    void setNumOfPlayers(String nickname, VirtualClient client, int num) throws IllegalStateException;
    void disconnect(String nickname, VirtualClient client) throws IllegalStateException;
    void placeStartCard(String nickname, VirtualClient client, boolean placeOnFront) throws IllegalStateException;
    void chooseObjective(String nickname, VirtualClient client, int choice) throws IllegalStateException;
    void placeCard(String nickname, VirtualClient client, String cardID, Point placePos, CornerDirection cornerDir) throws IllegalStateException;
    void draw(String nickname, VirtualClient client, char deck, int card) throws IllegalStateException;
    void startGame(String nickname, VirtualClient client) throws IllegalStateException;
}
