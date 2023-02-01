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
    private Comparator<? super LocalDateTime> taskComparator = new Comparator<LocalDateTime>() {
        @Override
        public int compare(LocalDateTime o1, LocalDateTime o2) {
            int result = 0;
            if ((o1 == (LocalDateTime) null && o2 == (LocalDateTime) null)) {
                result = 0;
            } else if (o1 == (LocalDateTime) null) {
                result = 1;
            } else if (o2 == (LocalDateTime) null) {
                result = -1;
            } else if (o1.isAfter(o2)) {
                result = 1;
            } else if (o1.isBefore(o2)) {
                result = -1;
            }
            return result;
        }
    };
    protected final TreeMap<LocalDateTime, Task> prioritizedTasks = new TreeMap<>(taskComparator);
    protected static int globalId = 1;
    protected HistoryManager historyManager = Managers.getDefaultHistory();

    protected int generateId() {
        return globalId++;
    }

    private boolean validationIntersection(Task task) {
        boolean result = false;
        if (prioritizedTasks.isEmpty()) {
            return true;
        }
        if (task.getStartTime() == ((LocalDateTime) null)) {
            return true;
        }
        if (prioritizedTasks.containsKey(task.getStartTime()) && task.getId() == prioritizedTasks.get(task.getStartTime()).getId()
                && task.getDuration() == prioritizedTasks.get(task.getStartTime()).getDuration()) {
            return true;
        }
        for (Map.Entry<LocalDateTime, Task> entry : prioritizedTasks.entrySet()) {
            if (entry.getKey() == null) {
                return true;
            }
            if (task.getEndTime().isBefore(entry.getKey()) || task.getStartTime().isAfter(entry.getValue().getEndTime())) {
                return true;
            } else if (task.getStartTime().isAfter(entry.getKey()) && task.getStartTime().isBefore(entry.getValue().getEndTime())) {
                return false;
            } else if (task.getEndTime().isAfter(entry.getKey()) && task.getEndTime().isBefore(entry.getValue().getEndTime())) {
                return false;
            }
        }
        return result;
    }

    @Override
    public void createNewTask(Task task) {
        if (validationIntersection(task)) {
            task.setId(generateId());
            tasks.put(task.getId(), task);
            prioritizedTasks.put(task.getStartTime(), task);
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
                epics.get(subtask.getEpicId()).setStartTime(calculateStartTime(subtask.getEpicId()));
                epics.get(subtask.getEpicId()).setDuration(calculateDuration(subtask.getEpicId()));
                epics.get(subtask.getEpicId()).setEndTime(calculateEndTime(subtask.getEpicId()));
                prioritizedTasks.put(subtask.getStartTime(), subtask);
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
            epics.get(epicID).setStatus(calculateStatus(epicID));
            epics.get(epicID).setStartTime(calculateStartTime(epicID));
            epics.get(epicID).setDuration(calculateDuration(epicID));
            epics.get(epicID).setEndTime(calculateEndTime(epicID));

        }
        for (Integer id : subtasks.keySet()) {
            historyManager.remove(id);
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
        if (validationIntersection(task)) {
            if (tasks.containsKey(task.getId())) {
                tasks.put(task.getId(), task);
                prioritizedTasks.put(task.getStartTime(), task);
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
            oldEpic.setStartTime(calculateStartTime(epic.getId()));
            oldEpic.setDuration(calculateDuration(epic.getId()));
            oldEpic.setEndTime(calculateEndTime(epic.getId()));
        } else {
            System.out.println("Проверьте корректность вводимых данных " + epic.getName() + "\n");
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (validationIntersection(subtask)) {
            if (subtasks.containsKey(subtask.getId())) {
                subtasks.put(subtask.getId(), subtask);
                epics.get(subtask.getEpicId()).setStatus(calculateStatus(subtask.getEpicId()));
                epics.get(subtask.getEpicId()).setStartTime(calculateStartTime(subtask.getEpicId()));
                epics.get(subtask.getEpicId()).setDuration(calculateDuration(subtask.getEpicId()));
                epics.get(subtask.getEpicId()).setEndTime(calculateEndTime(subtask.getEpicId()));
                prioritizedTasks.put(subtask.getStartTime(), subtask);
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
            subtasks.remove(id);
            ArrayList<Integer> newArray = epics.get(idEpic).getIncomingSubtasksId();
            newArray.remove(Integer.valueOf(id));
            epics.get(idEpic).setIncomingSubtasksId(newArray);
            epics.get(idEpic).setStatus(calculateStatus(idEpic));
            epics.get(idEpic).setStartTime(calculateStartTime(idEpic));
            epics.get(idEpic).setDuration(calculateDuration(idEpic));
            epics.get(idEpic).setEndTime(calculateEndTime(idEpic));
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
                subtasks.remove(id);
                historyManager.remove(id);
                epics.get(epicId).setStartTime(null);
                epics.get(epicId).setDuration(0);
                epics.get(epicId).setEndTime(null);
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

    private LocalDateTime calculateStartTime(int epicId) { // дописать метод для эпика
        ArrayList<Subtask> epicSubtasks = getAllEpicSubtasks(epicId);
        LocalDateTime start = null;
        if (epicSubtasks.isEmpty()) {
            return null;
        } else {
            for (int i = 0; i < epicSubtasks.size(); i++) {
                if (start == null) {
                    if (epicSubtasks.get(i).getStartTime() != null) {
                        start = epicSubtasks.get(i).getStartTime();
                    }/* else {
                        if (epicSubtasks.get(i).getStartTime().isBefore(start)) {
                            start = epicSubtasks.get(i).getStartTime();
                        }
                    }*/
                } else {
                    if (epicSubtasks.get(i).getStartTime() != null) {
                        if (epicSubtasks.get(i).getStartTime().isBefore(start)) {
                            start = epicSubtasks.get(i).getStartTime();
                        }
                    }
                }
            }
        }
        return start;
    }

    private long calculateDuration(int epicId) {
        ArrayList<Subtask> epicSubtasks = getAllEpicSubtasks(epicId);
        long duration = 0;
        if (epicSubtasks.isEmpty()) {
            duration = 0;
        }
        if (!epicSubtasks.isEmpty()) {
            for (int i = 0; i < epicSubtasks.size(); i++) {
                duration += epicSubtasks.get(i).getDuration();
            }
        }
        return duration;
    }

    private LocalDateTime calculateEndTime(int epicId) {
        if (calculateStartTime(epicId) == null) {
            return null;
        } else {
            return calculateStartTime(epicId).plusMinutes(calculateDuration(epicId));
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public TreeMap<LocalDateTime, Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }
}

