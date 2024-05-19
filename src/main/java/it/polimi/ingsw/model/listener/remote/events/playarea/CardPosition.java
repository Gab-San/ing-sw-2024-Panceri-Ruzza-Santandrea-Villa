package it.polimi.ingsw.model.listener.remote.events.playarea;

import java.io.Serial;
import java.io.Serializable;

public record CardPosition(int row, int col, String cardId) implements Serializable {}
