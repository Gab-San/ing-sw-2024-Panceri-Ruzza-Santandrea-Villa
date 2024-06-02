package it.polimi.ingsw.network;

/**
 * This record represents a broadcast message
 * @param messenger unique id of the messenger
 * @param message message to send
 */
public record BroadcastMessage(String messenger, String message) {}
