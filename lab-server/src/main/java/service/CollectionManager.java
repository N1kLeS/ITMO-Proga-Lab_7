package service;

import models.Coordinates;
import models.Ticket;
import models.TicketType;
import models.person.Location;
import models.person.Person;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

public class CollectionManager {
    private final HashSet<Ticket> collection;
    private final ZonedDateTime initializationDate;
    private final String file;

    public CollectionManager(String file) {
        this.collection = new HashSet<>();
        this.initializationDate = ZonedDateTime.now();
        this.file = file;
        loadCollection(file);
    }

    public ArrayList<Ticket> getCollection() {
        return new ArrayList<>(collection);
    }

    //Автомат. загрузка из файла
    public void loadCollection(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");

                Ticket ticket = parseTicket(fields);
                if (ticket != null) {
                    collection.add(ticket);
                }
            }
            System.out.println("Коллекция успешно загружена из файла: " + fileName);
        } catch (IOException e) {
            System.out.println("Ошибка при загрузке коллекции из файла " + fileName + ": " + e.getMessage());
        }
    }

    private Ticket parseTicket(String[] fields) {
        try {
            if (fields.length < 13 || fields.length > 15) {
                throw new IllegalArgumentException("Недостаточно данных для создания объекта Ticket.");
            }

            Long id = Long.parseLong(fields[0]);
            String name = fields[1];
            Coordinates coordinates = new Coordinates(
                    Integer.parseInt(fields[2]),
                    Integer.parseInt(fields[3])
            );
            ZonedDateTime creationDate = ZonedDateTime.parse(fields[4]);
            int price = Integer.parseInt(fields[5]);
            Boolean refundable = fields[6].equals("null") ? null : Boolean.parseBoolean(fields[6]);
            TicketType type = fields[7].equals("null") ? null : TicketType.valueOf(fields[7]);
            String s = fields[8];
            Person person = new Person(
                    java.sql.Date.valueOf(s),
                    Integer.parseInt(fields[9]),
                    fields[10],
                    new Location(
                            Integer.parseInt(fields[11]),
                            Integer.parseInt(fields[12]),
                            Float.parseFloat(fields[13]),
                            fields[14]
                    )
            );

            return new Ticket(id, name, coordinates, creationDate, price, refundable, type, person);

        } catch (Exception e) {
            System.out.println("Ошибка при обработке строки: " + e.getMessage());
            return null;
        }
    }


    //Команда инфы о коллекции
    public String getCollectionInfo() {
        return String.format(
                "Тип коллекции: %s\nДата инициализации: %s\nКоличество элементов: %d",
                collection.getClass().getName(),
                initializationDate,
                collection.size()
        );
    }

    //Команда замены тикета по айди
    public boolean updateElementById(Long id, Ticket updatedTicket) {
        Optional<Ticket> ticketToUpdate = collection.stream()
                .filter(ticket -> ticket.getId().equals(id))
                .findFirst();

        if (ticketToUpdate.isPresent()) {
            collection.remove(ticketToUpdate.get());
            collection.add(updatedTicket);
            return true;
        } else {
            return false;
        }
    }

    //Добавление
    public void add(Ticket ticket) {
        collection.add(ticket);
    }

    //Отчистка
    public void clearCollection() {
        collection.clear();
    }

    //Удаление по id
    public boolean removeById(Long id) {
        Optional<Ticket> ticketToRemove = collection.stream()
                .filter(ticket -> ticket.getId().equals(id))
                .findFirst();

        if (ticketToRemove.isPresent()) {
            collection.remove(ticketToRemove.get());
            return true;
        } else {
            return false;
        }
    }

    //Сохранение в форм. csv
    public boolean saveCollection() {
        try (PrintWriter writer = new PrintWriter(file)) {
            for (Ticket ticket : collection) {
                writer.println(ticket.toCsvString());
            }
            return true;
        } catch (Exception e) {
            System.out.println("Ошибка при сохранении коллекции: " + e.getMessage());
            return false;
        }
    }
}