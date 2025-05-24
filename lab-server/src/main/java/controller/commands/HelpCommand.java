package controller.commands;

import controller.*;
import ui.*;

import java.util.ArrayList;

public class HelpCommand extends Command {
    CommandHandler commandHandler;
    public HelpCommand(CommandHandler commandHandler) {
        super("help", "выводит информацию о всех доступных командах", 0, CommandType.WITHOUT_ARGUMENTS);
        this.commandHandler = commandHandler;
    }

    @Override
    public Response execute(Request request) {

        ArrayList<CommandInfo> commandsInfo = commandHandler.getCommandInfos();

        return Response.success("Доступные команды:", commandsInfo);
    }
}
