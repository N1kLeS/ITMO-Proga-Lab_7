package commands.data;

import authentication.User;
import ui.*;

import java.util.ArrayList;

public class HelpCommand extends Command {
    final CommandHandler commandHandler;

    public HelpCommand(CommandHandler commandHandler) {
        super("help", "выводит информацию о всех доступных командах", true, CommandType.WITHOUT_DATA(), false);
        this.commandHandler = commandHandler;
    }

    @Override
    public Response execute(Request request) {
        User user = request.getUser();

        ArrayList<CommandInfo> commandsInfo = commandHandler.getCommandInfos(user != null);

        return Response.success("Доступные команды:", commandsInfo);
    }
}
