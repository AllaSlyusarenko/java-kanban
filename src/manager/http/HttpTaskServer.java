package manager.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import handler.*;
import manager.Managers;
import manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private HttpServer httpServer;
    private Gson gson;
    private TaskManager taskManager;

    public HttpTaskServer() {
        taskManager = Managers.getDefault();
        gson = Managers.getGson();
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }

    public void start() {
        System.out.println("Запускаем HttpTaskServer сервер на порту " + PORT);
        try {
            httpServer = httpServer.create(new InetSocketAddress("localhost", PORT), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        httpServer.createContext("/tasks/task/", new TaskHandler());
        httpServer.createContext("/tasks/subtask/", new SubtaskHandler());
        httpServer.createContext("/tasks/epic/", new EpicHandler());
        httpServer.createContext("/tasks/history/", new HistoryHandler());
        httpServer.createContext("/tasks/", new TasksHandler());
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
    }
}


