package models;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class Location extends AbstractModel {
    private final Integer x; //Поле не может быть null
    private final Integer y; //Поле не может быть null
    private final Float z; //Поле не может быть null
    private final String name; //Поле не может быть null

    public Location(int x, int y, float z, String name) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
    }

//    @Override
//    public String toString() {
//        return x + ", " + y + ", " + z + ", " + name;
//    }

    public String toCsvString() {
        return (x != null ? x : "") + "," +
                (y != null ? y : "") + "," +
                (z != null ? z : "") + "," +
                (name != null ? name : "");
    }
}