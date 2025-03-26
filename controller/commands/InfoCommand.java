package controller.commands;



import controller.CommandType;
import service.CollectionManager;
import ui.Request;
import ui.Response;

public class InfoCommand extends Command {
    private final CollectionManager collectionManager;

    public InfoCommand(CollectionManager collectionManager) {
        super("info", "вывод информации о коллекции", 0, CommandType.WITHOUT_ARGUMENTS);
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        return new Response(true,"Информация о коллекции: \n" + collectionManager.getCollectionInfo(), null);
    }
}
