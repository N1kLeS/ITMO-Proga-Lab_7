package controller.commands;

import controller.*;
import service.CollectionManager;
import ui.Request;
import ui.Response;




public class HelpCommand extends Command {
    public HelpCommand() {
        super("help", "выводит информацию о всех доступных командах", 0, CommandType.WITHOUT_ARGUMENTS);
    }



    @Override
    public Response execute(Request request, CollectionManager collectionManager) {
        if (request.getCommandArgs().length != getArgumentCount()) {
            return Response.failure("Команда 'help' не принимает аргументов. Попробуйте просто ввести 'help'.");
        }

        StringBuilder helpMessage = new StringBuilder();
        helpMessage.append("Доступные команды:\n");

        String commandsInfo = CommandHandler.getCommands();
        if (commandsInfo.isEmpty()) {
            return Response.failure("Нет доступных команд.");
        }

        helpMessage.append(commandsInfo);
        return new Response(true, helpMessage.toString(), null);
    }
}
