package utils;

import models.AbstractModel;
import models.Coordinates;
import models.Ticket;
import models.TicketType;
import models.person.Location;
import models.person.Person;

import java.time.ZonedDateTime;
import java.util.Scanner;

public class ElementInputHandler {
    private final Scanner scanner;

    public ElementInputHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    public Object readValue(Class<? extends AbstractModel> type) {
        if (type == Ticket.class) {
            return createTicket();
        } else if (type == Person.class) {
            return createPerson();
        } else if (type == Coordinates.class) {
            return createCoordinates();
        } else if (type == Location.class) {
            return createLocation();
        }

        throw new IllegalArgumentException("Unsupported type: " + type.getName());
    }

    private Ticket createTicket() {
        System.out.print("Введите имя билета: ");
        String name = inputNonEmptyString();

        Coordinates coordinates = createCoordinates();

        System.out.print("Введите цену билета: ");
        int price = inputPositiveInt();

        System.out.print("Является ли билет возвратным (true/false): ");
        Boolean refundable = inputBooleanOrNull();

        System.out.print("Выберите тип билета (USUAL, BUDGETARY, CHEAP): ");
        TicketType type = inputEnum();

        Person person = createPerson();

        return new Ticket(
                generateId(),
                name,
                coordinates,
                ZonedDateTime.now(),
                price,
                refundable,
                type,
                person
        );
    }

    private Coordinates createCoordinates() {
        System.out.print("Введите координату X: ");
        int x = inputInt();

        System.out.print("Введите координату Y: ");
        int y = inputInt();

        return new Coordinates(x, y);
    }

    private Person createPerson() {
        System.out.print("Введите дату рождения (формат: yyyy-MM-dd): ");
        java.util.Date birthday = inputDate();

        System.out.print("Введите вес: ");
        int weight = inputPositiveInt();

        System.out.print("Введите ID паспорта: ");
        String passportID = inputNonEmptyString();

        System.out.println("Введите данные о местоположении: ");
        Location location = createLocation();

        return new Person(birthday, weight, passportID, location);
    }

    private Location createLocation() {
        System.out.print("Введите координату X: ");
        int x = inputInt();

        System.out.print("Введите координату Y: ");
        int y = inputInt();

        System.out.print("Введите координату Z: ");
        float z = inputFloat();

        System.out.print("Введите имя местоположения: ");
        String name = inputNonEmptyString();

        return new Location(x, y, z, name);
    }

    private String inputNonEmptyString() {
        String input;
        while (true) {
            input = scanner.nextLine().trim();
            if (!input.isEmpty()) break;
            System.out.println("Строка не может быть пустой. Попробуйте снова: ");
        }
        return input;
    }

    private int inputInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Введите целое число: ");
            }
        }
    }

    private int inputPositiveInt() {
        int value;
        do {
            value = inputInt();
            if (value > 0) break;
            System.out.print("Число должно быть больше 0. Попробуйте снова: ");
        } while (true);
        return value;
    }

    private Float inputFloat() {
        while (true) {
            try {
                return Float.parseFloat(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Введите число с плавающей точкой: ");
            }
        }
    }

    private Boolean inputBooleanOrNull() {
        String input;
        while (true) {
            input = scanner.nextLine().trim();
            if (input.isEmpty()) return null;
            if (input.equalsIgnoreCase("true")) return true;
            if (input.equalsIgnoreCase("false")) return false;
            System.out.print("Введите true, false или оставьте поле пустым: ");
        }
    }

    private <T extends Enum<T>> T inputEnum() {
        while (true) {
            try {
                return Enum.valueOf((Class<T>) TicketType.class, scanner.nextLine().trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Неверное значение. Доступные значения: " + java.util.Arrays.toString(((Class<T>) TicketType.class).getEnumConstants()));
            }
        }
    }

    private java.util.Date inputDate() {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                return java.sql.Date.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.print("Введите дату в формате yyyy-MM-dd: ");
            }
        }
    }

    private long generateId() {
        return System.currentTimeMillis();
    }
}

