package commands.data;

import service.CollectionManager;
import ui.Command;
import ui.CommandType;
import ui.Request;
import ui.Response;

public class FilterByRefundableCommand extends Command {
    private final CollectionManager collectionManager;

    public FilterByRefundableCommand(CollectionManager collectionManager) {
        super("filter_by_refundable",
              "выводит элементы, значение поля refundable (true|false) которых равно " + "заданному",
              true,
              CommandType.WITH_ARGUMENTS(1),
              true
        );
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        try {
            String argument = request.getArgument(0).toLowerCase();
            if (!argument.equals("true") && !argument.equals("false")) {
                return Response.warning("Ошибка: значение refundable должно быть строго 'true' или 'false'.");
            }

            Boolean refundableValue = Boolean.parseBoolean(request.getArgument(0));

            var filteredTickets = collectionManager.getCollection()
                    .stream()
                    .filter(ticket -> refundableValue.equals(ticket.getRefundable()))
                    .toList();

            if (filteredTickets.isEmpty()) {
                return Response.warning("Нет элементов, у которых поле refundable равно " + refundableValue);
            }
            
            return Response.success("Элементы, у которых поле refundable равно ", filteredTickets);
        } catch (Exception e) {
            return Response.error("Ошибка: значение refundable должно быть true или false.", null);
        }
    }
}
