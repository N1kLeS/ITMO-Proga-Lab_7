//package controller.commands;
//
//import controller.Command;
//import service.CollectionManager;
//
//public class SaveCommand implements Command {
//    private final CollectionManager collectionManager;
//
//    public SaveCommand(CollectionManager collectionManager) {
//        this.collectionManager = collectionManager;
//    }
//
//    @Override
//    public void execute(String[] args) {
//        if (args.length != 0) {
//            System.out.println("Ошибка: эта команда не принимает аргументы.");
//            return;
//        }
//        if (collectionManager.saveCollection()) {
//            System.out.println("Коллекция успешно сохранена.");
//        } else {
//            System.out.println("Ошибка при сохранении коллекции.");
//        }
//    }
//
//    @Override
//    public String describe() {
//        return "save - сохранение в файл формата csv";
//    }
//}
