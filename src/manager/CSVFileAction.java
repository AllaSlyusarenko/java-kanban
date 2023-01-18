package manager;

import models.*;

import java.util.LinkedList;
import java.util.List;

public class CSVFileAction {
    static TaskType typeClass(Task task) {
        if (task.getClass().getTypeName().equals("models.Task")) {
            return TaskType.TASK;
        }
        if (task.getClass().getTypeName().equals("models.Epic")) {
            return TaskType.EPIC;
        }
        if (task.getClass().getTypeName().equals("models.Subtask")) {
            return TaskType.SUBTASK;
        }
        return null;
    }

    public static String toString(Task task) {
        String result = "";
        if (task.getClass().getTypeName().equals("models.Task")) {
            result = task.getId() + "," + typeClass(task) + "," + task.getName() + "," + task.getStatus() + ","
                    + task.getDescription() + ",";
        }
        if (task.getClass().getTypeName().equals("models.Epic")) {
            Epic epic = (Epic) task;
            result = epic.getId() + "," + typeClass(task) + "," + epic.getName() + "," + epic.getStatus() + ","
                    + epic.getDescription() + ",";
        }
        if (task.getClass().getTypeName().equals("models.Subtask")) {
            Subtask subtask = (Subtask) task;
            result = subtask.getId() + "," + typeClass(task) + "," + subtask.getName() + "," + subtask.getStatus() + ","
                    + subtask.getDescription() + "," + subtask.getEpicId();
        }
        return result;
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
            Task subtask = new Subtask(name, description, epicId);
            subtask.setId(id);
            subtask.setStatus(taskStatus);
            return subtask;
        }
        if (taskType.equals(TaskType.EPIC)) {
            Task epic = new Epic(name, description);
            epic.setId(id);
            epic.setStatus(taskStatus);
            return epic;
        }
        if (taskType.equals(TaskType.TASK)) {
            Task task = new Task(name, description);
            task.setId(id);
            task.setStatus(taskStatus);
            return task;
        }
        return null;
    }

    public static String historyToString(HistoryManager manager) {
        String result = "";
        for (Task task : manager.getHistory()) {
            result = result + task.getId() + ",";
        }
        return result;
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
