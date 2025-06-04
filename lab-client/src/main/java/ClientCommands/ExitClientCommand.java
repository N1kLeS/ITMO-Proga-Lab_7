package ClientCommands;

import ui.Command;
import ui.CommandType;
import ui.Request;
import ui.Response;


public class ExitClientCommand extends Command {
    public ExitClientCommand() {
        super("exit", "выходит из клиента", false, CommandType.WITHOUT_DATA(), false);
    }

    @Override
    public Response execute(Request request) {
        System.out.println("Завершение работы клиента...");
        System.exit(0);
        return Response.success("Работа клиент завершена.", null);
    }
}

