package server;

import manager.Managers;
import manager.TaskManager;
import manager.http.HttpTaskServer;
import models.Epic;
import models.Subtask;
import models.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

public class MainKV {

    public static void main(String[] args) throws IOException, InterruptedException {

        KVServer kvServer = new KVServer();
        kvServer.start();
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
        TaskManager taskManager = httpTaskServer.getTaskManager();
        Task task1 = new Task("Task 1", "Description 1 ", 5, LocalDateTime.of(2022, 3, 8, 15, 30));//id=1
        taskManager.createNewTask(task1);
        Task task2 = new Task("Task 2", "Description 2 ", 5, (LocalDateTime) null);//id=2
        taskManager.createNewTask(task2);
        Epic epic1 = new Epic("Epic 1", "Description 1 ");//id=3
        taskManager.createNewEpic(epic1);

        Subtask subtask1 = new Subtask("Subtask 1", "Description 1 ", 6, LocalDateTime.of(2022, 3, 7, 12, 38), 3);//id=4
        taskManager.createNewSubtask(subtask1);

        Subtask subtask2 = new Subtask("Subtask 2", "Description 2 ", 10, LocalDateTime.of(2022, 3, 8, 15, 58), 3);//id=5
        taskManager.createNewSubtask(subtask2);

        Subtask subtask3 = new Subtask("Subtask 3", "Description 3 ", 10, LocalDateTime.of(2022, 3, 8, 15, 45), 3);//id=6
        taskManager.createNewSubtask(subtask3);

        taskManager.getTaskById(1);
        /*HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response);


        kvServer.stop();
        httpTaskServer.stop();*/
    }
}
