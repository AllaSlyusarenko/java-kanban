import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();

    void createNewTask(Task task) {
        tasks.put(generateIdNewTask(task), task);
    }

    int generateIdNewTask(Task task) {
        int idNewTask = getMaxIdTasks() + 1;
        task.setId(idNewTask);
        return idNewTask;
    }

    int getMaxIdTasks() {
        int maxValueIdTask = 0;
        for (int key : tasks.keySet()) {
            if (maxValueIdTask < key) {
                maxValueIdTask = key;
            }
        }
        return maxValueIdTask;
    }

    void createNewEpic(Epic epic) {
        epics.put(generateIdNewEpic(epic), epic);
    }

    int generateIdNewEpic(Epic epic) {
        int idNewEpic = getMaxIdEpics() + 1;
        epic.setId(idNewEpic);
        return idNewEpic;
    }

    int getMaxIdEpics() {
        int maxValueIdEpic = 0;
        for (int key : epics.keySet()) {
            if (maxValueIdEpic < key) {
                maxValueIdEpic = key;
            }
        }
        return maxValueIdEpic;
    }

    void createNewSubtask(Subtask subtask) {
        subtasks.put(generateIdNewSubtask(subtask), subtask);
        epics.get(subtask.epicId).incomingSubtasksId.add(subtask.getId());
        epics.get(subtask.epicId).setStatus(calculateStatus(subtask.epicId));
    }

    int generateIdNewSubtask(Subtask subtask) {
        int idNewSubtask = getMaxIdSubtasks() + 1;
        subtask.setId(idNewSubtask);
        return idNewSubtask;
    }

    int getMaxIdSubtasks() {
        int maxValueIdSubtask = 0;
        for (int key : subtasks.keySet()) {
            if (maxValueIdSubtask < key) {
                maxValueIdSubtask = key;
            }
        }
        return maxValueIdSubtask;
    }

    HashMap getAllTasks() {
        return tasks;
    }

    HashMap getAllEpics() {
        return epics;
    }

    HashMap getAllSubtasks() {
        return subtasks;
    }

    void deleteAllTasks() {
        tasks.clear();
    }

    void deleteAllEpics() {
        deleteAllSubtasks();
        epics.clear();
    }

    void deleteAllSubtasks() {
        for (int epicID : epics.keySet()) {
            epics.get(epicID).setIncomingSubtasksId(new ArrayList<>());
            epics.get(epicID).setStatus(calculateStatus(epicID));
        }
        subtasks.clear();
    }

    Task getTaskById(int id) {
        return tasks.get(id);
    }

    Epic getEpicById(int id) {
        return epics.get(id);
    }

    Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    void updateEpic(Epic epic) {
        Epic oldEpic = epics.get(epic.getId());
        oldEpic.setName(epic.getName());
        oldEpic.setDescription(epic.getDescription());
        oldEpic.setStatus(calculateStatus(epic.getId()));
        oldEpic.setIncomingSubtasksId(epic.getIncomingSubtasksId());

    }

    void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        epics.get(subtask.getEpicId()).setStatus(calculateStatus(subtask.getEpicId()));
    }

    void deleteTaskById(int id) {
        tasks.remove(id);
    }

    void deleteSubtaskById(int id) {

        int idEpic = subtasks.get(id).getEpicId();
        subtasks.remove(id);
        ArrayList<Integer> newArray = epics.get(idEpic).getIncomingSubtasksId();
        int indexInArray = newArray.indexOf((Integer) id);
        newArray.remove(indexInArray);
        epics.get(idEpic).setIncomingSubtasksId(newArray);
        epics.get(idEpic).setStatus(calculateStatus(idEpic));

    }

    void deleteSubtaskByIdEpic(int epicId) {
        ArrayList<Integer> newArray = epics.get(epicId).getIncomingSubtasksId();
        for (Integer id : newArray) {
            subtasks.remove(id);
        }
    }

    void deleteEpicById(int id) {
        deleteSubtaskByIdEpic(id);
        epics.remove(id);
    }

    ArrayList getAllEpicSubtasks(int id) {
        return epics.get(id).getIncomingSubtasksId();

    }

    String defineStatusEpicByAllSubtasks(int epicId) {
        int summs = 0;
        for (int id : epics.get(epicId).getIncomingSubtasksId()) {
            if (subtasks.get(id).status.equals("DONE")) {
                summs += 2;
            } else if (subtasks.get(id).status.equals("IN_PROGRESS")) {
                summs += 1;
            }
        }
        if (summs == 0) {
            return "NEW";
        } else if (summs == 2 * epics.get(epicId).getIncomingSubtasksId().size()) {
            return "DONE";
        } else {
            return "IN_PROGRESS";
        }

    }

    String calculateStatus(int epicId) {
        if (epics.get(epicId).getIncomingSubtasksId().isEmpty()) {
            return "NEW";
        } else {
            return defineStatusEpicByAllSubtasks(epicId);
        }
    }

}
