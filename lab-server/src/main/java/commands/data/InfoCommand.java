package commands.data;

import service.CollectionManager;
import ui.Command;
import ui.CommandType;
import ui.Request;
import ui.Response;

public class InfoCommand extends Command {
    private final CollectionManager collectionManager;

    public InfoCommand(CollectionManager collectionManager) {
        super("info", "вывод информации о коллекции", true, CommandType.WITHOUT_DATA(), true);
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        return Response.success("Информация о коллекции: \n" + collectionManager.getCollectionInfo(), null);
    }
}
