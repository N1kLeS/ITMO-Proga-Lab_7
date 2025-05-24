package controller.commands;

import controller.CommandHandler;
import ui.Command;
import ui.CommandType;
import ui.Request;
import ui.Response;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ExecuteScriptCommand extends Command {
    public ExecuteScriptCommand() {
        super("execute_script", "загрузка скрипта с указанного файла", 1, CommandType.WITH_ARGUMENT);
    }

    @Override
    public Response execute(Request request) {
        return Response.success("Турист выполнен!");
    }
}