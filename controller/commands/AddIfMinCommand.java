//package controller.commands;
//
//import controller.Command;
//import controller.ElementInputHandler;
//import models.Ticket;
//import service.CollectionManager;
//
//import java.util.Optional;
//
//public class AddIfMinCommand implements Command {
//    private final CollectionManager collectionManager;
//    private final ElementInputHandler inputHandler;
//
//    public AddIfMinCommand(CollectionManager collectionManager, ElementInputHandler inputHandler) {
//        this.collectionManager = collectionManager;
//        this.inputHandler = inputHandler;
//    }
//
//    @Override
//    public void execute(String[] args) {
//        Ticket newTicket = inputHandler.createTicket();
//
//        Optional<Ticket> minTicket = collectionManager.getCollection().stream().min(Ticket::compareTo);
//
//        if (minTicket.isEmpty() || newTicket.compareTo(minTicket.get()) < 0) {
//            collectionManager.add(newTicket);
//            System.out.println("Элемент успешно добавлен в коллекцию.");
//        } else {
//            System.out.println("Элемент не был добавлен, так как он больше или равен минимальному элементу.");
//        }
//    }
//
//    @Override
//    public String describe() {
//        return "add_if_min - добавляет новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции";
//    }
//}
