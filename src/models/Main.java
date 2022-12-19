package models;

import manager.Managers;
import manager.TaskManager;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();
        Task task1 = new Task("Task 1", "Description 1 ");//id=1
        taskManager.createNewTask(task1);
        Task task2 = new Task("Task 2", "Description 2 ");//id=2
        taskManager.createNewTask(task2);
        Epic epic1 = new Epic("Epic 1", "Description 1 ");//id=3
        taskManager.createNewEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask 1", "Description 1 ", 3);//id=4
        taskManager.createNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2 ", 3);//id=5
        taskManager.createNewSubtask(subtask2);
        Subtask subtask3 = new Subtask("Subtask 3", "Description 3 ", 3);//id=6
        taskManager.createNewSubtask(subtask3);
        Epic epic2 = new Epic("Epic 2", "Description 2 ");//id=7
        taskManager.createNewEpic(epic2);

        System.out.println("\n" + taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());
        System.out.println(taskManager.getHistory());
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        System.out.println(taskManager.getHistory());
        taskManager.getTaskById(1);
        System.out.println(taskManager.getHistory());
        taskManager.getSubtaskById(4);
        System.out.println(taskManager.getHistory());
        taskManager.getTaskById(2);
        System.out.println(taskManager.getHistory());
        taskManager.getSubtaskById(5);
        System.out.println(taskManager.getHistory());
        taskManager.getEpicById(3);
        System.out.println(taskManager.getHistory());
        taskManager.getEpicById(7);
        System.out.println(taskManager.getHistory());
        taskManager.getEpicById(3);
        System.out.println(taskManager.getHistory());

        taskManager.deleteTaskById(2);
        System.out.println("\n" + taskManager.getHistory());
        taskManager.deleteSubtaskById(4);
        System.out.println(taskManager.getHistory());
        taskManager.deleteEpicById(3);
        System.out.println(taskManager.getHistory());
    }
}
