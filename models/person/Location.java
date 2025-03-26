package models.person;



public class Location {
    private Integer x; //Поле не может быть null
    private Integer y; //Поле не может быть null
    private Float z; //Поле не может быть null
    private String name; //Поле не может быть null

    public Location(int x, int y, float z, String name) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
    }

    @Override
    public String toString() {
        return x + ", " + y + ", " + z + ", " + name;
    }

    public String toCsvString() {
        return (x != null ? x : "") + "," +
                (y != null ? y : "") + "," +
                (z != null ? z : "") + "," +
                (name != null ? name : "");
    }
}