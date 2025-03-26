import controller.CommandHandler;
import controller.ElementInputHandler;
import controller.commands.*;
import service.*;
import ui.*;


public class Main {
    public static void main(String[] args) {
        CollectionManager collectionManager = new CollectionManager();
        collectionManager.loadCollection("collection.csv");
        CommandHandler commandHandler = new CommandHandler();
        ElementInputHandler inputHandler = new ElementInputHandler();



        commandHandler.register(new ExitCommand());
        commandHandler.register(new AddCommand(collectionManager, inputHandler));
        commandHandler.register(new InfoCommand(collectionManager));
        commandHandler.register(new ShowCommand(collectionManager));
        commandHandler.register(new UpdateCommand(collectionManager, inputHandler));
        commandHandler.register(new ClearCommand(collectionManager));
//        commandHandler.register("remove_by_id", new RemoveByIdCommand(collectionManager));
        commandHandler.register(new SaveCommand(collectionManager));
        commandHandler.register(new ExecuteScriptCommand(commandHandler));
        commandHandler.register(new AddIfMinCommand(collectionManager, inputHandler));
//        commandHandler.register("remove_greater", new RemoveGreaterCommand(collectionManager, inputHandler));
//        commandHandler.register("remove_lower", new RemoveLowerCommand(collectionManager, inputHandler));
//        commandHandler.register("count_greater_than_type", new CountGreaterThanTypeCommand(collectionManager));
//        commandHandler.register("filter_by_refundable", new FilterByRefundableCommand(collectionManager));
//        commandHandler.register("filter_greater_than_person", new FilterGreaterThanPersonCommand(collectionManager));
        commandHandler.register(new HelpCommand(commandHandler));

        ConsoleUI consoleUI = new ConsoleUI(commandHandler);
        consoleUI.startInteractiveMode();
    }
}
