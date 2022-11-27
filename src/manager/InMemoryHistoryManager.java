package manager;

import models.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    List<Task> inMemory = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (inMemory.size() >= 10) {
            inMemory.remove(0);
            inMemory.add(task);
        } else {
            inMemory.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return inMemory;
    }
}