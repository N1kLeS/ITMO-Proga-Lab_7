//package controller.commands;
//
//import controller.Command;
//import controller.ElementInputHandler;
//import models.*;
//import service.CollectionManager;
//
//
//public class UpdateCommand implements Command {
//    private final CollectionManager collectionManager;
//    private final ElementInputHandler inputHandler;
//
//    public UpdateCommand(CollectionManager collectionManager, ElementInputHandler inputHandler) {
//        this.collectionManager = collectionManager;
//        this.inputHandler = inputHandler;
//    }
//
//    @Override
//    public void execute(String[] args) {
//        if (args.length != 1) {
//            System.out.println("Ошибка: необходимо указать id элемента для обновления.");
//            return;
//        }
//
//        try {
//            Long id = Long.parseLong(args[0]);
//            System.out.println("Создание нового объекта для обновления...");
//            Ticket updatedTicket = inputHandler.createTicket();
//
//            if (collectionManager.updateElementById(id, updatedTicket)) {
//                System.out.println("Элемент с id " + id + " успешно обновлён.");
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
//        return "update - обновление билета по id";
//    }
//
//
//}

