package handler;


import adapter.LocaleDateTimeTypeAdapter;
import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import models.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class TaskHandler implements HttpHandler { // 4 класса хендлеров

    private TaskManager taskManager; // связь между пользователем ктр запрашивает действия к методам таскменеджера,
    // нужен кто-то кто будет имплементировать TaskManager
    private Gson gson;
    String response;
    Task task;// чтобы перевести в формат json

    public TaskHandler(TaskManager taskManager) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocaleDateTimeTypeAdapter()); // создать этот класс адаптер
        // в теории это было registerTypeAdapter,  из теории write()
        gson = gsonBuilder.create();
        this.taskManager = taskManager;
    }

    // все методы TaskManager - а должны быть в каких-то хендлерах вызваны, должен быть какой-то ендпонит,
// как пользователь смог бы достучаться до этих методов
    //taskManager должен вызываться через ендпоинты с помощью хендлеров
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();
        if ("GET".equals(requestMethod)) {
            if (path.contains("?id=")) {
                int id = Integer.parseInt(path.split("=")[1]);
                task = taskManager.getTaskById(id);
                response = gson.toJson(task);
            } else {
                ArrayList<Task> tasks = taskManager.getAllTasks();
                response = gson.toJson(tasks);
            }
        }
        if ("DELETE".equals(requestMethod)) {
            if (path.contains("?id=")) {
                int id = Integer.parseInt(path.split("=")[1]);
                taskManager.deleteTaskById(id);
                response = "Задача успешно удалена";
            } else {
                taskManager.deleteAllTasks();
                response = "Задачи успешно удалены";
            }

        }
        if ("POST".equals(requestMethod)) { // если есть id , то это обновление, если нет id, то добавление
            String bodyTask = httpExchange.getRequestBody().toString();
            JsonElement jsonElement = JsonParser.parseString(bodyTask);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            //int id = jsonObject.get("id").getAsInt();

            if (jsonObject.get("id") == null) {
                //создание
                task = gson.fromJson(bodyTask,Task.class);
                taskManager.createNewTask(task);
                response = "Добавление задачи";


            } else {
                // обновление
                response = "";
            }

        }


        httpExchange.sendResponseHeaders(200, 0);
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(gson.toJson(new Object()).getBytes(StandardCharsets.UTF_8)); // потом преобразовываем в строчку JSON
        // и эту строчку подставим вместо new Object() - список задач или еще что-то, что нужно преобразовать в строчку с помощью gson
        // и пользователь увидит этот ответ
        // эта часть будет в каждом хендлере , написание ответа - что пользователь увидит в ответ на свой запрос


        if (httpExchange.getRequestURI().toString().contains("?id=")) { // хотели вызвать  одну конкретную по id
            // taskManager.getById(); // не хватает response
        } else {
            // taskManager.getAll();  // получили все задачи , потом преобразовываем в строчку JSON
            httpExchange.sendResponseHeaders(200, 0);
            //OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.write(gson.toJson(new Object()).getBytes(StandardCharsets.UTF_8)); // потом преобразовываем в строчку JSON
            // и эту строчку подставим вместо new Object() - список задач или еще что-то, что нужно преобразовать в строчку с помощью gson
            // и пользователь увидит этот ответ
            // эта часть будет в каждом хендлере , написание ответа - что пользователь увидит в ответ на свой запрос

            if (!"GET".equals(requestMethod)) { // если ни один из методов не вызван
                httpExchange.sendResponseHeaders(405, 0);
                throw new RuntimeException(); //  как-то сообщить что вызвал не тот метод write( осмысленный response) или вернуть код - 100
            }
            httpExchange.close();
        }

    }
}