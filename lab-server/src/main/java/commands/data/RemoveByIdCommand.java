package commands.data;

import models.Ticket;
import service.CollectionManager;
import ui.Command;
import ui.CommandType;
import ui.Request;
import ui.Response;

public class RemoveByIdCommand extends Command {
    private final CollectionManager collectionManager;

    public RemoveByIdCommand(CollectionManager collectionManager) {
        super("remove_by_id", "удаление тикета по id", true, CommandType.WITH_ARGUMENTS(1), true);
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        try {
            Long id = Long.parseLong(request.getArgument(0));

            Ticket ticket = collectionManager.getById(id);
            if (!ticket.getUserId().equals(request.getUser().getId())) {
                return Response.error("У вас нет прав на удаление этого билета");
            }

            if (collectionManager.removeById(id)) {
                return Response.success("Элемент с id " + id + " успешно удалён.");
            } else {
                return Response.warning("Элемент с id " + id + " не найден.");
            }
        } catch (NumberFormatException e) {
            return Response.error("Ошибка: id должен быть числом.");
        }
    }
}
