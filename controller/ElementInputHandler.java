package controller;

import models.*;
import models.person.*;

import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.Scanner;
import java.util.function.Consumer;

public class ElementInputHandler {
    private final Scanner scanner = new Scanner(System.in);

    public Ticket createTicket() {
        System.out.print("Введите имя билета: ");
        String name = inputNonEmptyString();

        System.out.print("Введите координату X: ");
        int x = inputInt();

        System.out.print("Введите координату Y: ");
        int y = inputInt();

        Coordinates coordinates = new Coordinates(x, y);

        System.out.print("Введите цену билета: ");
        int price = inputPositiveInt();

        System.out.print("Является ли билет возвратным (true/false): ");
        Boolean refundable = inputBooleanOrNull();

        System.out.print("Выберите тип билета (USUAL, BUDGETARY, CHEAP): ");
        TicketType type = inputEnum(TicketType.class);

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

    private <T extends Enum<T>> T inputEnum(Class<T> enumType) {
        while (true) {
            try {
                return Enum.valueOf(enumType, scanner.nextLine().trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Неверное значение. Доступные значения: " + java.util.Arrays.toString(enumType.getEnumConstants()));
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

    public Ticket readElement() {
        Ticket ticket = createTicket();
        return ticket;
    }

    public Person readPerson() throws ParseException {
        Person person = createPerson();
        return person;
    }

}

