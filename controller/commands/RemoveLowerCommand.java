//package controller.commands;
//
//
//import controller.Command;
//import controller.ElementInputHandler;
//import models.Ticket;
//import service.CollectionManager;
//
//
//public class RemoveLowerCommand implements Command {
//    private final CollectionManager collectionManager;
//    private final ElementInputHandler inputHandler;
//
//    public RemoveLowerCommand(CollectionManager collectionManager, ElementInputHandler inputHandler) {
//        this.collectionManager = collectionManager;
//        this.inputHandler = inputHandler;
//    }
//
//    @Override
//    public void execute(String[] args) {
//        if (args.length > 0) {
//            System.out.println("Команда remove_lower не принимает аргументы. Для выполнения команды следуйте подсказкам.");
//            return;
//        }
//
//        System.out.println("Введите данные для элемента:");
//        Ticket ticket = inputHandler.readElement();
//
//        int initialSize = collectionManager.getCollection().size();
//        collectionManager.getCollection().removeIf(existingTicket -> existingTicket.compareTo(ticket) < 0);
//        int removedCount = initialSize - collectionManager.getCollection().size();
//
//        System.out.println("Удалено элементов: " + removedCount);
//    }
//
//    @Override
//    public String describe() {
//        return "remove_lower - удаляет из коллекции все элементы, меньшие, чем заданный";
//    }
//}
