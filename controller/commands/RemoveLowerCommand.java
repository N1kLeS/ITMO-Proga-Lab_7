package controller.commands;


import controller.CommandType;
import controller.ElementInputHandler;
import models.Ticket;
import service.CollectionManager;
import ui.Request;
import ui.Response;


public class RemoveLowerCommand extends Command {
    private final CollectionManager collectionManager;
    private final ElementInputHandler inputHandler;

    public RemoveLowerCommand(CollectionManager collectionManager, ElementInputHandler inputHandler) {
        super("remove_lower", "удаляет из коллекции все элементы, меньшие, чем заданный", 0, CommandType.WITHOUT_ARGUMENTS);
        this.collectionManager = collectionManager;
        this.inputHandler = inputHandler;
    }

    @Override
    public Response execute(Request request) {

        System.out.println("Введите данные для элемента:");
        Ticket ticket = inputHandler.readElement();

        int initialSize = collectionManager.getCollection().size();
        collectionManager.getCollection().removeIf(existingTicket -> existingTicket.compareTo(ticket) < 0);
        int removedCount = initialSize - collectionManager.getCollection().size();

        return new Response(true, "Удалено элементов: " + removedCount, null);
    }
}
