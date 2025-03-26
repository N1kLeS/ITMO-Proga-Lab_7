package controller.commands;

import controller.CommandType;
import service.CollectionManager;
import ui.Request;
import ui.Response;

public class ShowCommand extends Command {
    private CollectionManager collectionManager;


    public ShowCommand(CollectionManager collectionManager) {
        super("show", "вывести все существующие тикеты", 0, CommandType.WITHOUT_ARGUMENTS);
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
//        return new Response(true, "Элементы коллекции:\n" + collectionManager.getAllElements(), null);
        String elements = collectionManager.getAllElements();

        if (elements == null || elements.isEmpty()) {
            return Response.failure("Коллекция пуста.");
        } else {
            return new Response(true, elements, null);
        }
    }
}
