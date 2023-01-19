package manager;

import models.*;

import java.util.LinkedList;
import java.util.List;

public class CSVFileAction {
    public static String toString(Task task) {
        if (task.typeClass() != TaskType.SUBTASK) {
            return (task.getId() + "," + task.typeClass() + "," + task.getName() + "," + task.getStatus() + ","
                    + task.getDescription() + ",");
        }
        Subtask subtask = (Subtask) task;
        return (subtask.getId() + "," + task.typeClass() + "," + subtask.getName() + "," + subtask.getStatus() + ","
                + subtask.getDescription() + "," + subtask.getEpicId());
    }

    public static Task fromString(String value) {
        String[] values = value.split(",");
        int id = Integer.parseInt(values[0]);
        TaskType taskType = TaskType.valueOf(values[1]);
        String name = values[2];
        TaskStatus taskStatus = TaskStatus.valueOf(values[3]);
        String description = values[4];
        if (taskType.equals(TaskType.SUBTASK)) {
            int epicId = Integer.parseInt(values[5]);
            return new Subtask(name, description, id, taskStatus, epicId);
        }
        if (taskType.equals(TaskType.EPIC)) {
            return new Epic(name, description, id, taskStatus);
        }
        return new Task(name, description, id, taskStatus);
    }

    public static String historyToString(HistoryManager manager) {
        StringBuilder result = new StringBuilder();
        for (Task task : manager.getHistory()) {
            result.append(task.getId() + ",");
        }
        return result.toString();
    }

    static List<Integer> historyFromString(String value) {
        String[] parts = value.split(",");
        List<Integer> history = new LinkedList<>();
        for (String part : parts) {
            history.add(Integer.parseInt(part));
        }
        return history;
    }
}
