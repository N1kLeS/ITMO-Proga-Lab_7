//package controller.commands;
//
//import controller.Command;
//import service.CollectionManager;
//
//
//public class RemoveByIdCommand implements Command {
//    private final CollectionManager collectionManager;
//
//    public RemoveByIdCommand(CollectionManager collectionManager) {
//        this.collectionManager = collectionManager;
//    }
//
//    @Override
//    public void execute(String[] args) {
//        if (args.length < 2) {
//            System.out.println("Ошибка: необходимо указать id элемента для удаления.");
//            return;
//        }
//
//        try {
//            Long id = Long.parseLong(args[1]);
//            if (collectionManager.removeById(id)) {
//                System.out.println("Элемент с id " + id + " успешно удалён.");
//            } else {
//                System.out.println("Элемент с id " + id + " не найден.");
//            }
//        } catch (NumberFormatException e) {
//            System.out.println("Ошибка: id должен быть числом.");
//        }
//    }
//
//    @Override
//    public String describe() {
//        return "remove_by_id - удаление тикета по id";
//    }
//
//}
