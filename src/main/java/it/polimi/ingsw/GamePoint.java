package it.polimi.ingsw;

import java.util.List;

/**
 * Record representing 2D coordinates. <br>
 * Includes methods to conveniently move using CornerDirections.
 * @param row the y coordinate
 * @param col the x coordinate
 */
public record GamePoint(int row, int col) {
    /**
     * Constructs a Point as a copy of another point
     * @param other point to copy
     */
    public GamePoint(GamePoint other){
        this(other.row, other.col);
    }

    /**
     * Returns a point that represents the movement in the desired direction by one step.
     * @param from Point from which to move from
     * @param direction direction to move towards
     * @return the point obtained by moving in the given direction one step
     */
    public static GamePoint move(GamePoint from, CornerDirection direction) {
        return switch (direction) {
            default -> new GamePoint(from.row + 1, from.col - 1);
            case TR -> new GamePoint(from.row + 1, from.col + 1);
            case BL -> new GamePoint(from.row - 1, from.col - 1);
            case BR -> new GamePoint(from.row - 1, from.col + 1);
        };
    }
    /**
     * Returns the point obtained by moving from this in the given direction one step.
     * @param direction direction to move towards
     * @return the point obtained by moving from this in the given direction one step
     */
    public GamePoint move(CornerDirection direction){
        return move(this, direction);
    }

    /**
     * Returns the point obtained by applying successive moves.
     * @param directions ordered sequence of moves to apply to this point
     * @return the destination point obtained by applying successive moves
     */
    public GamePoint move(CornerDirection...directions){
        GamePoint p = this;
        for (CornerDirection c : directions){
            p = p.move(c);
        }
        return p;
    }
    /**
     * Returns destination point obtained by applying successive moves.
     * @param directions ordered sequence of moves to apply to this point
     * @return the destination point obtained by applying successive moves
     */
    public GamePoint move(List<CornerDirection> directions){
        GamePoint p = this;
        for (CornerDirection c : directions){
            p = p.move(c);
        }
        return p;
    }

    @Override
    public boolean equals(Object other){
        if(other == this) return true;
        if(other instanceof GamePoint otherPoint){
            return otherPoint.row == this.row &&
                    otherPoint.col == this.col;
        }
        else return false;
    }

    /**
     * Returns a point that is the sum of this' and p's rows and columns.
     * @param p other point to add to this
     * @return Point obtained from the sum of this' and p's rows and columns
     */
    public GamePoint add(GamePoint p){
        return new GamePoint(row + p.row, col + p.col);
    }

    /**
     * Compares two points in increasing order of row then column
     * @param other point to compare this to
     * @return 1 if this > other <br> 0 if this == other <br> -1 if this < other
     */
    public int compare(GamePoint other) {
        if(row > other.row) return 1;
        if(row == other.row){
            return Integer.compare(col, other.col);
        }
        else return -1;
    }

    @Override
    public String toString(){
        return "(" + col + "," + row + ")";
    }
}
