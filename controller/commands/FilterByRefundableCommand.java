//package controller.commands;
//
//import controller.Command;
//import service.CollectionManager;
//
//import java.util.stream.Collectors;
//
//public class FilterByRefundableCommand implements Command {
//    private final CollectionManager collectionManager;
//
//    public FilterByRefundableCommand(CollectionManager collectionManager) {
//        this.collectionManager = collectionManager;
//    }
//
//    @Override
//    public void execute(String[] args) {
//        if (args.length != 1) {
//            System.out.println("Использование команды: filter_by_refundable <true|false>");
//            return;
//        }
//
//        try {
//            Boolean refundableValue = Boolean.parseBoolean(args[0]);
//
//            var filteredTickets = collectionManager.getCollection().stream().filter(ticket -> refundableValue.equals(ticket.getRefundable())).collect(Collectors.toList());
//
//            if (filteredTickets.isEmpty()) {
//                System.out.println("Нет элементов, у которых поле refundable равно " + refundableValue);
//            } else {
//                System.out.println("Элементы, у которых поле refundable равно " + refundableValue + ":");
//                filteredTickets.forEach(System.out::println);
//            }
//        } catch (Exception e) {
//            System.out.println("Ошибка: значение refundable должно быть true или false.");
//        }
//    }
//
//    @Override
//    public String describe() {
//        return "filter_by_refundable - выводит элементы, значение поля refundable которых равно заданному";
//    }
//}
