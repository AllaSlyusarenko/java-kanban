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
    List<Task> history;
    String response;

    public HistoryHandler() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocaleDateTimeTypeAdapter());
        gson = Managers.getGson();
        taskManager = new HttpTaskServer().getTaskManager();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        String requestMethod = httpExchange.getRequestMethod();

        if ("GET".equals(requestMethod)) {
            history = taskManager.getHistory();
            response = gson.toJson(history);
        } else {
            response = "Проверьте правильность вводимых данных";
            httpExchange.sendResponseHeaders(405, 0);
        }

        OutputStream outputStream = httpExchange.getResponseBody();
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        httpExchange.sendResponseHeaders(200, bytes.length);
        outputStream.write(bytes);
    }
}
