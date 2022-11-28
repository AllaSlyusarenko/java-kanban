package manager;

import models.Epic;
import models.Subtask;
import models.Task;
import models.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    public static int globalId = 1;
    private final HistoryManager historyManager = Managers.getDefaultHistory();


    private int generateId() {
        return globalId++;
    }

    @Override
    public void createNewTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
    }

    @Override
    public void createNewEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
    }

    @Override
    public void createNewSubtask(Subtask subtask) {
        if (epics.containsKey(subtask.getEpicId())) {
            subtask.setId(generateId());
            subtasks.put(subtask.getId(), subtask);
            epics.get(subtask.getEpicId()).getIncomingSubtasksId().add(subtask.getId());
            epics.get(subtask.getEpicId()).setStatus(calculateStatus(subtask.getEpicId()));
        } else {
            System.out.println("Проверьте корректность вводимых данных в " + subtask.getName()
                    + " : на момент ввода нет эпика с номером " + subtask.getEpicId() + "\n");
        }
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> allTasks = new ArrayList<>(tasks.values());
        return allTasks;
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        ArrayList<Epic> allEpics = new ArrayList<>(epics.values());
        return allEpics;
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        ArrayList<Subtask> allSubtasks = new ArrayList<>(subtasks.values());
        return allSubtasks;
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        deleteAllSubtasks();
        epics.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        for (int epicID : epics.keySet()) {
            epics.get(epicID).setIncomingSubtasksId(new ArrayList<>());
            epics.get(epicID).setStatus(calculateStatus(epicID));
        }
        subtasks.clear();
    }

    @Override
    public Task getTaskById(int id) {
        if (tasks.containsKey(id)) {
            Task task = tasks.get(id);
            historyManager.add(task);
            return task;

        } else {
            System.out.println("Проверьте корректность вводимых данных"
                    + " : на момент ввода нет таска с номером " + id + "\n");
            return null;
        }
    }

    @Override
    public Epic getEpicById(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            historyManager.add(epic);
            return epic;
        } else {
            System.out.println("Проверьте корректность вводимых данных"
                    + " : на момент ввода нет эпика с номером " + id + "\n");
            return null;
        }
    }

    @Override
    public Subtask getSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.get(id);
            historyManager.add(subtask);
            return subtask;
        } else {
            System.out.println("Проверьте корректность вводимых данных"
                    + " : на момент ввода нет сабтаска с номером " + id + "\n");
            return null;
        }
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Проверьте корректность вводимых данных" + task.getName()
                    + " : на момент ввода нет таска с номером " + task.getId() + "\n");
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId()) && epic.getName() != null && epic.getDescription() != null
                && epic.getIncomingSubtasksId() != null) {
            Epic oldEpic = epics.get(epic.getId());
            oldEpic.setName(epic.getName());
            oldEpic.setDescription(epic.getDescription());
            oldEpic.setIncomingSubtasksId(epic.getIncomingSubtasksId());
            oldEpic.setStatus(calculateStatus(epic.getId()));
        } else {
            System.out.println("Проверьте корректность вводимых данных" + epic.getName() + "\n");
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            epics.get(subtask.getEpicId()).setStatus(calculateStatus(subtask.getEpicId()));
        } else {
            System.out.println("Проверьте корректность вводимых данных" + subtask.getName()
                    + " : на момент ввода нет сабтаска с номером " + subtask.getId() + "\n");
        }
    }

    @Override
    public void deleteTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else {
            System.out.println("Проверьте корректность вводимых данных"
                    + " : на момент ввода нет таска с номером " + id + "\n");
        }
    }

    @Override
    public void deleteSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            int idEpic = subtasks.get(id).getEpicId();
            subtasks.remove(id);
            ArrayList<Integer> newArray = epics.get(idEpic).getIncomingSubtasksId();
            newArray.remove(Integer.valueOf(id));
            epics.get(idEpic).setIncomingSubtasksId(newArray);
            epics.get(idEpic).setStatus(calculateStatus(idEpic));
        } else {
            System.out.println("Проверьте корректность вводимых данных"
                    + " : на момент ввода нет сабтаска с номером " + id + "\n");
        }
    }

    @Override
    public void deleteSubtaskByIdEpic(int epicId) {
        if (epics.containsKey(epicId)) {
            ArrayList<Integer> newArray = epics.get(epicId).getIncomingSubtasksId();
            for (Integer id : newArray) {
                subtasks.remove(id);
            }
        } else {
            System.out.println("Проверьте корректность вводимых данных"
                    + " : на момент ввода нет эпика с номером " + epicId + "\n");
        }
    }

    @Override
    public void deleteEpicById(int id) {
        if (epics.containsKey(id)) {
            deleteSubtaskByIdEpic(id);
            epics.remove(id);
        } else {
            System.out.println("Проверьте корректность вводимых данных"
                    + " : на момент ввода нет эпика с номером " + id + "\n");
        }
    }

    @Override
    public ArrayList<Subtask> getAllEpicSubtasks(int id) {
        if (epics.containsKey(id)) {
            ArrayList<Subtask> epicSubtasks = new ArrayList<>();
            for (Integer idSubtask : epics.get(id).getIncomingSubtasksId()) {
                epicSubtasks.add(subtasks.get(idSubtask));
            }
            return epicSubtasks;
        } else {
            System.out.println("Проверьте корректность вводимых данных"
                    + " : на момент ввода нет эпика с номером " + id + "\n");
            return null;
        }
    }

    public TaskStatus defineStatusEpicByAllSubtasks(int epicId) {
        int summs = 0;
        for (int id : epics.get(epicId).getIncomingSubtasksId()) {
            if (subtasks.get(id).getStatus().equals("DONE")) {
                summs += 2;
            } else if (subtasks.get(id).getStatus().equals("IN_PROGRESS")) {
                summs += 1;
            }
        }
        if (summs == 0) {
            return TaskStatus.NEW;
        } else if (summs == 2 * epics.get(epicId).getIncomingSubtasksId().size()) {
            return TaskStatus.DONE;
        } else {
            return TaskStatus.IN_PROGRESS;
        }
    }

    @Override
    public TaskStatus calculateStatus(int epicId) {
        if (epics.get(epicId).getIncomingSubtasksId().isEmpty()) {
            return TaskStatus.NEW;
        } else {
            return defineStatusEpicByAllSubtasks(epicId);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}

