package controller.commands;

import ui.Command;
import ui.CommandType;
import models.TicketType;
import service.CollectionManager;
import ui.Request;
import ui.Response;

public class CountGreaterThanTypeCommand extends Command {
    private final CollectionManager collectionManager;

    public CountGreaterThanTypeCommand(CollectionManager collectionManager) {
        super("count_greater_than_type", "выводит количество элементов, значение поля type (USUAL, BUDGETARY, CHEAP) которых больше заданного", 1, CommandType.WITH_ARGUMENT);
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        try {
            TicketType type = TicketType.valueOf(request.getArgument(0).toUpperCase());

            long count = collectionManager.getCollection().stream().filter(ticket -> ticket.getType() != null && ticket.getType().compareTo(type) > 0).count();

            return Response.success("Количество элементов, значение поля type которых больше " + type + ": " + count);
        } catch (IllegalArgumentException e) {
            return Response.error("Неверное значение типа. Доступные значения: USUAL, BUDGETARY, CHEAP.");
        }
    }
}
