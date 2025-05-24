package controller.commands;

import ui.Command;
import ui.CommandType;
import service.CollectionManager;
import ui.Request;
import ui.Response;

public class ClearCommand extends Command {
    private final CollectionManager collectionManager;

    public ClearCommand(CollectionManager collectionManager) {
        super("clear", "команда очистки коллекции.", true, CommandType.WITHOUT_DATA());
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        collectionManager.clearCollection();
        return Response.success("Коллекция успешно очищена.", null);
    }
}
