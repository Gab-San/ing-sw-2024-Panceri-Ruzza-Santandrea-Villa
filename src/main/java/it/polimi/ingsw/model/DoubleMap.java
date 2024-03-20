package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.CornerDirection;

import java.util.HashMap;
import java.util.Map;

public class DoubleMap<V> implements DoubleMapRO<V> {
    protected final Map<Integer, Map<Integer, V>> rowMap;

    public DoubleMap() {
        this.rowMap = new HashMap<>();
    }

    @Override
    public V get(int row, int column) {
        Map<Integer, V> colMap = rowMap.get(row);
        if(colMap == null) return null;
        else return colMap.get(column);
    }

    /**
     * !! IMPORTANT !!
     * Always call this after moveCol as row is a key factor for that movement
     * @param row  starting row
     * @param col  starting column
     * @param direction  direction to move towards
     * @return row value after movement
     */
    @Override
    public int moveRow(int row, int col, CornerDirection direction) {
        switch (direction){
            case TL:
            case TR:
                row += 1;
                break;
            case BR:
            case BL:
                row += -1;
                break;
        }
        return row;
    }

    @Override
    public int moveCol(int row, int col, CornerDirection direction) {
        switch (direction){
            case TL:
            case BL:
                col += (row%2 == 0) ? 0 : -1;
                break;
            case TR:
            case BR:
                col += (row%2 == 0) ? 1 : 0;
                break;
        }
        return col;
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