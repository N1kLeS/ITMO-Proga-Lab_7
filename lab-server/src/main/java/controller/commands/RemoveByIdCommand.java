package controller.commands;

import ui.Command;
import ui.CommandType;
import service.CollectionManager;
import ui.Request;
import ui.Response;

public class RemoveByIdCommand extends Command {
    private final CollectionManager collectionManager;

    public RemoveByIdCommand(CollectionManager collectionManager) {
        super("remove_by_id", "удаление тикета по id", 1, CommandType.WITH_ARGUMENT);
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        try {
            Long id = Long.parseLong(request.getArgument(0));
            if (collectionManager.removeById(id)) {
                return Response.success("Элемент с id " + id + " успешно удалён.");
            } else {
                return Response.warning("Элемент с id " + id + " не найден.");
            }
        } catch (NumberFormatException e) {
            return Response.error( "Ошибка: id должен быть числом.");
        }
    }
}
