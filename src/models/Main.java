package models;

import manager.Manager;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();

        Task task1 = new Task("Task 1", "Description 1 ");
        manager.createNewTask(task1);
        Task task2 = new Task("Task 2", "Description 2 ");
        manager.createNewTask(task2);
        Epic epic1 = new Epic("Epic 1", "Description 1 ");
        manager.createNewEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask 1", "Description 1 ", 3);
        manager.createNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2 ", 3);
        manager.createNewSubtask(subtask2);
        Epic epic2 = new Epic("Epic 2", "Description 2 ");
        manager.createNewEpic(epic2);
        Subtask subtask3 = new Subtask("Subtask 3", "Description 3 ", 6);
        manager.createNewSubtask(subtask3);
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubtasks());

        task1.setName("Task 1_1");
        task1.setDescription("Description 1_1");
        task1.setStatus("IN_PROGRESS");
        manager.updateTask(task1);
        task2.setStatus("DONE");
        manager.updateTask(task2);
        subtask1.setStatus("IN_PROGRESS");
        manager.updateSubtask(subtask1);
        subtask2.setStatus("DONE");
        manager.updateSubtask(subtask2);
        subtask3.setStatus("IN_PROGRESS");
        manager.updateSubtask(subtask3);
        System.out.println("\n" + manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubtasks());

        manager.deleteTaskById(1);
        manager.deleteEpicById(3);
        System.out.println("\n" + manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubtasks());
    }
}
