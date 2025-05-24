package models;

import models.person.Person;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;

public class Ticket implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private int price; //Значение поля должно быть больше 0
    private Boolean refundable; //Поле может быть null
    private TicketType type; //Поле может быть null
    private Person person; //Поле не может быть null


    public Ticket(String name, Coordinates coordinates, ZonedDateTime creationDate,
                  int price, Boolean refundable, TicketType type, Person person) {
        this(null, name, coordinates, creationDate, price, refundable, type, person);
    }

    public Ticket(Long id, String name, Coordinates coordinates, ZonedDateTime creationDate, int price, Boolean refundable, TicketType type, Person person) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.price = price;
        this.refundable = refundable;
        this.type = type;
        this.person = person;
    }

    @Override
    public String toString() {
        return String.format("Ticket { id=%d, name='%s', coordinates=%s, creationDate=%s, price=%d, refundable=%s, type=%s, person=%s }",
                id, name, coordinates, creationDate, price, refundable, type, person);
    }

    public Object getId() {
        return id;
    }

    public String toCsvString() {
        return id + "," + name + "," + coordinates.toCsvString() + "," +
                creationDate + "," + price + "," +
                (refundable != null ? refundable : "") + "," +
                type  + "," +
                person.toCsvString();
    }

    public int compareTo(Ticket other) {
        int priceComparison = Integer.compare(this.price, other.price);
        if (priceComparison != 0) {
            return priceComparison;
        }

        int nameComparison = this.name.compareTo(other.name);
        if (nameComparison != 0) {
            return nameComparison;
        }

        return this.id.compareTo(other.id);
    }

    public void setId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Некорректный ID");
        }
        this.id = id;
    }

    public TicketType getType() {
        return type;
    }

    public Boolean getRefundable() {
        return refundable;
    }

    public Comparable<Person> getPerson() {
        return person;
    }
}
