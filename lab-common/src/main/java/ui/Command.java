package ui;



public abstract class Command extends CommandBase {
    public Command(String name, String info, boolean isServerCommand, CommandType commandType, boolean requiredAuth) {
        this.name = name;
        this.info = info;
        this.isServerCommand = isServerCommand;
        this.commandType = commandType;
        this.requiredAuth = requiredAuth;
    }

    public abstract Response execute(Request request);
}
