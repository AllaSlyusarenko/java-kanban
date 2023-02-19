package handler;

import adapter.LocaleDateTimeTypeAdapter;
import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.Managers;
import manager.TaskManager;
import manager.http.HttpTaskServer;
import models.Epic;
import models.TaskStatus;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class EpicHandler implements HttpHandler {

    private TaskManager taskManager;
    private Gson gson;
    String response;
    Epic epic;

    public EpicHandler() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocaleDateTimeTypeAdapter());
        gson = Managers.getGson();
        taskManager = new HttpTaskServer().getTaskManager();
    }

    @Override
    public void handle(HttpExchange httpExchange) {
        String requestMethod = httpExchange.getRequestMethod();
        String query = httpExchange.getRequestURI().getQuery();
        try {
            switch (requestMethod) {
                case "GET":
                    if (query != null) {
                        int id = Integer.parseInt(query.split("=")[1]);
                        epic = taskManager.getEpicById(id);
                        response = gson.toJson(epic);
                    } else {
                        ArrayList<Epic> epics = taskManager.getAllEpics();
                        response = gson.toJson(epics);
                    }
                    break;
                case "DELETE":
                    if (query != null) {
                        int id = Integer.parseInt(query.split("=")[1]);
                        taskManager.deleteEpicById(id);
                        response = gson.toJson("Эпик успешно удален");
                    } else {
                        taskManager.deleteAllEpics();
                        response = "Эпики успешно удалены";
                    }
                    break;
                case "POST":
                    String bodyEpic = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    JsonElement jsonElement = JsonParser.parseString(bodyEpic);
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    if (!httpExchange.getRequestURI().getPath().contains("?id=")) {//создание
                        epic = gson.fromJson(bodyEpic, Epic.class);
                        taskManager.createNewEpic(epic);
                        response = "Эпик добавлен";
                    } else {
                        int id = jsonObject.get("id").getAsInt();
                        epic = taskManager.getEpicById(id);
                        String taskStatus = jsonObject.get("status").getAsString();
                        TaskStatus taskStatusType;
                        if (taskStatus.equals("NEW")) {
                            taskStatusType = TaskStatus.NEW;
                        } else if (taskStatus.equals("IN_PROGRESS")) {
                            taskStatusType = TaskStatus.IN_PROGRESS;
                        } else {
                            taskStatusType = TaskStatus.DONE;
                        }
                        epic.setStatus(taskStatusType);
                        taskManager.updateEpic(epic);

                        response = "Обновление эпика";
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
