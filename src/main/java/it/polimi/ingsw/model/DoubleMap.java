package it.polimi.ingsw.model;

import java.util.HashMap;
import java.util.Map;

public class DoubleMap<V> implements DoubleMapRO<V> {
    protected final Map<Integer, Map<Integer, V>> rowMap;
    private int maxRow, maxCol;

    public DoubleMap() {
        this.rowMap = new HashMap<>();
    }

    public V get(int row, int column) {
        Map<Integer, V> colMap = rowMap.get(row);
        if(colMap == null) return null;
        else return colMap.get(column);
    }
    public void put(int row, int column, V value) {
        rowMap.computeIfAbsent(row, k -> new HashMap<>())
                .put(column, value);
        int absRow = Math.abs(row);
        int absCol = Math.abs(column);
        if (absRow > maxRow) maxRow = absRow;
        if (absCol > maxCol) maxCol = absCol;
    }
}