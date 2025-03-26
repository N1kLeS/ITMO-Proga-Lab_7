//package controller.commands;
//
//
//import controller.Command;
//import service.CollectionManager;
//
//public class InfoCommand implements Command {
//    private final CollectionManager collectionManager;
//
//    public InfoCommand(CollectionManager collectionManager) {
//        this.collectionManager = collectionManager;
//    }
//
//    @Override
//    public void execute(String[] args) {
//        if (args.length != 0) {
//            System.out.println("Ошибка: эта команда не принимает аргументы.");
//            return;
//        }
//        System.out.println("Информация о коллекции:");
//        System.out.println(collectionManager.getCollectionInfo());
//    }
//
//    @Override
//    public String describe() {
//        return "info - вывод информации о коллекции";
//    }
//
//}
