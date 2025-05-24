package ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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


    public ArrayList<Command> getCommands() {
        return new ArrayList<>(this.commands.values());
    }

    public Command getCommand(String name) {
        return commands.get(name);
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

        return actualArgs == expectedArgs;
    }

    public ArrayList<CommandInfo> getCommandInfos() {
        ArrayList<CommandInfo> commandInfos = new ArrayList<>();

        for (Command command : commands.values()) {
            commandInfos.add(new CommandInfo(command));
        }

        return commandInfos;
    }
}
