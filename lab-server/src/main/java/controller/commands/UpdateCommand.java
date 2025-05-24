package controller.commands;

import ui.Command;
import ui.CommandType;
import models.Ticket;
import service.CollectionManager;
import ui.Request;
import ui.Response;

public class UpdateCommand extends Command {
    private final CollectionManager collectionManager;

    public UpdateCommand(CollectionManager collectionManager) {
        super("update", "обновление билета по id", 1, CommandType.WITH_ARGUMENT);
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        try {
            Long id = Long.parseLong(request.getArgument(0));
            System.out.println("Создание нового объекта для обновления...");

            Ticket ticket = (Ticket) request.getData();

            if (collectionManager.updateElementById(id, ticket)) {
                return Response.success("Элемент с id " + id + " успешно обновлён.");
            } else {
                return Response.warning("Элемент с id " + id + " не найден.");
            }
        } catch (NumberFormatException e) {
            return Response.error("Ошибка: id должен быть числом.");
        }
    }
}

