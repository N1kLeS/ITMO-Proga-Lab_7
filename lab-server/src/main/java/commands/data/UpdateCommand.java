package commands.data;

import models.Ticket;
import service.CollectionManager;
import ui.Command;
import ui.CommandType;
import ui.Request;
import ui.Response;

public class UpdateCommand extends Command {
    private final CollectionManager collectionManager;

    public UpdateCommand(CollectionManager collectionManager) {
        super("update", "обновление билета по id", true, CommandType.WITH_ARGUMENTS(1), true);
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        try {
            Long id = Long.parseLong(request.getArgument(0));
            Ticket ticket = (Ticket) request.getData();
            ticket.setId(id);

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

