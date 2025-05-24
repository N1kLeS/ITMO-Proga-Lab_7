package controller.commands;

import models.Ticket;
import service.CollectionManager;
import ui.Command;
import ui.CommandType;
import ui.Request;
import ui.Response;

import java.util.ArrayList;

public class ShowCommand extends Command {
    private final CollectionManager collectionManager;

    public ShowCommand(CollectionManager collectionManager) {
        super("show", "вывести все существующие тикеты", true, CommandType.WITHOUT_DATA());
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        ArrayList<Ticket> elements = collectionManager.getCollection();

        if (elements == null || elements.isEmpty()) {
            return Response.warning("Коллекция пуста.");
        } else {
            return Response.success("Элементы коллекции:", elements);
        }
    }
}
