package commands.data;

import models.Ticket;
import service.CollectionManager;
import ui.Command;
import ui.CommandType;
import ui.Request;
import ui.Response;

public class RemoveLowerCommand extends Command {
    private final CollectionManager collectionManager;

    public RemoveLowerCommand(CollectionManager collectionManager) {
        super("remove_lower",
              "удаляет из коллекции все элементы, меньшие, чем заданный",
              true,
              CommandType.WITH_FORM(Ticket.class),
              true
        );
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        Ticket ticket = (Ticket) request.getData();

        Long id = Long.parseLong(request.getArgument(0));
        Ticket ticket1 = collectionManager.getById(id);
        if (!ticket1.getUserId().equals(request.getUser().getId())) {
            return Response.error("У вас нет прав на удаление этого билета");
        }

        int initialSize = collectionManager.getCollection().size();
        collectionManager.getCollection().removeIf(existingTicket -> existingTicket.compareTo(ticket) < 0);
        int removedCount = initialSize - collectionManager.getCollection().size();

        return Response.success("Удалено элементов: " + removedCount);
    }
}
