package it.polimi.ingsw;

import it.polimi.ingsw.model.enums.CornerDirection;

//TODO When merging delete other point classes
public record Point(int row, int col) {
    /**
     * Constructs a Point as a copy of another point
     * @param other point to copy
     */
    public Point(Point other){
        this(other.row, other.col);
    }

    /**
     * @param from Point from which to move from
     * @param direction direction to move towards
     * @return the point obtained by moving in the given direction one step
     */
    public static Point move(Point from, CornerDirection direction) {
        return switch (direction) {
            default -> new Point(from.row + 1, from.col - 1);
            case TR -> new Point(from.row + 1, from.col + 1);
            case BL -> new Point(from.row - 1, from.col - 1);
            case BR -> new Point(from.row - 1, from.col + 1);
        };
    }
    /**
     * @param direction direction to move towards
     * @return the point obtained by moving from this in the given direction one step
     */
    public Point move(CornerDirection direction){
        return move(this, direction);
    }

    /**
     * @param directions ordered sequence of moves to apply to this point
     * @return the destination point obtained by applying successive moves
     */
    public Point move(CornerDirection ...directions){
        Point p = this;
        for (CornerDirection c : directions){
            p = p.move(c);
        }
        return p;
    }

    /**
     * @param p other point to add to this
     * @return Point obtained from the sum of this' and p's rows and columns
     */
    public Point add(Point p){
        return new Point(row + p.row, col + p.col);
    }

    /**
     * Compares two points in increasing order of row then column
     * @param other point to compare this to
     * @return 1 if this > other <br> 0 if this == other <br> -1 if this < other
     */
    public int compare(Point other) {
        if(row > other.row) return 1;
        if(row == other.row){
            return Integer.compare(col, other.col);
        }
        else return -1;
    }
}
