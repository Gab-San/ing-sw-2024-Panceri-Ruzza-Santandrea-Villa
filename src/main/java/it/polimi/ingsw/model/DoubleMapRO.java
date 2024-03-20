package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.CornerDirection;

public interface DoubleMapRO<V> {
    public V get(int row, int column);
    public int moveRow(int row, int col, CornerDirection direction);
    public int moveCol(int row, int col, CornerDirection direction);
}
