package controller.commands;

import controller.CommandType;
import models.*;
import service.CollectionManager;
import controller.ElementInputHandler;
import ui.Request;
import ui.Response;

public class AddCommand extends Command {
    ElementInputHandler inputHandler;
    CollectionManager collectionManager;

    public AddCommand(CollectionManager collectionManager, ElementInputHandler inputHandler) {
        super("add", "добавляет новый элемент в коллекцию", 0, CommandType.WITHOUT_ARGUMENTS);
        this.collectionManager = collectionManager;
        this.inputHandler = inputHandler;
    }

    @Override
    public Response execute(Request request) {
        if (collectionManager != null) {
            System.out.println("Добавление нового элемента в коллекцию...");

            Ticket ticket = inputHandler.createTicket();
            collectionManager.add(ticket);
        }

        return new Response(true, "Элемент успешно добавлен!", null);
    }
}


