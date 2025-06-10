package service;

import DataBase.TicketDAO;
import models.Ticket;

import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CollectionManager {
    private final HashSet<Ticket> collection;
    private final ZonedDateTime initializationDate;
    private final TicketDAO ticketDAO;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    public CollectionManager(TicketDAO ticketDAO) {
        this.collection = new HashSet<>();
        this.initializationDate = ZonedDateTime.now();
        this.ticketDAO = ticketDAO;
        loadCollection();
    }

    public ArrayList<Ticket> getCollection() {
        readLock.lock();
        try {
            return new ArrayList<>(collection);
        } finally {
            readLock.unlock();
        }
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
        writeLock.lock();
        try {
            ticketDAO.createTicket(ticket);
            collection.add(ticket);
        } catch (SQLException e) {
            System.out.println("Ошибка при сохранении билета в базу данных: " + e.getMessage());
        } finally {
            writeLock.unlock();
        }
    }

    //Отчистка
    public void clearCollection() {
        writeLock.lock();
        try {
            collection.clear();
        } finally {
            writeLock.unlock();
        }
    }

    //Удаление по id
    public boolean removeById(Long id) {
        writeLock.lock();
        try {
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
        } finally {
            writeLock.unlock();
        }
    }

    public Ticket getById(Long id) {
        Optional<Ticket> getTicket = collection.stream()
                .filter(ticket -> ticket.getId().equals(id))
                .findFirst();

        return getTicket.orElse(null);
    }
}