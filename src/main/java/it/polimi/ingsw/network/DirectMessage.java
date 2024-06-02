package it.polimi.ingsw.network;

/**
 * This record represents a direct message
 * @param messenger unique id of the messenger
 * @param addressee unique id of the addressee
 * @param message message to send
 */
public record DirectMessage(String messenger, String addressee, String message) {}
