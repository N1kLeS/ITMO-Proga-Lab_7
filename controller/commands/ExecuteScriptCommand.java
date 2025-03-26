//package controller.commands;
//
//import controller.Command;
//import controller.CommandHandler;
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//
//public class ExecuteScriptCommand implements Command {
//    private final CommandHandler commandHandler;
//
//    public ExecuteScriptCommand(CommandHandler commandHandler) {
//        this.commandHandler = commandHandler;
//    }
//    boolean isExecuting = false;
//
//    @Override
//    public void execute(String[] args) {
//        if (args.length != 1) {
//            System.out.println("Ошибка: Укажите имя файла.");
//            return;
//        }
//
//        String fileName = args[0];
//
//        if (isExecuting) {
//            System.out.println("Ошибка: команда уже выполняется.");
//            return;
//        }
//
//        try {
//            isExecuting = true;
//            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    line = line.trim();
//                    if (!line.isEmpty()) {
//                        System.out.println("Выполняется команда: " + line);
//                        commandHandler.execute(line);
//                    }
//                }
//            } catch (IOException e) {
//                System.out.println("Ошибка при чтении файла: " + e.getMessage());
//            }
//        } finally {
//            isExecuting = false;
//        }
//    }
//
//    @Override
//    public String describe() {
//        return "execute_script - загрузка скрипта с указанного файла";
//    }
//}
