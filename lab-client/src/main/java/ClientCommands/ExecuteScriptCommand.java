package ClientCommands;

import client.RequestManager;
import ui.Command;
import ui.CommandType;
import ui.Request;
import ui.Response;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ExecuteScriptCommand extends Command {
    private final Map<String, Integer> scriptExecutionCount = new HashMap<>();
    private final RequestManager requestManager;

    public ExecuteScriptCommand(RequestManager requestManager) {
        super("execute_script", "загрузка скрипта с указанного файла", false, CommandType.WITH_ARGUMENTS(1));
        this.requestManager = requestManager;
    }

    @Override
    public Response execute(Request request) {
        String fileName = request.getArgument(0);

        scriptExecutionCount.put(fileName, scriptExecutionCount.getOrDefault(fileName, 0) + 1);

        if (scriptExecutionCount.get(fileName) > 5) {
            scriptExecutionCount.put(fileName, scriptExecutionCount.get(fileName) - 1);
            return Response.warning("Обнаружена рекурсия! Скрипт " + fileName + " вызван более 5 раз.");
        } else {
            try (Scanner scanner = new Scanner(new File(fileName))) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    if (!line.isEmpty()) {

                        System.out.println("Выполняется команда: " + line);
                        Request innerRequest = requestManager.generateRequest(line, scanner);
                        Response innerResponse = requestManager.getConnectionUDP()
                                .sendRequestWithRetry(innerRequest);

                        System.out.println(innerResponse);
                    }
                }
            } catch (FileNotFoundException e) {
                return Response.error("Файл не найден: " + e.getMessage());
            } finally {
                scriptExecutionCount.put(fileName, scriptExecutionCount.get(fileName) - 1);
                if (scriptExecutionCount.get(fileName) <= 0) {
                    scriptExecutionCount.remove(fileName);
                }
            }
        }
        return Response.success("Скрипт успешно выполнен!");
    }
}