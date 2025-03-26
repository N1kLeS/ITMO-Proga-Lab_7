//package controller.commands;
//
//import controller.Command;
//import controller.ElementInputHandler;
//import models.person.Person;
//import service.CollectionManager;
//
//public class FilterGreaterThanPersonCommand implements Command {
//    private final CollectionManager collectionManager;
//
//    public FilterGreaterThanPersonCommand(CollectionManager collectionManager) {
//        this.collectionManager = collectionManager;
//    }
//
//    @Override
//    public void execute(String[] args) {
//        if (args.length != 0) {
//            System.out.println("Эта команда требует ввода сложного объекта person. Пожалуйста, вводите поля по одному.");
//            return;
//        }
//
//        try {
//            ElementInputHandler inputHandler = new ElementInputHandler();
//            Person inputPerson = inputHandler.readPerson();
//
//            var filteredTickets = collectionManager.getCollection().stream().filter(ticket -> ticket.getPerson() != null && ticket.getPerson().compareTo(inputPerson) > 0).toList();
//
//            if (filteredTickets.isEmpty()) {
//                System.out.println("Нет элементов, у которых поле person больше указанного.");
//            } else {
//                System.out.println("Элементы, у которых поле person больше указанного:");
//                filteredTickets.forEach(System.out::println);
//            }
//        } catch (Exception e) {
//            System.out.println("Ошибка при вводе объекта person: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public String describe() {
//        return "filter_greater_than_person - выводит элементы, значение поля person которых больше заданного";
//    }
//}
