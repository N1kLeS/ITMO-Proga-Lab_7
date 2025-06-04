package commands.data;

import authentication.User;
import models.Ticket;
import service.CollectionManager;
import ui.Command;
import ui.CommandType;
import ui.Request;
import ui.Response;

public class AddCommand extends Command {
    final CollectionManager collectionManager;

    public AddCommand(CollectionManager collectionManager) {
        super("add", "добавляет новый элемент в коллекцию", true, CommandType.WITH_FORM(Ticket.class), true);
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        Ticket ticket = (Ticket) request.getData();

        User user = request.getUser();
        ticket.setOwnerUser(user);

        ticket.setId(collectionManager.generateId());

        collectionManager.add(ticket);

        return Response.success("Элемент успешно добавлен!");
    }
}


