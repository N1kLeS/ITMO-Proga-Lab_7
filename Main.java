import controller.CommandHandler;
import controller.ElementInputHandler;
import controller.commands.*;
import models.Ticket;
import service.*;
import ui.*;


public class Main {
    public static void main(String[] args) {
        CollectionManager collectionManager = new CollectionManager();

        CommandHandler commandHandler = new CommandHandler();
        ElementInputHandler inputHandler = new ElementInputHandler();
//        String fileName = "collection.csv";
//        collectionManager.loadCollection(fileName);

        commandHandler.register(new HelpCommand());
        commandHandler.register(new ExitCommand());
        commandHandler.register(new AddCommand(collectionManager, inputHandler));
//        commandHandler.register("info", new InfoCommand(collectionManager));
        commandHandler.register(new ShowCommand(collectionManager));
//        commandHandler.register("update", new UpdateCommand(collectionManager, inputHandler));
//        commandHandler.register("clear", new ClearCommand(collectionManager));
//        commandHandler.register("remove_by_id", new RemoveByIdCommand(collectionManager));
//        commandHandler.register("save", new SaveCommand(collectionManager));
//        commandHandler.register("execute_script", new ExecuteScriptCommand(commandHandler));
//        commandHandler.register("add_if_min", new AddIfMinCommand(collectionManager, inputHandler));
//        commandHandler.register("remove_greater", new RemoveGreaterCommand(collectionManager, inputHandler));
//        commandHandler.register("remove_lower", new RemoveLowerCommand(collectionManager, inputHandler));
//        commandHandler.register("count_greater_than_type", new CountGreaterThanTypeCommand(collectionManager));
//        commandHandler.register("filter_by_refundable", new FilterByRefundableCommand(collectionManager));
//        commandHandler.register("filter_greater_than_person", new FilterGreaterThanPersonCommand(collectionManager));

        ConsoleUI consoleUI = new ConsoleUI(commandHandler);
        consoleUI.startInteractiveMode();
    }
}
