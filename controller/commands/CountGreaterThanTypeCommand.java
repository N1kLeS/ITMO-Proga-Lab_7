//package controller.commands;
//
//import controller.Command;
//import models.TicketType;
//import service.CollectionManager;
//
//public class CountGreaterThanTypeCommand implements Command {
//    private final CollectionManager collectionManager;
//
//    public CountGreaterThanTypeCommand(CollectionManager collectionManager) {
//        this.collectionManager = collectionManager;
//    }
//
//    @Override
//    public void execute(String[] args) {
//        if (args.length != 1) {
//            System.out.println("Использование команды: count_greater_than_type <type>");
//            System.out.println("Доступные значения: USUAL, BUDGETARY, CHEAP.");
//            return;
//        }
//
//        try {
//            TicketType type = TicketType.valueOf(args[0].toUpperCase());
//
//            long count = collectionManager.getCollection().stream().filter(ticket -> ticket.getType() != null && ticket.getType().compareTo(type) > 0).count();
//
//            System.out.println("Количество элементов, значение поля type которых больше " + type + ": " + count);
//        } catch (IllegalArgumentException e) {
//            System.out.println("Неверное значение типа. Доступные значения: USUAL, BUDGETARY, CHEAP.");
//        }
//    }
//
//    @Override
//    public String describe() {
//        return "count_greater_than_type - выводит количество элементов, значение поля type которых больше заданного";
//    }
//}
