package controller.commands;

import ui.Command;
import ui.CommandType;
import controller.ElementInputHandler;
import models.Ticket;
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
                return Response.success("Элемент с id " + id + " успешно обновлён.");
            } else {
                return Response.warning("Элемент с id " + id + " не найден.");
            }
        } catch (NumberFormatException e) {
            return Response.error("Ошибка: id должен быть числом.");
        }
    }
}

