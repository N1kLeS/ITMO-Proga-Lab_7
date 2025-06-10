package models;

import authentication.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Data
public class Ticket extends AbstractModel {
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    @Getter
    private final String name; //Поле не может быть null, Строка не может быть пустой
    @Getter
    private final Coordinates coordinates; //Поле не может быть null
    private final java.time.ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    @Getter
    private final int price; //Значение поля должно быть больше 0
    @Getter
    private final Boolean refundable; //Поле может быть null
    @Getter
    private final TicketType type; //Поле может быть null
    private final Person person; //Поле не может быть null

    @Getter
    @Setter
    private User ownerUser;
    private Long userId;

    public Ticket(String name, Coordinates coordinates, ZonedDateTime creationDate,
                  int price, Boolean refundable, TicketType type, Person person) {
        this(null, name, coordinates, creationDate, price, refundable, type, person);
    }

    public Ticket(Long id,
                  String name,
                  Coordinates coordinates,
                  ZonedDateTime creationDate,
                  int price,
                  Boolean refundable,
                  TicketType type,
                  Person person) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.price = price;
        this.refundable = refundable;
        this.type = type;
        this.person = person;
    }

    public Ticket(Long id, String name, Coordinates coordinates, ZonedDateTime creationDate, int price, Boolean refundable, TicketType ticketType, Person person, long userID) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.price = price;
        this.refundable = refundable;
        this.type = ticketType;
        this.person = person;
        this.userId = userID;
    }

    @Override
    public String toString() {
        return String.format(
            "Ticket(id=%s, name=%s, coordinates=%s, creationDate=%s, price=%d, refundable=%s, type=%s, person=%s, userId=%s)",
            id,
            name,
            coordinates,
            creationDate,
            price,
            refundable,
            type,
            person,
            userId
        );
    }

    public String toCsvString() {
        return id + "," + name + "," + coordinates.toCsvString() + "," +
                creationDate + "," + price + "," +
                (refundable != null ? refundable : "") + "," +
                type + "," +
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

    public Comparable<Person> getPerson() {
        return person;
    }

    public Object getCreationDate() {
        return creationDate;
    }
}
