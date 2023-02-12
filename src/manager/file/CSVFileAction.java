package manager.file;

import manager.history.HistoryManager;
import models.*;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class CSVFileAction {
    public static String toString(Task task) {
        if (task.typeClass() != TaskType.SUBTASK) {
            return (task.getId() + "," + task.typeClass() + "," + task.getName() + "," + task.getStatus() + ","
                    + task.getDescription() + "," + task.getStartTime() + "," + task.getDuration() + "," + task.getEndTime() + ",");
        }
        Subtask subtask = (Subtask) task;
        return (subtask.getId() + "," + task.typeClass() + "," + subtask.getName() + "," + subtask.getStatus() + ","
                + subtask.getDescription() + "," + subtask.getStartTime() + "," + subtask.getDuration() + "," + task.getEndTime() + ","
                + subtask.getEpicId());
    }

    public static Task fromString(String value) {
        String[] values = value.split(",");
        int id = Integer.parseInt(values[0]);
        TaskType taskType = TaskType.valueOf(values[1]);
        String name = values[2];
        TaskStatus taskStatus = TaskStatus.valueOf(values[3]);
        String description = values[4];
        LocalDateTime startTime;
        LocalDateTime endTime;
        if (values[5].equals("null")) {
            startTime = (LocalDateTime) null;
        } else {
            startTime = LocalDateTime.parse(values[5]);
        }
        long duration = Long.parseLong(values[6]);
        if (values[7].equals("null")) {
            endTime = (LocalDateTime) null;
        } else {
            endTime = LocalDateTime.parse(values[7]);
        }
        if (taskType.equals(TaskType.SUBTASK)) {
            int epicId = Integer.parseInt(values[8]);
            return new Subtask(name, description, id, taskStatus, duration, startTime, endTime, epicId);
        }
        if (taskType.equals(TaskType.EPIC)) {
            Epic epic = new Epic(name, description, id, taskStatus, duration, startTime, endTime);
            epic.setEndTime(endTime);
            return epic;
        }
        return new Task(name, description, id, taskStatus, duration, startTime, endTime);
    }

    public static String historyToString(HistoryManager manager) {
        StringBuilder result = new StringBuilder();
        for (Task task : manager.getHistory()) {
            result.append(task.getId() + ",");
        }
        return result.toString();
    }

    static List<Integer> historyFromString(String value) {
        List<Integer> history = new LinkedList<>();
        if (!value.equals("")) {
            String[] parts = value.split(",");
            for (String part : parts) {
                history.add(Integer.parseInt(part));
            }
        }
        return history;
    }
}
