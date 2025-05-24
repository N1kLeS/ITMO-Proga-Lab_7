package controller.commands;

import service.CollectionManager;
import ui.Command;
import ui.CommandType;
import ui.Request;
import ui.Response;

import java.util.stream.Collectors;

public class FilterByRefundableCommand extends Command {
    private final CollectionManager collectionManager;

    public FilterByRefundableCommand(CollectionManager collectionManager) {
        super("filter_by_refundable",
              "выводит элементы, значение поля refundable (true|false) которых равно " + "заданному",
              true,
              CommandType.WITH_ARGUMENTS(1));
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
                    .collect(Collectors.toList());

            if (filteredTickets.isEmpty()) {
                return Response.warning("Нет элементов, у которых поле refundable равно " + refundableValue);
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Элементы, у которых поле refundable равно ")
                        .append(refundableValue)
                        .append(":\n");

                filteredTickets.forEach(ticket -> stringBuilder.append(ticket).append("\n"));
                return Response.success(stringBuilder.toString(), null);
            }
        } catch (Exception e) {
            return Response.error("Ошибка: значение refundable должно быть true или false.", null);
        }
    }
}
