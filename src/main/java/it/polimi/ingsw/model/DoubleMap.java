package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.CornerDirection;

import java.util.*;

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
     * @param row  starting row
     * @param direction  direction to move towards
     * @return row value after movement in direction
     */
    @Override
    public int moveRow(int row, CornerDirection direction) {
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

    /**
     * @param col  starting column
     * @param direction  direction to move towards
     * @return col value after movement in direction
     */
    @Override
    public int moveCol(int col, CornerDirection direction) {
        switch (direction) {
            case TL:
            case BL:
                col += -1;
                break;
            case TR:
            case BR:
                col += 1;
                break;
        }
        return col;
    }

    public V getOffset(int row, int col, CornerDirection direction){
        int offCol = moveCol(col, direction);
        int offRow = moveRow(row, direction);
        return get(offRow, offCol);
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