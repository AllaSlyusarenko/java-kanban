package models;

import manager.memory.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;

import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();
        Task task1 = new Task("Task 1", "Description 1 ", 5, LocalDateTime.of(2022, 3, 8, 15, 30));//id=1
        taskManager.createNewTask(task1);
        Task task2 = new Task("Task 2", "Description 2 ", 5, (LocalDateTime) null);//id=2
        taskManager.createNewTask(task2);
        Epic epic1 = new Epic("Epic 1", "Description 1 ");//id=3
        taskManager.createNewEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask 1", "Description 1 ", 6, LocalDateTime.of(2022, 3, 8, 15, 38), 3);//id=4
        taskManager.createNewSubtask(subtask1);

        Subtask subtask2 = new Subtask("Subtask 2", "Description 2 ", 10, (LocalDateTime) null, 3);//id=5
        taskManager.createNewSubtask(subtask2);
        System.out.println("\n" + taskManager.getEpicById(3));
        Subtask subtask3 = new Subtask("Subtask 3", "Description 3 ", 10, LocalDateTime.of(2022, 3, 8, 15, 45), 3);//id=6
        taskManager.createNewSubtask(subtask3);
        Subtask subtask3_1 = new Subtask("Subtask 3_1", "Description 3 ",6,TaskStatus.IN_PROGRESS, 10, LocalDateTime.of(2022, 3, 8, 15, 45), 3);//id=6
        taskManager.updateSubtask(subtask3_1);



        //System.out.println(taskManager.getAllEpicSubtasks(3));
        /*System.out.println(taskManager.getHistory());
        System.out.println("\n" + taskManager.getEpicById(3));
        System.out.println("\n" + taskManager.getTaskById(1));
        System.out.println(taskManager.getHistory());
        System.out.println("\n" + taskManager.getEpicById(3));
        System.out.println("\n" + taskManager.getTaskById(1));
        System.out.println(taskManager.getHistory());*/

        Task task1_1 = new Task("Task 1_1", "Description 1 ", 1, TaskStatus.IN_PROGRESS, 10, LocalDateTime.of(2022, 3, 8, 15, 10));//id=1
        taskManager.updateTask(task1_1);
        //System.out.println(taskManager.getTaskById(1));
        /*Epic epic2 = new Epic("Epic 2", "Description 2 ");//id=7
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
        //taskManager.deleteEpicById(3);
        System.out.println(taskManager.getHistory());
        Subtask subtask4 = new Subtask("Subtask 4", "Description 4 ", 7,TaskStatus.NEW, 15, LocalDateTime.now(),3);//id=7
        taskManager.createNewSubtask(subtask4);
        taskManager.getSubtaskById(7).getEndTime();*/

        //System.out.println("\n" + taskManager.getEpicById(3));
        //InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) taskManager;
        //System.out.println(inMemoryTaskManager.getPrioritizedTasks());
    }
}
