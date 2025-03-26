package models.person;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Person implements Comparable<Person> {
    private java.util.Date birthday; //Поле не может быть null
    private int weight; //Значение поля должно быть больше 0
    private String passportID; //Поле не может быть null
    private Location location; //Поле не может быть null

    public Person(Date birthday, int weight, String passportID, Location location) {
        this.birthday = birthday;
        this.weight = weight;
        this.passportID = passportID;
        this.location = location;
    }


    public String toCsvString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String birthdayStr = (birthday != null) ? dateFormat.format(birthday) : "";

        return birthdayStr + "," +
                weight + "," +
                (passportID != null ? passportID : "") + "," +
                (location != null ? location.toCsvString() : "");
    }

    @Override
    public String toString() {
        return birthday + ", " + weight + ", " + passportID + ", " + location.toString();
    }

    @Override
    public int compareTo(Person other) {
        int birthdayComparison = birthday.compareTo(other.birthday);
        if (birthdayComparison != 0) {
            return birthdayComparison;
        }

        int weightComparison = Integer.compare(weight, other.weight);
        if (weightComparison != 0) {
            return weightComparison;
        }

        return passportID.compareTo(other.passportID);
    }
}
