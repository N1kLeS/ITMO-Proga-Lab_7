package controller;

import models.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ui.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommandHandler {
    private static final Logger logger = LogManager.getLogger(CommandHandler.class);
    private final Map<String, Command> commands = new HashMap<>();


    public void register(Command command) {
        commands.put(command.getName(), command);
        logger.debug("Registered command: {}", command.getName());
    }

    public Response execute(Command command, Request request) {
        CommandType commandType = command.getCommandType();
        String[] args = request.getCommandArgs();


        if (commandType == CommandType.WITHOUT_ARGUMENTS) {
            if (args.length == 0) {
                return command.execute(request);
            }
            return Response.error("Команда не принимает аргументы.");
        }

        if (commandType.getArgumentCount() == args.length || commandType.getArgumentCount() == -1) {
            return command.execute(request);
        }

        return Response.warning("Неверное количество аргументов для команды: " + command.getName());
    }

    public ArrayList<Command> getCommands() {
        ArrayList<Command> commands = new ArrayList<>(this.commands.values());
        return commands;
    }

    public Response handle(Request request) {
        try {
            Command command = commands.get(request.getCommandName().toLowerCase());

            if (command == null) {
                logger.warn("Unknown command: {}", request.getCommandName());
                return Response.error("Unknown command. Use 'help' to list available commands");
            }

            if (!validateArguments(command, request)) {
                logger.warn("Invalid arguments for command: {}", command.getName());
                return Response.error("Invalid arguments for command: " + command.getName());
            }

            logger.info("Executing command: {}", command.getName());
            return command.execute(request);

        } catch (Exception e) {
            logger.error("Command execution error: {}", e.getMessage(), e);
            return Response.error("Internal server error: " + e.getMessage());
        }
    }

    private boolean validateArguments(Command command, Request request) {
        int expectedArgs = command.getCommandType().getArgumentCount();
        int actualArgs = request.getCommandArgs().length;

        return expectedArgs == -1 || actualArgs == expectedArgs;
    }

    public  Command getCommand(String commandName) {
        return commands.get(commandName);
    }

    public Response executeCommand(String commandName, Request request) {
        try {
            Command command = commands.get(commandName.toLowerCase());

            if (command == null) {
                return Response.warning("Неизвестная команда. Введите 'help' для списка доступных команд.");
            }

            return command.execute(request);
        } catch (Exception e) {
            return Response.error("Ошибка при выполнении команды: " + e.getMessage());
        }
    }

    public boolean hasCommand(String commandName) {
        return commands.containsKey(commandName.toLowerCase());
    }

    public ArrayList<CommandInfo> getCommandInfos() {
        ArrayList<CommandInfo> commandInfos = new ArrayList<>();

        for (Command command : commands.values()) {
            commandInfos.add(new CommandInfo(command));
        }

        return commandInfos;
    }
}
