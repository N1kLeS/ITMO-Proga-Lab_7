package models.person;

import lombok.ToString;
import models.AbstractModel;

import java.text.SimpleDateFormat;
import java.util.Date;

@ToString
public class Person extends AbstractModel implements Comparable<Person> {
    private final java.util.Date birthday; //Поле не может быть null
    private final int weight; //Значение поля должно быть больше 0
    private final String passportID; //Поле не может быть null
    private final Location location; //Поле не может быть null

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

//    @Override
//    public String toString() {
//        return birthday + ", " + weight + ", " + passportID + ", " + location.toString();
//    }

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
