package server;

import com.google.gson.Gson;
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

        KVServer kvServer = new KVServer(); //8078
        kvServer.start();
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
        /*TaskManager taskManager = httpTaskServer.getTaskManager();
        Task task1 = new Task("Task 1", "Description 1 ", 5, LocalDateTime.of(2022, 3, 8, 15, 30));//id=1
        HttpClient httpClient = HttpClient.newHttpClient();
        Gson gson = Managers.getGson();

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8078/save/Task1?API_TOKEN=DEBUG"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task1))).build();
        HttpResponse<String> response = httpClient.send(request,HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

        HttpRequest request2 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8078/load/Task1?API_TOKEN=DEBUG"))
                .GET().build();
        HttpResponse<String> response2 = httpClient.send(request2,HttpResponse.BodyHandlers.ofString());

        Task task2 = new Task("Task 2", "Description 2 ", 5, LocalDateTime.of(2022, 3, 8, 15, 30));//id=1
        HttpRequest request3 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8078/save/Task1?API_TOKEN=DEBUG"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task2))).build();
        HttpResponse<String> response3 = httpClient.send(request3,HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

        HttpRequest request4 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8078/load/Task1?API_TOKEN=DEBUG"))
                .GET().build();
        HttpResponse<String> response4 = httpClient.send(request4,HttpResponse.BodyHandlers.ofString());
*/
        //kvServer.stop();
        //httpTaskServer.stop();

    }
}
