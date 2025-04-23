package controller.commands;

import controller.*;
import service.CollectionManager;
import ui.*;

public class ExitCommand extends Command {
    private final CollectionManager collectionManager;
    public ExitCommand(CollectionManager collectionManager) {
        super("exit", "выходит из программы", 0, CommandType.WITHOUT_ARGUMENTS);
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        collectionManager.saveCollection();
        System.out.println("Завершение работы программы...");
        System.exit(0);
        return new Response(true, "Программа завершена.", null);
    }
}

