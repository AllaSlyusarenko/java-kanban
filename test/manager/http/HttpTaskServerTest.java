package manager.http;

import com.google.gson.*;
import exceptions.ManagerSaveException;
import manager.Managers;
import models.Epic;
import models.Subtask;
import models.Task;
import org.junit.jupiter.api.*;
import server.KVServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HttpTaskServerTest {
    HttpTaskServer httpTaskServer;
    HttpClient httpClient;
    String url;
    KVServer kvServer;
    Gson gson;

    @BeforeAll
    void init1() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
    }

    @BeforeEach
    void init2() throws IOException, InterruptedException {
        httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
        url = "http://localhost:8080";
        httpClient = HttpClient.newHttpClient();
        gson = Managers.getGson();
        HttpRequest request1_1 = HttpRequest.newBuilder().uri(URI.create(url + "/tasks/task/")).DELETE().build();
        HttpRequest request2_1 = HttpRequest.newBuilder().uri(URI.create(url + "/tasks/epic/")).DELETE().build();
        HttpRequest request3_1 = HttpRequest.newBuilder().uri(URI.create(url + "/tasks/subtask/")).DELETE().build();
        httpClient.send(request1_1, HttpResponse.BodyHandlers.ofString());
        httpClient.send(request2_1, HttpResponse.BodyHandlers.ofString());
        httpClient.send(request3_1, HttpResponse.BodyHandlers.ofString());
    }

    @AfterEach
    void stop1() {
        httpTaskServer.stop();
    }

    @AfterAll
    void stop2() {
        kvServer.stop();
    }

    @Test
    void getDeleteAllTasks() {
        Task task1 = new Task("Task 1", "Description 1", 5, LocalDateTime.of(2023, 1, 31, 21, 20));
        URI uri = URI.create(url + "/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task1))).build();
        try {
            HttpResponse response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

            HttpRequest request2 = HttpRequest.newBuilder().uri(uri).GET().build();
            HttpResponse response2 = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());
            JsonElement jsonElement = JsonParser.parseString(response2.body().toString());
            JsonArray jsonArraytasks = jsonElement.getAsJsonArray();
            assertEquals(1, jsonArraytasks.size());

            HttpRequest request3 = HttpRequest.newBuilder().uri(uri).DELETE().build();
            HttpResponse response3 = httpClient.send(request3, HttpResponse.BodyHandlers.ofString());
            HttpRequest request4 = HttpRequest.newBuilder().uri(uri).GET().build();
            HttpResponse response4 = httpClient.send(request4, HttpResponse.BodyHandlers.ofString());
            JsonElement jsonElement4 = JsonParser.parseString(response4.body().toString());
            JsonArray jsonArraytasks4 = jsonElement4.getAsJsonArray();
            assertEquals(0, jsonArraytasks4.size());


        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException("Ошибка");
        }
    }

    @Test
    void getDeleteTaskById() {
        Task task1 = new Task("Task 1", "Description 1", 5, LocalDateTime.of(2023, 1, 31, 21, 20));
        URI uri = URI.create(url + "/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task1))).build();
        try {
            HttpResponse response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

            HttpRequest request2 = HttpRequest.newBuilder().uri(uri).GET().build();
            HttpResponse response2 = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());
            JsonElement jsonElement1 = JsonParser.parseString(response2.body().toString());
            JsonArray jsonArray1 = jsonElement1.getAsJsonArray();
            JsonObject jsonObject = jsonArray1.get(0).getAsJsonObject();
            int id = jsonObject.get("id").getAsInt();

            URI uri2 = URI.create(url + "/tasks/task/?id=" + id);
            HttpRequest request3 = HttpRequest.newBuilder().uri(uri2).GET().build();
            HttpResponse response3 = httpClient.send(request3, HttpResponse.BodyHandlers.ofString());
            JsonElement jsonElement2 = JsonParser.parseString(response3.body().toString());
            JsonObject jsonObject2 = jsonElement2.getAsJsonObject();
            int id2 = jsonObject2.get("id").getAsInt();
            assertEquals(id, id2);

            HttpRequest request4 = HttpRequest.newBuilder().uri(uri2).DELETE().build();
            HttpResponse response4 = httpClient.send(request4, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response4.statusCode());

            HttpRequest request5 = HttpRequest.newBuilder().uri(uri).GET().build();
            HttpResponse response5 = httpClient.send(request5, HttpResponse.BodyHandlers.ofString());
            JsonElement jsonElement5 = JsonParser.parseString(response5.body().toString());
            JsonArray jsonArraytasks = jsonElement5.getAsJsonArray();
            assertEquals(0, jsonArraytasks.size());


        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException("Ошибка");
        }
    }


    @Test
    void getDeleteAllEpics() {
        Epic epic1 = new Epic("Epic 1", "Epic 1");
        URI uri = URI.create(url + "/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic1))).build();
        try {
            HttpResponse response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

            HttpRequest request2 = HttpRequest.newBuilder().uri(uri).GET().build();
            HttpResponse response2 = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());
            JsonElement jsonElement = JsonParser.parseString(response2.body().toString());
            JsonArray jsonArrayepics = jsonElement.getAsJsonArray();
            assertEquals(1, jsonArrayepics.size());

            HttpRequest request3 = HttpRequest.newBuilder().uri(uri).DELETE().build();
            HttpResponse response3 = httpClient.send(request3, HttpResponse.BodyHandlers.ofString());
            HttpRequest request4 = HttpRequest.newBuilder().uri(uri).GET().build();
            HttpResponse response4 = httpClient.send(request4, HttpResponse.BodyHandlers.ofString());
            JsonElement jsonElement4 = JsonParser.parseString(response4.body().toString());
            JsonArray jsonArraytasks4 = jsonElement4.getAsJsonArray();
            assertEquals(0, jsonArraytasks4.size());

        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException("Ошибка");
        }
    }

    @Test
    void getDeleteEpicById() {
        Epic epic1 = new Epic("Epic 1", "Epic 1");
        URI uri = URI.create(url + "/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic1))).build();
        try {
            HttpResponse response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

            HttpRequest request2 = HttpRequest.newBuilder().uri(uri).GET().build();
            HttpResponse response2 = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());
            JsonElement jsonElement1 = JsonParser.parseString(response2.body().toString());
            JsonArray jsonArray1 = jsonElement1.getAsJsonArray();
            JsonObject jsonObject = jsonArray1.get(0).getAsJsonObject();
            int id = jsonObject.get("id").getAsInt();

            URI uri2 = URI.create(url + "/tasks/epic/?id=" + id);
            HttpRequest request3 = HttpRequest.newBuilder().uri(uri2).GET().build();
            HttpResponse response3 = httpClient.send(request3, HttpResponse.BodyHandlers.ofString());
            JsonElement jsonElement2 = JsonParser.parseString(response3.body().toString());
            JsonObject jsonObject2 = jsonElement2.getAsJsonObject();
            int id2 = jsonObject2.get("id").getAsInt();
            assertEquals(id, id2);

            HttpRequest request4 = HttpRequest.newBuilder().uri(uri2).DELETE().build();
            HttpResponse response4 = httpClient.send(request4, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response4.statusCode());

            HttpRequest request5 = HttpRequest.newBuilder().uri(uri).GET().build();
            HttpResponse response5 = httpClient.send(request5, HttpResponse.BodyHandlers.ofString());
            JsonElement jsonElement5 = JsonParser.parseString(response5.body().toString());
            JsonArray jsonArrayepics = jsonElement5.getAsJsonArray();
            assertEquals(0, jsonArrayepics.size());


        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException("Ошибка");
        }
    }


    @Test
    void getDeleteAllSubtasks() {
        Epic epic1 = new Epic("Epic 1", "Epic 1");
        URI uri1 = URI.create(url + "/tasks/epic/");
        HttpRequest request1 = HttpRequest.newBuilder().uri(uri1).POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic1))).build();
        try {
            HttpResponse response1 = httpClient.send(request1, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response1.statusCode());
            HttpRequest request2 = HttpRequest.newBuilder().uri(uri1).GET().build();
            HttpResponse response2 = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());
            JsonElement jsonElement1 = JsonParser.parseString(response2.body().toString());
            JsonArray jsonArray1 = jsonElement1.getAsJsonArray();
            JsonObject jsonObject = jsonArray1.get(0).getAsJsonObject();
            int id = jsonObject.get("id").getAsInt();

            URI uri2 = URI.create(url + "/tasks/subtask/");
            Subtask subtask2 = new Subtask("Subtask 2", "Subtask 2", 10, LocalDateTime.of(2022, 4, 15, 15, 30), id);
            HttpRequest request3 = HttpRequest.newBuilder().uri(uri2).POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask2))).build();
            HttpResponse response3 = httpClient.send(request3, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response3.statusCode());

            HttpRequest request4 = HttpRequest.newBuilder().uri(uri2).GET().build();
            HttpResponse response4 = httpClient.send(request4, HttpResponse.BodyHandlers.ofString());
            JsonElement jsonElement = JsonParser.parseString(response4.body().toString());
            JsonArray jsonArraysubtasks = jsonElement.getAsJsonArray();
            assertEquals(1, jsonArraysubtasks.size());

            HttpRequest request5 = HttpRequest.newBuilder().uri(uri2).DELETE().build();
            HttpResponse response5 = httpClient.send(request5, HttpResponse.BodyHandlers.ofString());
            HttpRequest request6 = HttpRequest.newBuilder().uri(uri2).GET().build();
            HttpResponse response6 = httpClient.send(request6, HttpResponse.BodyHandlers.ofString());
            JsonElement jsonElement6 = JsonParser.parseString(response6.body().toString());
            JsonArray jsonArraytasks6 = jsonElement6.getAsJsonArray();
            assertEquals(0, jsonArraytasks6.size());

        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException("Ошибка");
        }
    }

    @Test
    void getDeleteSubtaskById() {
        Epic epic1 = new Epic("Epic 1", "Epic 1");
        URI uri = URI.create(url + "/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic1))).build();
        try {
            HttpResponse response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

            HttpRequest request2 = HttpRequest.newBuilder().uri(uri).GET().build();
            HttpResponse response2 = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());
            JsonElement jsonElement1 = JsonParser.parseString(response2.body().toString());
            JsonArray jsonArray1 = jsonElement1.getAsJsonArray();
            JsonObject jsonObject = jsonArray1.get(0).getAsJsonObject();
            int idEpic = jsonObject.get("id").getAsInt(); //id epic

            URI uri2 = URI.create(url + "/tasks/subtask/");
            Subtask subtask2 = new Subtask("Subtask 2", "Subtask 2", 10, LocalDateTime.of(2022, 4, 15, 15, 30), idEpic);
            HttpRequest request3 = HttpRequest.newBuilder().uri(uri2).POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask2))).build();
            HttpResponse response3 = httpClient.send(request3, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response3.statusCode());

            HttpRequest request4 = HttpRequest.newBuilder().uri(uri2).GET().build();
            HttpResponse response4 = httpClient.send(request4, HttpResponse.BodyHandlers.ofString());
            JsonElement jsonElement2 = JsonParser.parseString(response4.body().toString());
            JsonArray jsonArray2 = jsonElement2.getAsJsonArray();
            JsonObject jsonObject2 = jsonArray2.get(0).getAsJsonObject();
            int id2 = jsonObject2.get("id").getAsInt(); //id subtask

            URI uri4 = URI.create(url + "/tasks/subtask/?id=" + id2);
            HttpRequest request5 = HttpRequest.newBuilder().uri(uri4).GET().build();
            HttpResponse response5 = httpClient.send(request5, HttpResponse.BodyHandlers.ofString());
            JsonElement jsonElement5 = JsonParser.parseString(response5.body().toString());
            JsonObject jsonObject5 = jsonElement5.getAsJsonObject();
            int id5 = jsonObject5.get("id").getAsInt();
            assertEquals(id2, id5);

            HttpRequest request6 = HttpRequest.newBuilder().uri(uri4).DELETE().build();
            HttpResponse response6 = httpClient.send(request6, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response6.statusCode());

            HttpRequest request7 = HttpRequest.newBuilder().uri(uri2).GET().build();
            HttpResponse response7 = httpClient.send(request7, HttpResponse.BodyHandlers.ofString());
            JsonElement jsonElement7 = JsonParser.parseString(response7.body().toString());
            JsonArray jsonArraysubtasks = jsonElement7.getAsJsonArray();
            assertEquals(0, jsonArraysubtasks.size());
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException("Ошибка");
        }
    }

    @Test
    void getHistory() {
        Task task1 = new Task("Task 1", "Description 1", 5, LocalDateTime.of(2023, 1, 31, 21, 20));
        URI uri = URI.create(url + "/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task1))).build();
        try {
            HttpResponse response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

            HttpRequest request2 = HttpRequest.newBuilder().uri(uri).GET().build();
            HttpResponse response2 = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());

            URI uri3 = URI.create(url + "/tasks/history");
            HttpRequest request3 = HttpRequest.newBuilder().uri(uri3).GET().build();
            HttpResponse response3 = httpClient.send(request3, HttpResponse.BodyHandlers.ofString());
            JsonElement jsonElement3 = JsonParser.parseString(response3.body().toString());
            JsonArray jsonArraytasks = jsonElement3.getAsJsonArray();
            assertEquals(1, jsonArraytasks.size());
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException("Ошибка");
        }
    }

    @Test
    void getPrioritizedTasks() {
        Task task1 = new Task("Task 1", "Description 1", 5, LocalDateTime.of(2023, 1, 31, 21, 20));
        URI uri = URI.create(url + "/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task1))).build();
        try {
            HttpResponse response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

            HttpRequest request2 = HttpRequest.newBuilder().uri(uri).GET().build();
            HttpResponse response2 = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());

            URI uri3 = URI.create(url + "/tasks/");
            HttpRequest request3 = HttpRequest.newBuilder().uri(uri3).GET().build();
            HttpResponse response3 = httpClient.send(request3, HttpResponse.BodyHandlers.ofString());
            JsonElement jsonElement3 = JsonParser.parseString(response3.body().toString());
            JsonArray jsonArraytasks = jsonElement3.getAsJsonArray();
            assertEquals(1, jsonArraytasks.size());

        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException("Ошибка");
        }
    }
}