package it.polimi.ingsw.model;

public interface DoubleMapRO<R, C, V> {
    public V get(R row, C column);
}
