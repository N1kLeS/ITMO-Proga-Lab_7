package models;

import java.io.Serial;
import java.io.Serializable;

public class Coordinates extends AbstractModel {
    private Integer x; //Поле не может быть null
    private int y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toCsvString() {
        return x + "," + y;
    }

    @Override
    public String toString() {
        return x + ", " + y;
    }
}