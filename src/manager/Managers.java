package manager;

import adapter.LocaleDateTimeTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import manager.history.HistoryManager;
import manager.history.InMemoryHistoryManager;
import manager.http.HttpTaskManager;

import java.time.LocalDateTime;

public class Managers {
    private static TaskManager taskManager = new HttpTaskManager("http://localhost:8078");

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefault() {
        return taskManager;
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocaleDateTimeTypeAdapter());
        return gsonBuilder.create();
    }
}