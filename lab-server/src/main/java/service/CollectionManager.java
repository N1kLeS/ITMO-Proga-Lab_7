package service;

import DataBase.TicketDAO;
import models.Ticket;

import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

public class CollectionManager {
    private final HashSet<Ticket> collection;
    private final ZonedDateTime initializationDate;
    private final TicketDAO ticketDAO;

    public CollectionManager(TicketDAO ticketDAO) {
        this.collection = new HashSet<>();
        this.initializationDate = ZonedDateTime.now();
        this.ticketDAO = ticketDAO;
        loadCollection();
    }

    public ArrayList<Ticket> getCollection() {
        return new ArrayList<>(collection);
    }

    //Автомат. загрузка из файла
    public void loadCollection() {
        try {
            collection.addAll(ticketDAO.getAllTickets());
            System.out.println("Коллекция успешно загружена из базы данных");
        } catch (SQLException e) {
            System.out.println("Ошибка при загрузке коллекции из базы данных: " + e.getMessage());
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
            try {
                ticketDAO.updateTicket(updatedTicket);
                collection.remove(ticketToUpdate.get());
                collection.add(updatedTicket);
                return true;
            } catch (SQLException e) {
                System.out.println("Ошибка при обновлении билета в базе данных: " + e.getMessage());
                return false;
            }
        } else {
            return false;
        }
    }

    //Добавление
    public void add(Ticket ticket) {
        try {
            ticketDAO.createTicket(ticket);
            collection.add(ticket);
        } catch (SQLException e) {
            System.out.println("Ошибка при сохранении билета в базу данных: " + e.getMessage());
        }
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
            try {
                ticketDAO.deleteTicket(id);
                collection.remove(ticketToRemove.get());
                return true;
            } catch (SQLException e) {
                System.out.println("Ошибка при удалении билета из базы данных: " + e.getMessage());
                return false;
            }
        } else {
            return false;
        }
    }

    public Ticket getById(Long id) {
        Optional<Ticket> getTicket = collection.stream()
                .filter(ticket -> ticket.getId().equals(id))
                .findFirst();

        return getTicket.orElse(null);
    }
}