package ui;

public class CommandInfo extends CommandBase {
    public CommandInfo(Command command) {
        this.name = command.getName();
        this.info = command.getInfo();
        this.argumentCount = command.getArgumentCount();
        this.commandType = command.getCommandType();
    }

    @Override
    public String toString() {
        return this.name + " — " + this.info;
    }
}