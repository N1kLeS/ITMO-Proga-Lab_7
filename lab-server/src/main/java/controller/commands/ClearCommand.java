package controller.commands;

import controller.CommandType;
import service.CollectionManager;
import ui.Request;
import ui.Response;

public class ClearCommand extends Command {
    private CollectionManager collectionManager;

    public ClearCommand(CollectionManager collectionManager) {
        super("clear", "команда очистки коллекции.", 0, CommandType.WITHOUT_ARGUMENTS);
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        collectionManager.clearCollection();
        return new Response(true, "Коллекция успешно очищена.", null);
    }
}
