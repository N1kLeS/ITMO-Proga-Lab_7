package controller.commands;

import controller.CommandType;
import service.CollectionManager;
import ui.Request;
import ui.Response;

public class RemoveByIdCommand extends Command {
    private final CollectionManager collectionManager;

    public RemoveByIdCommand(CollectionManager collectionManager) {
        super("remove_by_id", "удаление тикета по id", 1, CommandType.WITH_ARGUMENTS);
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        try {
            Long id = Long.parseLong(request.getArgument(0));
            if (collectionManager.removeById(id)) {
                return new Response(true, "Элемент с id " + id + " успешно удалён.");
            } else {
                return new Response(false, "Элемент с id " + id + " не найден.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: id должен быть числом.");
        }
        return new Response(true, "", null);
    }
}
