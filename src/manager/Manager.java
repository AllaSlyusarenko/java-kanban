package manager;

import models.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();
    public static int globalId = 1;

    public int generateId() {
        return globalId++;
    }

    public void createNewTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
    }

    public void createNewEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
    }

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

    public void getAllTasks() {
        System.out.println(tasks);
    }

    public void getAllEpics() {
        System.out.println(epics);
    }

    public void getAllSubtasks() {
        System.out.println(subtasks);
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        deleteAllSubtasks();
        epics.clear();
    }

    public void deleteAllSubtasks() {
        for (int epicID : epics.keySet()) {
            epics.get(epicID).setIncomingSubtasksId(new ArrayList<>());
            epics.get(epicID).setStatus(calculateStatus(epicID));
        }
        subtasks.clear();
    }

    public void getTaskById(int id) {
        if (tasks.containsKey(id)) {
            System.out.println(tasks.get(id));
        } else {
            System.out.println("Проверьте корректность вводимых данных"
                    + " : на момент ввода нет таска с номером " + id + "\n");
        }
    }

    public void getEpicById(int id) {
        if (epics.containsKey(id)) {
            System.out.println(epics.get(id));
        } else {
            System.out.println("Проверьте корректность вводимых данных"
                    + " : на момент ввода нет эпика с номером " + id + "\n");
        }
    }

    public void getSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            System.out.println(subtasks.get(id));
        } else {
            System.out.println("Проверьте корректность вводимых данных"
                    + " : на момент ввода нет сабтаска с номером " + id + "\n");
        }
    }

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Проверьте корректность вводимых данных" + task.getName()
                    + " : на момент ввода нет таска с номером " + task.getId() + "\n");
        }
    }

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

    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            epics.get(subtask.getEpicId()).setStatus(calculateStatus(subtask.getEpicId()));
        } else {
            System.out.println("Проверьте корректность вводимых данных" + subtask.getName()
                    + " : на момент ввода нет сабтаска с номером " + subtask.getId() + "\n");
        }

    }

    public void deleteTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else {
            System.out.println("Проверьте корректность вводимых данных"
                    + " : на момент ввода нет таска с номером " + id + "\n");
        }
    }

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

    public void deleteEpicById(int id) {
        if (epics.containsKey(id)) {
            deleteSubtaskByIdEpic(id);
            epics.remove(id);
        } else {
            System.out.println("Проверьте корректность вводимых данных"
                    + " : на момент ввода нет эпика с номером " + id + "\n");
        }
    }

    public void getAllEpicSubtasks(int id) {
        if (epics.containsKey(id)) {
            ArrayList<Subtask> epicSubtasks = new ArrayList<>();
            for (Integer idSubtask : epics.get(id).getIncomingSubtasksId()) {
                epicSubtasks.add(subtasks.get(idSubtask));
            }
            System.out.println(epicSubtasks);
        } else {
            System.out.println("Проверьте корректность вводимых данных"
                    + " : на момент ввода нет эпика с номером " + id + "\n");
        }


    }

    public String defineStatusEpicByAllSubtasks(int epicId) {
        int summs = 0;
        for (int id : epics.get(epicId).getIncomingSubtasksId()) {
            if (subtasks.get(id).getStatus().equals("DONE")) {
                summs += 2;
            } else if (subtasks.get(id).getStatus().equals("IN_PROGRESS")) {
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

    public String calculateStatus(int epicId) {
        if (epics.get(epicId).getIncomingSubtasksId().isEmpty()) {
            return "NEW";
        } else {
            return defineStatusEpicByAllSubtasks(epicId);
        }
    }

}
