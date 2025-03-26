package controller.commands;

import controller.CommandType;
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
            return new Response(true, "Коллекция успешно сохранена.");
        } else {
            return new Response(false, "Ошибка при сохранении коллекции.");
        }
    }
}
