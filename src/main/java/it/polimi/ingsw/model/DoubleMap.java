package it.polimi.ingsw.model;

import java.util.HashMap;
import java.util.Map;

public class DoubleMap<V> implements DoubleMapRO<V> {
    protected final Map<Integer, Map<Integer, V>> rowMap;

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
    }

    public int getMaxRow(){
        return rowMap.keySet().stream()
                .reduce(0, (max,i)-> i>max ? i : max );
    }
    public int getMinRow(){
        return rowMap.keySet().stream()
                .reduce(0, (min,i)-> i<min ? i : min );
    }
    public int getMaxCol(){
        return rowMap.values().stream()
                .flatMap(v -> v.keySet().stream())
                .reduce(0, (max,i)-> i>max ? i : max );
    }
    public int getMinCol(){
        return rowMap.values().stream()
                .flatMap(v -> v.keySet().stream())
                .reduce(0, (min,i)-> i<min ? i : min );
    }
}