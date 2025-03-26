package controller.commands;

import controller.*;
import service.CollectionManager;
import ui.*;

public class ExitCommand extends Command {

    public ExitCommand() {
        super("exit", "выходит из программы", 0, CommandType.WITHOUT_ARGUMENTS);
    }

    @Override
    public Response execute(Request request) {
        System.out.println("Завершение работы программы...");
        System.exit(0);
        return new Response(true, "Программа завершена.", null);
    }
}

