package controller.commands;

import controller.CommandType;
import controller.ElementInputHandler;
import models.*;
import service.CollectionManager;
import ui.Request;
import ui.Response;

public class UpdateCommand extends Command {
    private final CollectionManager collectionManager;
    private final ElementInputHandler inputHandler;

    public UpdateCommand(CollectionManager collectionManager, ElementInputHandler inputHandler) {
        super("update", "обновление билета по id", 1, CommandType.WITH_ARGUMENTS);
        this.collectionManager = collectionManager;
        this.inputHandler = inputHandler;
    }

    @Override
    public Response execute(Request request) {
        try {
            Long id = Long.parseLong(request.getArgument(0));
            System.out.println("Создание нового объекта для обновления...");
            Ticket updatedTicket = inputHandler.createTicket();

            if (collectionManager.updateElementById(id, updatedTicket)) {
                return new Response(true, "Элемент с id " + id + " успешно обновлён.");
            } else {
                return new Response(false, "Элемент с id " + id + " не найден.");
            }
        } catch (NumberFormatException e) {
            return new Response(false, "Ошибка: id должен быть числом.");
        }
    }
}

