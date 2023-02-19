package manager.http;

import com.google.gson.*;
import manager.Managers;
import manager.file.FileBackedTasksManager;
import models.Epic;
import models.Subtask;
import models.Task;

public class HttpTaskManager extends FileBackedTasksManager {
    private final KVTaskClient kvTaskClient;
    private Gson gson;

    public HttpTaskManager(int PORT) {
        super(null);
        gson = Managers.getGson();
        kvTaskClient = new KVTaskClient(PORT);
        load();
    }

    @Override
    public void save() {
        String jsonTasks = gson.toJson(tasks);
        kvTaskClient.put("tasks", jsonTasks);

        String jsonEpics = gson.toJson(epics);
        kvTaskClient.put("epics", jsonEpics);

        String jsonSubtasks = gson.toJson(subtasks);
        kvTaskClient.put("subtasks", jsonSubtasks);

        String jsonHistory = gson.toJson(historyManager.getHistory()); //положить или возвращать только список id
        kvTaskClient.put("history", jsonHistory);
    }

    private void load() {
        JsonElement jsonElementTasks = JsonParser.parseString(kvTaskClient.load("tasks"));
        if (!jsonElementTasks.isJsonNull()) {
            JsonArray jsonArrayTasks = jsonElementTasks.getAsJsonArray();
            for (JsonElement jsonElement : jsonArrayTasks) {
                Task task = gson.fromJson(jsonElement, Task.class);
                createNewTask(task);
            }
        }
        JsonElement jsonElementSubtasks = JsonParser.parseString(kvTaskClient.load("subtasks"));
        if (!jsonElementSubtasks.isJsonNull()) {
            JsonArray jsonArraySubtasks = jsonElementSubtasks.getAsJsonArray();
            for (JsonElement jsonElement : jsonArraySubtasks) {
                Subtask subtask = gson.fromJson(jsonElement, Subtask.class);
                createNewSubtask(subtask);
            }
        }
        JsonElement jsonElementEpics = JsonParser.parseString(kvTaskClient.load("epics"));
        if (!jsonElementEpics.isJsonNull()) {
            JsonArray jsonArrayEpics = jsonElementEpics.getAsJsonArray();
            for (JsonElement jsonElement : jsonArrayEpics) {
                Epic epic = gson.fromJson(jsonElement, Epic.class);
                createNewEpic(epic);
            }
        }
        JsonElement jsonElementHistory = JsonParser.parseString(kvTaskClient.load("history"));
        if (!jsonElementHistory.isJsonNull()) {
            JsonArray jsonArrayHistory = jsonElementHistory.getAsJsonArray();
            for (JsonElement jsonElement : jsonArrayHistory) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                int id = jsonObject.get("id").getAsInt();
                if (tasks.containsKey(id)) {
                    historyManager.add(tasks.get(id));
                } else if (subtasks.containsKey(id)) {
                    historyManager.add(subtasks.get(id));
                } else if (epics.containsKey(id)) {
                    historyManager.add(epics.get(id));
                }
            }
        }
    }
}
