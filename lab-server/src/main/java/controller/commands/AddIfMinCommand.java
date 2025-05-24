package controller.commands;

import ui.Command;
import ui.CommandType;
import models.Ticket;
import service.CollectionManager;
import ui.Request;
import ui.Response;

import java.util.Optional;

import static ui.Response.error;
import static ui.Response.success;

public class AddIfMinCommand extends Command {
    private final CollectionManager collectionManager;

    public AddIfMinCommand(CollectionManager collectionManager) {
        super("add_if_min",
              "добавляет новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции",
              true,
              CommandType.WITH_FORM(Ticket.class));
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        Ticket ticket = (Ticket) request.getData();

        Optional<Ticket> minTicket = collectionManager.getCollection().stream().min(Ticket::compareTo);

        if (minTicket.isEmpty() || ticket.compareTo(minTicket.get()) < 0) {
            collectionManager.add(ticket);
            return success("Элемент успешно добавлен в коллекцию.");
        } else {
            return error("Элемент не был добавлен, так как он больше или равен минимальному элементу.");
        }
    }
}
