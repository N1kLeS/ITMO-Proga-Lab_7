package models;

import lombok.ToString;

@ToString
public class Coordinates extends AbstractModel {
    private final Integer x; //Поле не может быть null
    private final int y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toCsvString() {
        return x + "," + y;
    }

//    @Override
//    public String toString() {
//        return x + ", " + y;
//    }
}