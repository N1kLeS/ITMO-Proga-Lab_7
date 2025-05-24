package controller.commands;

import ui.Command;
import ui.CommandType;
import models.Ticket;
import service.CollectionManager;
import ui.Request;
import ui.Response;

public class AddCommand extends Command {
    CollectionManager collectionManager;

    public AddCommand(CollectionManager collectionManager) {
        super("add", "добавляет новый элемент в коллекцию", 0, CommandType.WITH_FORM);
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        Ticket ticket = (Ticket) request.getData();

        if (collectionManager != null) {
            System.out.println("Добавление нового элемента в коллекцию...");
            collectionManager.add(ticket);
        }

        return Response.success( "Элемент успешно добавлен!", null);
    }
}


