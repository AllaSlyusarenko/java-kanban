package manager;

import models.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private LinkedList<Task> historyOfViewedTasks = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (historyOfViewedTasks.size() == 10) {
            historyOfViewedTasks.removeFirst();
        }
        historyOfViewedTasks.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return historyOfViewedTasks;
    }
}