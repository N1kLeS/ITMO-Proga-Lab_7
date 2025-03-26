//package controller.commands;
//
//import controller.Command;
//import service.CollectionManager;
//
//public class ClearCommand implements Command {
//    private final CollectionManager collectionManager;
//
//    public ClearCommand(CollectionManager collectionManager) {
//        this.collectionManager = collectionManager;
//    }
//
//    @Override
//    public void execute(String[] args) {
//        if (args.length != 0) {
//            System.out.println("Ошибка: эта команда не принимает аргументы.");
//            return;
//        }
//        collectionManager.clearCollection();
//        System.out.println("Коллекция успешно очищена.");
//    }
//
//    @Override
//    public String describe() {
//        return "clear - отчистка коллекции";
//    }
//}
