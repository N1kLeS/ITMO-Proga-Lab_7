package controller.commands;

import ui.Command;
import ui.CommandType;
import service.CollectionManager;
import ui.Request;
import ui.Response;

public class SaveCommand extends Command {
    private final CollectionManager collectionManager;

    public SaveCommand(CollectionManager collectionManager) {
        super("save", "сохранение в файл формата csv", 0, CommandType.WITHOUT_ARGUMENTS);
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        if (collectionManager.saveCollection()) {
            return Response.success("Коллекция успешно сохранена.");
        } else {
            return Response.error("Ошибка при сохранении коллекции.");
        }
    }
}
