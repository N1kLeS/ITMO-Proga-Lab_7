package controller.commands;

import controller.CommandType;
import controller.ElementInputHandler;
import models.Ticket;
import service.CollectionManager;
import ui.Request;
import ui.Response;

import java.util.Optional;

public class AddIfMinCommand extends Command {
    private final CollectionManager collectionManager;
    private final ElementInputHandler inputHandler;

    public AddIfMinCommand(CollectionManager collectionManager, ElementInputHandler inputHandler) {
        super("add_if_min", "добавляет новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции", 0, CommandType.WITHOUT_ARGUMENTS);
        this.collectionManager = collectionManager;
        this.inputHandler = inputHandler;
    }

    @Override
    public Response execute(Request request) {
        Ticket newTicket = inputHandler.createTicket();

        Optional<Ticket> minTicket = collectionManager.getCollection().stream().min(Ticket::compareTo);

        if (minTicket.isEmpty() || newTicket.compareTo(minTicket.get()) < 0) {
            collectionManager.add(newTicket);
            return new Response(true, "Элемент успешно добавлен в коллекцию.");
        } else {
            return new Response(false, "Элемент не был добавлен, так как он больше или равен минимальному элементу.");
        }
    }
}
