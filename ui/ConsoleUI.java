package ui;

import controller.CommandHandler;
import controller.commands.Command;

import java.util.Scanner;

public class ConsoleUI {
    private final CommandHandler commandHandler;

    public ConsoleUI(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    public void startInteractiveMode() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Добро пожаловать! Введите 'help' для списка команд.");

        try {
            while (true) {
                System.out.print("> ");
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) continue;

                String[] parts = input.split(" ", 2);
                String commandName = parts[0];
                String[] args = parts.length > 1 ? parts[1].split(" ") : new String[0];

                try {
                    Command command = commandHandler.getCommand(commandName);

                    if (command != null) {
                        Request request = new Request(args);
                        Response response = commandHandler.execute(command, request);

                        if (response.isSuccess()) {
                            System.out.println(response.getMessage());
                        } else {
                            System.out.println("Ошибка: " + response.getMessage());
                        }
                    } else {
                        System.out.println("Ошибка: Неизвестная команда. Введите 'help' для списка команд.");

                    }
                } catch (Exception e) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("Работа завершена!");
        }
    }
}
