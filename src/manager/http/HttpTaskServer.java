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

    public HttpTaskServer(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        try {
            httpServer = httpServer.create(new InetSocketAddress("localhost", PORT), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        httpServer.createContext("/tasks/task", new TaskHandler(Managers.getDefault()));
        httpServer.createContext("/tasks/subtask", new SubtaskHandler(Managers.getDefault()));
        httpServer.createContext("/tasks/epic", new EpicHandler(Managers.getDefault()));
        httpServer.createContext("/tasks/history", new HistoryHandler(Managers.getDefault()));
        httpServer.createContext("/tasks/", new TasksHandler(Managers.getDefault()));
        //Managers.getDefault()  - new FileBackedTasksManager() - это из тз

        httpServer.start();
    }
}


