package commands.data;

import models.Person;
import service.CollectionManager;
import ui.Command;
import ui.CommandType;
import ui.Request;
import ui.Response;

public class FilterGreaterThanPersonCommand extends Command {
    private final CollectionManager collectionManager;

    public FilterGreaterThanPersonCommand(CollectionManager collectionManager) {
        super("filter_greater_than_person",
              "выводит элементы, значение поля person которых больше заданного",
              true,
              CommandType.WITH_FORM(Person.class),
              true
        );
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        try {

            Person inputPerson = (Person) request.getData();

            var filteredTickets = collectionManager.getCollection()
                    .stream()
                    .filter(ticket -> ticket.getPerson() != null && ticket.getPerson().compareTo(inputPerson) > 0)
                    .toList();

            if (filteredTickets.isEmpty()) {
                return Response.warning("Нет элементов, у которых поле person больше указанного.");
            }

            return Response.success("Элементы, у которых поле person больше указанного:", filteredTickets);

        } catch (Exception e) {
            return Response.error("Ошибка при вводе объекта person: " + e.getMessage());
        }
    }
}
