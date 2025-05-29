package commands;

import ui.*;

import java.util.ArrayList;

public class HelpCommand extends Command {
    final CommandHandler commandHandler;

    public HelpCommand(CommandHandler commandHandler) {
        super("help", "выводит информацию о всех доступных командах", true, CommandType.WITHOUT_DATA());
        this.commandHandler = commandHandler;
    }

    @Override
    public Response execute(Request request) {
        ArrayList<CommandInfo> commandsInfo = commandHandler.getCommandInfos();

        return Response.success("Доступные команды:", commandsInfo);
    }
}
