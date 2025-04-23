package controller.commands;

import controller.CommandType;
import ui.Request;
import ui.Response;


public abstract class Command {
    private String name;
    private final String info;
    private final int argumentCount;
    private final CommandType commandType;

    public Command(String name, String info, int argumentCount, CommandType commandType) {
        this.name = name;
        this.info = info;
        this.argumentCount = argumentCount;
        this.commandType = commandType;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    public int getArgumentCount() {
        return argumentCount;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public boolean isNeedForm() {
        return commandType.isNeedForm();
    }

    public abstract Response execute(Request request);
}
