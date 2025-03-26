package controller;

import controller.commands.Command;
import ui.Request;
import ui.Response;

import java.util.HashMap;
import java.util.Map;

public class CommandHandler {
    private static final Map<String, Command> commands = new HashMap<>();


    public void register(Command command) {
        commands.put(Command.getName(), command);
    }

    public Response execute(Command command, Request request) {
        CommandType commandType = command.getCommandType();
        String[] args = request.getCommandArgs();

        if (commandType == CommandType.WITHOUT_ARGUMENTS) {
            if (args.length == 0) {
                return command.execute(request);
            }
            return new Response(false, "Команда не принимает аргументы.", null);
        }

        if (commandType.getArgs() == args.length || commandType.getArgs() == -1) {
            return command.execute(request, null);
        }


        return new Response(false, "Неверное количество аргументов для команды: " + command.getName(), null);
    }

    // Вывод всех зарегистрированных команд с их описаниями
    public static String getCommands() {
        for (Command command : commands.values()) {
            return command.getName() + ": " + command.getInfo();
        }
        return "";
    }

    public static Command getCommand(String commandName) {
        return commands.get(commandName);
    }
}
