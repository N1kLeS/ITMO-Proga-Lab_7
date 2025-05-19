package controller.commands;

import ui.Command;
import ui.CommandType;
import ui.Request;
import ui.Response;
import service.CollectionManager;

public class ExitCommand extends Command {
    private final CollectionManager collectionManager;
    public ExitCommand(CollectionManager collectionManager) {
        super("exit", "выходит из программы", 0, CommandType.WITHOUT_ARGUMENTS);
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        System.out.println("Завершение работы программы...");
        System.exit(0);
        return Response.success("Программа завершена.", null);
    }
}

