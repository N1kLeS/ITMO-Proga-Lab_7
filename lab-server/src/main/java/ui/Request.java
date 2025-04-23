package ui;

import java.io.Serializable;

public class Request implements Serializable {
    private final String commandName;
    private final String[] commandArgs;

    public Request(String commandName, String[] commandArgs) {
        this.commandName = commandName;
        this.commandArgs = commandArgs;
    }

    public String getCommandName() {
        return commandName;
    }

    public String[] getCommandArgs() {
        return commandArgs;
    }

    public String getArgument(int index) {
        if (index >= 0 && index < commandArgs.length) {
            return commandArgs[index];
        }
        return null;
    }
}