package handler;

import adapter.LocaleDateTimeTypeAdapter;
import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.Managers;
import manager.TaskManager;
import manager.http.HttpTaskServer;
import models.Subtask;
import models.TaskStatus;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class SubtaskHandler implements HttpHandler {
    private TaskManager taskManager;
    private Gson gson;
    String response;
    Subtask subtask;

    public SubtaskHandler() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocaleDateTimeTypeAdapter());
        gson = Managers.getGson();
        taskManager = new HttpTaskServer().getTaskManager();
    }

    @Override
    public void handle(HttpExchange httpExchange) {
        String query = httpExchange.getRequestURI().getQuery();
        String requestMethod = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();
        try {
            switch (requestMethod) {
                case "GET":
                    if (path.contains("epic")) {
                        int id = Integer.parseInt(query.split("=")[1]);
                        ArrayList<Subtask> subtasksEpic = taskManager.getAllEpicSubtasks(id);
                        response = gson.toJson(subtasksEpic);
                    } else if (query == null) {
                        ArrayList<Subtask> subtasks = taskManager.getAllSubtasks();
                        response = gson.toJson(subtasks);
                    } else {
                        int id = Integer.parseInt(query.split("=")[1]);
                        subtask = taskManager.getSubtaskById(id);
                        response = gson.toJson(subtask);
                    }
                    break;
                case "DELETE":
                    if (query != null) {
                        int id = Integer.parseInt(query.split("=")[1]);
                        taskManager.deleteSubtaskById(id);
                        response = "Подзадача успешно удалена";
                    } else {
                        taskManager.deleteAllSubtasks();
                        response = "Подадачи успешно удалены";
                    }
                    break;
                case "POST":
                    String bodySubtask = null;
                    try {
                        bodySubtask = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    JsonElement jsonElement = JsonParser.parseString(bodySubtask);
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    if (!httpExchange.getRequestURI().getPath().contains("?id=")) {//создание
                        subtask = gson.fromJson(bodySubtask, Subtask.class);
                        taskManager.createNewSubtask(subtask);
                        response = "Подзадача добавлена";
                    } else {
                        int id = jsonObject.get("id").getAsInt();
                        subtask = taskManager.getSubtaskById(id);
                        String taskStatus = jsonObject.get("status").getAsString();
                        TaskStatus taskStatusType;
                        if (taskStatus.equals("NEW")) {
                            taskStatusType = TaskStatus.NEW;
                        } else if (taskStatus.equals("IN_PROGRESS")) {
                            taskStatusType = TaskStatus.IN_PROGRESS;
                        } else {
                            taskStatusType = TaskStatus.DONE;
                        }
                        subtask.setStatus(taskStatusType);
                        taskManager.updateTask(subtask);

                        response = "Обновление подзадачи";
                    }
                    break;
                default:
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
