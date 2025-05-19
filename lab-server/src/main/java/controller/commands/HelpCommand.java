package controller.commands;

import controller.*;
import ui.Command;
import ui.CommandType;
import ui.Request;
import ui.Response;

public class HelpCommand extends Command {
    CommandHandler commandHandler;
    public HelpCommand(CommandHandler commandHandler) {
        super("help", "выводит информацию о всех доступных командах", 0, CommandType.WITHOUT_ARGUMENTS);
        this.commandHandler = commandHandler;
    }

    @Override
    public Response execute(Request request) {
        StringBuilder helpMessage = new StringBuilder();
        helpMessage.append("Доступные команды:\n");

        String commandsInfo = commandHandler.getCommands();
        if (commandsInfo.isEmpty()) {
            return Response.warning("Нет доступных команд.");
        }

        helpMessage.append(commandsInfo);
        return Response.success(helpMessage.toString(), null);
    }
}
