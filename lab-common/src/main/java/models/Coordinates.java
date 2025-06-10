package models;

import lombok.Getter;
import lombok.ToString;

@ToString
public class Coordinates extends AbstractModel {
    private final Integer x; //Поле не может быть null
    @Getter
    private final int y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toCsvString() {
        return x + "," + y;
    }

    public int getX() {
        return x;
    }

    //    @Override
//    public String toString() {
//        return x + ", " + y;
//    }
}