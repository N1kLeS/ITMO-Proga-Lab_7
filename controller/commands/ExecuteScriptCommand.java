package controller.commands;

import controller.CommandHandler;
import controller.CommandType;
import ui.Request;
import ui.Response;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ExecuteScriptCommand extends Command {
    private final CommandHandler commandHandler;
    private final Map<String, Integer> scriptExecutionCount = new HashMap<>();

    public ExecuteScriptCommand(CommandHandler commandHandler) {
        super("execute_script", "загрузка скрипта с указанного файла", 1, CommandType.WITH_ARGUMENTS);
        this.commandHandler = commandHandler;
    }

    @Override
    public Response execute(Request request) {
        String fileName = request.getArgument(0);

        scriptExecutionCount.put(fileName, scriptExecutionCount.getOrDefault(fileName, 0) + 1);


        if (scriptExecutionCount.get(fileName) > 5) {
            scriptExecutionCount.put(fileName, scriptExecutionCount.get(fileName) - 1); // Откатываем счетчик
            return new Response(false, "Обнаружена рекурсия! Скрипт " + fileName + " вызван более 5 раз.", null);
        } else {
                try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        line = line.trim();
                        if (!line.isEmpty()) {
                            System.out.println("Выполняется команда: " + line);

                            String[] command_with_arguments = line.split(" ");

                            String[] arguments = Arrays.copyOfRange(command_with_arguments, 1, command_with_arguments.length);
                            Command command = commandHandler.getCommand(command_with_arguments[0]);

                            Response r = commandHandler.execute(command, new Request(arguments)); // TODO fix me
                            System.out.println(r);
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Ошибка при чтении файла: " + e.getMessage());
                }finally {
                    scriptExecutionCount.put(fileName, scriptExecutionCount.get(fileName) - 1);
                    if (scriptExecutionCount.get(fileName) <= 0) {
                        scriptExecutionCount.remove(fileName);
                    }
                }
        }
        return new Response(true, "Турист выполнен!", null);
    }
}