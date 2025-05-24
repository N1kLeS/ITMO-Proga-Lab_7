package controller.commands;

import service.CollectionManager;
import ui.Command;
import ui.CommandType;
import ui.Request;
import ui.Response;

public class ShowCommand extends Command {
    private CollectionManager collectionManager;

    public ShowCommand(CollectionManager collectionManager) {
        super("show", "вывести все существующие тикеты", true, CommandType.WITHOUT_DATA());
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        String elements = collectionManager.getAllElements();

        if (elements == null || elements.isEmpty()) {
            return Response.warning("Коллекция пуста.");
        } else {
            return Response.success(elements, null);
        }
    }
}
