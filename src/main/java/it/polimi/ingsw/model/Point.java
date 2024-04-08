package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.CornerDirection;

public record Point(int row, int col) {
    public Point(Point other){
        this(other.row, other.col);
    }

    public static Point move(Point from, CornerDirection direction) {
        return switch (direction) {
            default -> new Point(from.row + 1, from.col - 1);
            case TR -> new Point(from.row + 1, from.col + 1);
            case BL -> new Point(from.row - 1, from.col - 1);
            case BR -> new Point(from.row - 1, from.col + 1);
        };
    }
    public Point move(CornerDirection direction){
        return move(this, direction);
    }

    public Point move(CornerDirection ...directions){
        Point p = this;
        for (CornerDirection c : directions){
            p = p.move(c);
        }
        return p;
    }
    public Point add(Point p){
        return new Point(row + p.row, col + p.col);
    }
}
