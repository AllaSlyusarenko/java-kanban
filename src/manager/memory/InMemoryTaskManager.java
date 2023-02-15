package manager.memory;

import manager.Managers;
import manager.TaskManager;
import manager.history.HistoryManager;
import models.Epic;
import models.Subtask;
import models.Task;
import models.TaskStatus;

import java.time.LocalDateTime;
import java.util.*;


public class InMemoryTaskManager implements TaskManager {
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected final TreeSet<Task> prioritizedTasks = new TreeSet<>(
            Comparator.comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder())).thenComparing(Task::getId));
    protected int globalId = 1; //static
    protected HistoryManager historyManager = Managers.getDefaultHistory();

    protected int generateId() {
        return globalId++;
    }

    private boolean validationIntersection(Task task) {
        if (task.getStartTime() == null) {
            return true;
        }
        for (Task entry : prioritizedTasks) {
            if (task.getId() == entry.getId()) {
                continue;
            }
            if (entry.getStartTime() != null) {
                if (!task.getStartTime().isBefore(entry.getStartTime()) && task.getStartTime().isBefore(entry.getEndTime())) {
                    return false;
                }
                if (task.getEndTime().isAfter(entry.getStartTime()) && (!task.getEndTime().isAfter(entry.getEndTime()))) {
                    return false;
                }
                if (!task.getStartTime().isAfter(entry.getStartTime()) && (!task.getEndTime().isBefore(entry.getEndTime()))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void createNewTask(Task task) { // нужно добавить int id
        if (validationIntersection(task)) {
            task.setId(generateId());
            tasks.put(task.getId(), task);
            prioritizedTasks.add(task);
        } else {
            System.out.println("Есть пересечение по времени с другими задачами");
        }
    }

    @Override
    public void createNewEpic(Epic epic) {
        epic.setId(generateId());
        epic.setStatus(TaskStatus.NEW);
        epics.put(epic.getId(), epic);
    }

    @Override
    public void createNewSubtask(Subtask subtask) {
        if (validationIntersection(subtask)) {
            if (epics.containsKey(subtask.getEpicId())) {
                subtask.setId(generateId());
                subtasks.put(subtask.getId(), subtask);
                epics.get(subtask.getEpicId()).getIncomingSubtasksId().add(subtask.getId());
                epics.get(subtask.getEpicId()).setStatus(calculateStatus(subtask.getEpicId()));
                calculateTimeEpic(subtask.getEpicId());
                prioritizedTasks.add(subtask);
            } else {
                System.out.println("Проверьте корректность вводимых данных в " + subtask.getName()
                        + " : на момент ввода нет эпика с номером " + subtask.getEpicId() + "\n");
            }
        } else {
            System.out.println("Есть пересечение по времени с другими задачами");
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
        for (Integer id : tasks.keySet()) {
            historyManager.remove(id);
            prioritizedTasks.remove(tasks.get(id));
        }
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        deleteAllSubtasks();
        for (Integer id : epics.keySet()) {
            historyManager.remove(id);
        }
        epics.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        for (int epicID : epics.keySet()) {
            epics.get(epicID).setIncomingSubtasksId(new ArrayList<>());
            epics.get(epicID).setStatus(TaskStatus.NEW);
            calculateTimeEpic(epicID);
        }
        for (Integer id : subtasks.keySet()) {
            historyManager.remove(id);
            prioritizedTasks.remove(subtasks.get(id));

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
        Epic epic;
        if (epics.containsKey(id)) {
            epic = epics.get(id);
            historyManager.add(epic);
        } else {
            System.out.println("Проверьте корректность вводимых данных"
                    + " : на момент ввода нет эпика с номером " + id + "\n");
            epic = null;
        }
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask;
        if (subtasks.containsKey(id)) {
            subtask = subtasks.get(id);
            historyManager.add(subtask);
        } else {
            System.out.println("Проверьте корректность вводимых данных"
                    + " : на момент ввода нет сабтаска с номером " + id + "\n");
            subtask = null;
        }
        return subtask;
    }

    @Override
    public void updateTask(Task task) {
        Task taskForRemove = tasks.get(task.getId());
        if (validationIntersection(task)) {
            if (tasks.containsKey(task.getId())) {
                prioritizedTasks.remove(taskForRemove);
                tasks.put(task.getId(), task);
                prioritizedTasks.add(task);
            } else {
                System.out.println("Проверьте корректность вводимых данных" + task.getName()
                        + " : на момент ввода нет таска с номером " + task.getId() + "\n");
            }

        } else {
            System.out.println("Есть пересечение по времени с другими задачами");
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
            calculateTimeEpic(epic.getId());
        } else {
            System.out.println("Проверьте корректность вводимых данных " + epic.getName() + "\n");
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Subtask subtaskForRemove = subtasks.get(subtask.getId());
        if (validationIntersection(subtask)) {
            if (subtasks.containsKey(subtask.getId())) {
                subtasks.put(subtask.getId(), subtask);
                epics.get(subtask.getEpicId()).setStatus(calculateStatus(subtask.getEpicId()));
                calculateTimeEpic(subtask.getEpicId());
                prioritizedTasks.remove(subtaskForRemove);
                prioritizedTasks.add(subtask);
            } else {
                System.out.println("Проверьте корректность вводимых данных" + subtask.getName()
                        + " : на момент ввода нет сабтаска с номером " + subtask.getId() + "\n");
            }
        } else {
            System.out.println("Есть пересечение по времени с другими задачами");
        }
    }

    @Override
    public void deleteTaskById(int id) {
        if (tasks.containsKey(id)) {
            prioritizedTasks.remove(tasks.get(id));
            tasks.remove(id);
            historyManager.remove(id);
        } else {
            System.out.println("Проверьте корректность вводимых данных"
                    + " : на момент ввода нет таска с номером " + id + "\n");
        }
    }

    @Override
    public void deleteSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            int idEpic = subtasks.get(id).getEpicId();
            prioritizedTasks.remove(subtasks.get(id));
            subtasks.remove(id);
            ArrayList<Integer> newArray = epics.get(idEpic).getIncomingSubtasksId();
            newArray.remove(Integer.valueOf(id));
            epics.get(idEpic).setIncomingSubtasksId(newArray);
            epics.get(idEpic).setStatus(calculateStatus(idEpic));
            calculateTimeEpic(idEpic);
            historyManager.remove(id);
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
                prioritizedTasks.remove(subtasks.get(id));
                subtasks.remove(id);
                historyManager.remove(id);
            }
            epics.get(epicId).setIncomingSubtasksId(new ArrayList<>());
            epics.get(epicId).setStatus(TaskStatus.NEW);
            calculateTimeEpic(epicId);

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
            historyManager.remove(id);
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

    private TaskStatus defineStatusEpicByAllSubtasks(int epicId) {
        int summs = 0;
        for (int id : epics.get(epicId).getIncomingSubtasksId()) {
            if (subtasks.get(id).getStatus().equals(TaskStatus.DONE)) {
                summs += 2;
            } else if (subtasks.get(id).getStatus().equals(TaskStatus.IN_PROGRESS)) {
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

    private TaskStatus calculateStatus(int epicId) {
        if (epics.get(epicId).getIncomingSubtasksId().isEmpty()) {
            return TaskStatus.NEW;
        } else {
            return defineStatusEpicByAllSubtasks(epicId);
        }
    }

    private void calculateTimeEpic(int epicId) {
        ArrayList<Subtask> epicSubtasks = getAllEpicSubtasks(epicId);
        LocalDateTime startTimeEpic = null;
        long duration = 0;
        LocalDateTime endTimeEpic = null;
        if (epicSubtasks.isEmpty()) {
            epics.get(epicId).setStartTime(startTimeEpic);
            epics.get(epicId).setDuration(duration);
            epics.get(epicId).setEndTime(endTimeEpic);
        }
        if (!epicSubtasks.isEmpty()) {
            for (Subtask subtask : epicSubtasks) {
                if (startTimeEpic == null) {
                    if (subtask.getStartTime() != null && subtask.getEndTime() != null) {
                        startTimeEpic = subtask.getStartTime();
                        duration += subtask.getDuration();
                        endTimeEpic = subtask.getEndTime();
                    }
                } else {
                    if (subtask.getStartTime() != null) {
                        if (subtask.getStartTime().isBefore(startTimeEpic)) {
                            startTimeEpic = subtask.getStartTime();
                            duration += subtask.getDuration();
                        }
                        if (subtask.getEndTime().isAfter(endTimeEpic)) {
                            endTimeEpic = subtask.getEndTime();
                        }
                    }
                }
            }
            epics.get(epicId).setStartTime(startTimeEpic);
            epics.get(epicId).setDuration(duration);
            epics.get(epicId).setEndTime(endTimeEpic);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public ArrayList<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }
}

