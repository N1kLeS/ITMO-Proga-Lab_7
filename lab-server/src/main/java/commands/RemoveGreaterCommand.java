package commands;

import models.Ticket;
import service.CollectionManager;
import ui.Command;
import ui.CommandType;
import ui.Request;
import ui.Response;

public class RemoveGreaterCommand extends Command {
    private final CollectionManager collectionManager;

    public RemoveGreaterCommand(CollectionManager collectionManager) {
        super("remove_greater",
              "удаляет из коллекции все элементы, превышающие заданный",
              true,
              CommandType.WITH_FORM(Ticket.class));
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        Ticket ticket = (Ticket) request.getData();

        int initialSize = collectionManager.getCollection().size();
        collectionManager.getCollection().removeIf(existingTicket -> existingTicket.compareTo(ticket) > 0);
        int removedCount = initialSize - collectionManager.getCollection().size();

        return Response.success("Удалено элементов: " + removedCount);
    }
}