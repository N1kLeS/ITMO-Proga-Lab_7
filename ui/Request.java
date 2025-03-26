package ui;

public class Request {
    private final String[] commandArgs;

    public Request(String[] commandArgs) {
        this.commandArgs = commandArgs;
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