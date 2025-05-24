package controller.commands;

import ui.Command;
import ui.CommandType;
import models.Ticket;
import service.CollectionManager;
import ui.Request;
import ui.Response;

public class RemoveGreaterCommand extends Command {
    private final CollectionManager collectionManager;

    public RemoveGreaterCommand(CollectionManager collectionManager) {
        super("remove_greater", "удаляет из коллекции все элементы, превышающие заданный", 0, CommandType.WITH_FORM);
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        System.out.println("Введите данные для элемента:");
        Ticket ticket = (Ticket) request.getData();

        int initialSize = collectionManager.getCollection().size();
        collectionManager.getCollection().removeIf(existingTicket -> existingTicket.compareTo(ticket) > 0);
        int removedCount = initialSize - collectionManager.getCollection().size();

        return Response.success("Удалено элементов: " + removedCount);
    }
}