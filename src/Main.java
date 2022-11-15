public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();

        Task task1 = new Task("Task 1", "Description 1 ");
        manager.createNewTask(task1);
        Task task2 = new Task("Task 2", "Description 2 ");
        manager.createNewTask(task2);
        Epic epic1 = new Epic("Epic 1", "Description 1 ");
        manager.createNewEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask 1", "Description 1 ", "NEW", 1);
        manager.createNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2 ", "NEW", 1);
        manager.createNewSubtask(subtask2);
        Epic epic2 = new Epic("Epic 2", "Description 2 ");
        manager.createNewEpic(epic2);
        Subtask subtask3 = new Subtask("Subtask 3", "Description 3 ", "NEW", 2);
        manager.createNewSubtask(subtask3);
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubtasks());

        Task task1_1 = new Task("Task 1", "Description 1 ", 1, "IN_PROGRESS");
        manager.updateTask(task1_1);
        Task task2_1 = new Task("Task 2", "Description 2 ", 2, "DONE");
        manager.updateTask(task2_1);
        Subtask subtask1_1 = new Subtask("Subtask 1", "Description 1 ", 1, "IN_PROGRESS", 1);
        manager.updateSubtask(subtask1_1);
        Subtask subtask2_1 = new Subtask("Subtask 2", "Description 2 ", 2, "DONE", 1);
        manager.updateSubtask(subtask2_1);
        Subtask subtask3_1 = new Subtask("Subtask 3", "Description 3 ", 3, "IN_PROGRESS", 2);
        manager.updateSubtask(subtask3_1);
        System.out.println("\n" + manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubtasks());

        manager.deleteTaskById(1);
        manager.deleteEpicById(2);
        System.out.println("\n" + manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubtasks());
    }
}
