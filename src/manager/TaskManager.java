package manager;

import models.Epic;
import models.Subtask;
import models.Task;
import models.TaskStatus;

import java.util.ArrayList;
import java.util.List;


public interface TaskManager {

    public int generateId();

    public void createNewTask(Task task);

    public void createNewEpic(Epic epic);

    public void createNewSubtask(Subtask subtask);

    public ArrayList<Task> getAllTasks();

    public ArrayList<Epic> getAllEpics();

    public ArrayList<Subtask> getAllSubtasks();

    public void deleteAllTasks();

    public void deleteAllEpics();

    public void deleteAllSubtasks();

    public Task getTaskById(int id);

    public Epic getEpicById(int id);

    public Subtask getSubtaskById(int id);

    public void updateTask(Task task);

    public void updateEpic(Epic epic);

    public void updateSubtask(Subtask subtask);

    public void deleteTaskById(int id);

    public void deleteSubtaskById(int id);

    public void deleteSubtaskByIdEpic(int epicId);

    public void deleteEpicById(int id);

    public ArrayList<Subtask> getAllEpicSubtasks(int id);


    public TaskStatus calculateStatus(int epicId);

    public List<Task> getHistory();

}

