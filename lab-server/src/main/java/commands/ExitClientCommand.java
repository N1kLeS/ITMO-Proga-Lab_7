package commands;

import ui.Command;
import ui.CommandType;
import ui.Request;
import ui.Response;

public class ExitClientCommand extends Command {
    public ExitClientCommand() {
        super("exit", "выходит из клиента", false, CommandType.WITHOUT_DATA());
    }

    @Override
    public Response execute(Request request) {
        return Response.success("Работа клиент завершена.", null);
    }
}