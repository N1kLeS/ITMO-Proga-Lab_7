package ui;



public abstract class Command extends CommandBase {
    public Command(String name, String info, boolean isServerCommand, CommandType commandType) {
        this.name = name;
        this.info = info;
        this.isServerCommand = isServerCommand;
        this.commandType = commandType;
    }

    public abstract Response execute(Request request);
}
