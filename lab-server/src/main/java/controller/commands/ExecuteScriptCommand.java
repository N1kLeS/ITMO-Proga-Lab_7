package controller.commands;

import ui.Command;
import ui.CommandType;
import ui.Request;
import ui.Response;

public class ExecuteScriptCommand extends Command {
    public ExecuteScriptCommand() {
        super("execute_script", "загрузка скрипта с указанного файла", false, CommandType.WITH_ARGUMENTS(1));
    }

    @Override
    public Response execute(Request request) {
        return Response.success("Скрипт выполнен!");
    }
}