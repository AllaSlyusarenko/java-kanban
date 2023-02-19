package handler;

import adapter.LocaleDateTimeTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.Managers;
import manager.TaskManager;
import manager.http.HttpTaskServer;
import models.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HistoryHandler implements HttpHandler {
    private TaskManager taskManager;
    private Gson gson;
    String response;

    public HistoryHandler() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocaleDateTimeTypeAdapter());
        gson = Managers.getGson();
        taskManager = new HttpTaskServer().getTaskManager();
    }

    @Override
    public void handle(HttpExchange httpExchange) {

        String requestMethod = httpExchange.getRequestMethod();
        try {
            switch (requestMethod) {
                case "GET":
                    List<Integer> listId = new ArrayList<>();
                    List<Task> history = taskManager.getHistory();
                    for (Task task : history) {
                        listId.add(task.getId());
                    }
                    response = gson.toJson(listId);
                    break;
                default:
                    response = "Проверьте правильность вводимых данных";
                    httpExchange.sendResponseHeaders(405, 0);
            }
            OutputStream outputStream = httpExchange.getResponseBody();
            byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
            httpExchange.sendResponseHeaders(200, bytes.length);
            outputStream.write(bytes);
        } catch (IOException e) {
            System.out.println("Возникла проблема");
            e.printStackTrace();
        }
        httpExchange.close();
    }
}
