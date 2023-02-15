package manager;

import adapter.LocaleDateTimeTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import manager.file.FileBackedTasksManager;
import manager.history.HistoryManager;
import manager.history.InMemoryHistoryManager;
import manager.http.HttpTaskManager;
import manager.memory.InMemoryTaskManager;

import java.time.LocalDateTime;

public class Managers {
    private static TaskManager taskManager = new InMemoryTaskManager();

    public static TaskManager getDefault() {
        return taskManager;
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    /*public static TaskManager getDefault() {
        return new HttpTaskManager(KVServer.PORT);
    }*/
    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocaleDateTimeTypeAdapter());
        //gsonBuilder.serializeNulls();
        return gsonBuilder.create();
    }
}