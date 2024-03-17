package it.polimi.ingsw.model;

import java.util.HashMap;
import java.util.Map;

public class DoubleMap<R, C, V> implements DoubleMapRO<R,C,V> {
    protected final Map<R, Map<C, V>> rowMap;

    public DoubleMap() {
        this.rowMap = new HashMap<>();
    }

    public V get(R row, C column) {
        Map<C, V> colMap = rowMap.get(row);
        if(colMap == null) return null;
        else return colMap.get(column);
    }
    public void put(R row, C column, V value) {
        rowMap.computeIfAbsent(row, k -> new HashMap<C, V>())
                .put(column, value);
    }
}
