package manager.http;

import manager.TaskManager;
import manager.file.FileBackedTasksManager;
import models.Epic;
import models.Subtask;
import models.Task;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskManager  extends FileBackedTasksManager {
    private final HttpClient kvServerClient = HttpClient.newHttpClient();// кв сервер будет знать, что мы хотим с ним взаимодействовать
    // из него можно получить исходное состояние менеджера
    private final String API_TOKEN;

    public HttpTaskManager() throws IOException, InterruptedException {
        super(new File(""));        // эту строчку очень посмотреть!!!!!!
        URI uri = URI.create("http://localhost:8078/register"); // регистрируем

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("Content-type", "application/json")
                .uri(uri)
                .build();

        HttpResponse<String> apiToken =
                kvServerClient.send(request,  HttpResponse.BodyHandlers.ofString());
        API_TOKEN = apiToken.body();
    }

    //клиент, который будет общаться с кв сервером, через которого мы сможем  к кв серверу  будем посылать
// запросы на сохранение и на загрузку данных
    /*@Override
    public void createNewTask(Task task) {
        URI uri = URI.create("http://localhost:8078/save/tasks?API_TOKEN=" + API_TOKEN) ;

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString("{}"))  //в json
                .header("Content-type", "application/json")
                .uri(uri)
                .build();
        try {
            //HttpResponse<String> response =
                    kvServerClient.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (IOException|InterruptedException e){
            // или исключение или сообщение

        }
    }*/

    // нужно заменить вызов сохранения в файлах на вызов клиента
// переопределяем метод save. Раньше он сохранял данные в файл, а теперь - на kvserver
    // для этого делаем запросы к kvserver на сохранение данных по ключам tasks, epics, subtasks и history
    // таким образом на сервере в хэш мапе будут храниться данные по ключам tasks, epics, subtasks и history,
    // а значения - все задачи/эпики/подзадачи/история в формате json
    @Override
    public void save() {
        URI tasksUri = URI.create("http://localhost:8078/save/tasks?API_TOKEN=" + API_TOKEN);
        URI epicUri = URI.create("http://localhost:8078/save/epics?API_TOKEN=" + API_TOKEN);
        URI subtasksUri = URI.create("http://localhost:8078/save/subtasks?API_TOKEN=" + API_TOKEN);
        // и для истории

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString("{}")) // тело запроса - все задачи в формате json: "[{"id":1}, {"id":2}]"
                .uri(tasksUri)
                .build();
        // + запросы для эпиков, подзадач и истории

        try {
            kvServerClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {

        }
    }



    /*@Override
    public void loadFromFile() {
        URI tasksUri = URI.create("http://localhost:8078/load/tasks?API_TOKEN=" + API_TOKEN);
        URI epicUri = URI.create("http://localhost:8078/load/epics?API_TOKEN=" + API_TOKEN);
        URI subtasksUri = URI.create("http://localhost:8078/load/subtasks?API_TOKEN=" + API_TOKEN);

        // делаем запросы к kvserver, получаем от него задачи в формате json и складываем их в хэш мапы
    }*/

}
