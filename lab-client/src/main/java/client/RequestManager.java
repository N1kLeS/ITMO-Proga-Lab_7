package client;

import ClientCommands.ExecuteScriptCommand;
import ClientCommands.ExitClientCommand;
import authentication.User;
import lombok.Getter;
import ui.*;
import utils.ElementInputHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class RequestManager {
    private final CommandHandler clientCommandHandler;
    private final HashMap<String, CommandInfo> serverCommandsInfoList;
    @Getter
    private final ConnectionUDP connectionUDP;

    public RequestManager(ConnectionUDP connectionUDP) {
        this.connectionUDP = connectionUDP;

        this.clientCommandHandler = new CommandHandler();
        registerClientCommands();

        this.serverCommandsInfoList = new HashMap<>();
        loadServerCommandsList();
    }

    private void registerClientCommands() {
        clientCommandHandler.register(new ExitClientCommand());
        clientCommandHandler.register(new ExecuteScriptCommand(this));
    }

    public Request generateRequest(String input,
                                   Scanner scanner) {

        String[] parts = input.split(" ", 2);
        String commandName = parts[0];
        String[] args = parts.length > 1 ? parts[1].split(" ") : new String[0];

        if (!serverCommandsInfoList.containsKey(commandName)) {
            System.out.println("Неизвестная команда: " + commandName);
        }

        CommandInfo command = serverCommandsInfoList.get(commandName);
        CommandType commandType = command.getCommandType();
        if (commandType.getArgumentCount() != args.length) {
            System.out.println("Неверное количество аргументов для команды: " + commandName);
        }

        if (!command.isServerCommand()) {
            Command clientCommand = clientCommandHandler.getCommand(commandName);
            if (clientCommand != null) {
                Response response = clientCommand.execute(new Request(commandName, args));
                System.out.println(response);
            } else {
                System.out.println("Команда не найдена: " + commandName);
            }
        }

        Object object = null;

        if (commandType.hasForm()) {
            ElementInputHandler inputHandler = new ElementInputHandler(scanner);
            object = inputHandler.readValue(commandType.getFormClass());
        }

        return new Request(commandName, args, object);
    }

    private void loadServerCommandsList() {
        Response response = connectionUDP.sendRequestWithRetry(new Request("help", new String[0]));

        for (CommandInfo commandInfo : (ArrayList<CommandInfo>) response.getData())
            this.serverCommandsInfoList.put(commandInfo.getName(), commandInfo);

    }
}
