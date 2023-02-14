package handler;

import adapter.LocaleDateTimeTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;

import java.io.IOException;
import java.time.LocalDateTime;

public class SubtaskHandler implements HttpHandler {
    private TaskManager taskManager; // связь между пользователем ктр запрашивает действия к методам таскменеджера,
    // нужен кто-то кто будет имплементировать TaskManager
    private Gson gson; // чтобы перевести в формат json

    public SubtaskHandler(TaskManager taskManager) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocaleDateTimeTypeAdapter()); // создать этот класс адаптер
        // в теории это было registerTypeAdapter,  из теории write()
        gson = gsonBuilder.create();
        this.taskManager = taskManager;
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
}
