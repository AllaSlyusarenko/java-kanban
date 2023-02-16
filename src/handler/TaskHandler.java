package handler;


import adapter.LocaleDateTimeTypeAdapter;
import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.Managers;
import manager.TaskManager;
import manager.http.HttpTaskManager;
import manager.http.HttpTaskServer;
import models.Task;
import models.TaskStatus;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class TaskHandler implements HttpHandler { // 4 класса хендлеров

    private TaskManager taskManager;
    private Gson gson;
    String response;
    Task task;

    public TaskHandler() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocaleDateTimeTypeAdapter());
        gson = Managers.getGson();
        taskManager = new HttpTaskServer().getTaskManager();
    }

    @Override
    public void handle(HttpExchange httpExchange){
        String query = httpExchange.getRequestURI().getQuery();
        String requestMethod = httpExchange.getRequestMethod();
        try {
            if ("GET".equals(requestMethod)) {
                if (query != null) {
                    int id = Integer.parseInt(query.split("=")[1]);
                    task = taskManager.getTaskById(id);
                    response = gson.toJson(task);
                } else {
                    ArrayList<Task> tasks = taskManager.getAllTasks();
                    response = gson.toJson(tasks);
                }
            }

            if ("DELETE".equals(requestMethod)) {
                if (query != null) {
                    int id = Integer.parseInt(query.split("=")[1]);
                    taskManager.deleteTaskById(id);
                    response = "Задача успешно удалена";
                } else {
                    taskManager.deleteAllTasks();
                    response = "Задачи успешно удалены";
                }

            }
            if ("POST".equals(requestMethod)) { // если есть id , то это обновление, если нет id, то добавление
                String bodyTask = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                JsonElement jsonElement = JsonParser.parseString(bodyTask);
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                if (!httpExchange.getRequestURI().getPath().contains("?id=")) {
                    task = gson.fromJson(bodyTask, Task.class);
                    taskManager.createNewTask(task); // много раз сейв  - значение ключа для всех обновлено
                    response = "Задача добавлена";
                } else {
                    int id = jsonObject.get("id").getAsInt();
                    task = taskManager.getTaskById(id);
                    String taskStatus = jsonObject.get("status").getAsString();
                    TaskStatus taskStatusType;
                    if (taskStatus.equals("NEW")) {
                        taskStatusType = TaskStatus.NEW;
                    } else if (taskStatus.equals("IN_PROGRESS")) {
                        taskStatusType = TaskStatus.IN_PROGRESS;
                    } else {
                        taskStatusType = TaskStatus.DONE;
                    }
                    task.setStatus(taskStatusType);
                    taskManager.updateTask(task);

                    response = "Обновление задачи";
                }

            }
            if (!"GET".equals(requestMethod) && !"DELETE".equals(requestMethod) && !"POST".equals(requestMethod)) {
                response = "Проверьте правильность вводимых данных";
                httpExchange.sendResponseHeaders(405, 0);
            }

            httpExchange.sendResponseHeaders(200, 0);

            OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.write(response.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.println("Возникла проблема");
            e.printStackTrace();
        }

        httpExchange.close();
    }
}