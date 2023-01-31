import manager.Managers;
import manager.TaskManager;
import models.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected TaskManager taskManager = Managers.getDefault();

    @Test
    void TaskTest() {
        /*
        Для каждого метода нужно проверить его работу:
        a. Со стандартным поведением.
                b. С пустым списком задач.
                c. С неверным идентификатором задачи (пустой и/или несуществующий идентификатор).*/
        Task task = new Task("Task 1", "Description 1", 10, LocalDateTime.of(2023, 1, 31, 21, 20));
        taskManager.createNewTask(task);
        assertEquals(1, taskManager.getAllTasks().size(), "1.Метод не добавил Task");
        assertEquals(task, taskManager.getTaskById(1), "2.Задачи не совпадают");
        assertEquals(1, task.getId(), "3.Неверный id задачи");
        assertEquals("Task 1", task.getName(), "4.Неверное имя задачи");
        assertEquals("Description 1", task.getDescription(), "5.Неверное описание задачи");
        assertEquals(task.getStatus(), TaskStatus.NEW, "6.Неверный статус при создании задачи");
        assertEquals(10, task.getDuration(), "7.Неверное значение продолжительности задачи");
        assertEquals(TaskType.TASK, task.typeClass(), "8.Неверный тип задач");
        assertEquals(LocalDateTime.of(2023, 1, 31, 21, 30), task.getEndTime(), "9.Неверное время окончания задачи");

        Task task1_1 = new Task("Task 1_1", "Description 1_1", 1, TaskStatus.IN_PROGRESS, 10, LocalDateTime.of(2023, 1, 31, 21, 20));
        taskManager.updateTask(task1_1);
        assertNotEquals(taskManager.getTaskById(1), task, "10.Задачи не должны совпадать");

        Task task2 = new Task("Task 2", "Description 2", 5, (LocalDateTime) null);
        taskManager.createNewTask(task2);
        assertNull(task2.getStartTime());
        assertNull(task2.getEndTime());
        assertEquals(2, taskManager.getAllTasks().size(), "11.Неверное количество задач после добавления задачи");


        Task task3 = new Task("Task 2_1", "Description 2", 2, TaskStatus.IN_PROGRESS, 8, LocalDateTime.of(2023, 1, 31, 21, 28));
        taskManager.createNewTask(task3);
        assertEquals(2, taskManager.getAllTasks().size(), "12.Задача не будет создана, одновременно может выполняться только одна задача");
        assertNull(taskManager.getTaskById(3), "13.Нет задачи с таким Id ");
        taskManager.updateTask(task3);
        assertEquals(taskManager.getTaskById(2), task2, "14.Задача не будет обновлена, одновременно может выполняться только одна задача");


        taskManager.deleteTaskById(1);
        assertEquals(1, taskManager.getAllTasks().size(), "15.Неверное количество задач после удаления задачи");
        taskManager.deleteTaskById(5);
        assertEquals(1, taskManager.getAllTasks().size(), "16.Неверное количество задач после удаления задачи");
        taskManager.deleteAllTasks();
        assertEquals(0, taskManager.getAllTasks().size(), "17.Неверное количество задач после удаления задачи");
    }

    @Test
    void EpicTest() {
       /*
        Для подзадач нужно дополнительно проверить наличие эпика, а для эпика — расчёт статуса.
        Потребуются следующие тесты.
                Для расчёта статуса Epic. Граничные условия:
        a.   Пустой список подзадач.
                b.   Все подзадачи со статусом NEW.
        c.    Все подзадачи со статусом DONE.
                d.    Подзадачи со статусами NEW и DONE.
                e.    Подзадачи со статусом IN_PROGRESS.*/
        /*epic
        void createNewEpic(Epic epic);

        ArrayList<Epic> getAllEpics();

        void deleteAllEpics();

        Epic getEpicById(int id);
        void updateEpic(Epic epic);
        void deleteEpicById(int id);*/

    }

    @Test
    void SubtaskTest() {
        //subtask Для подзадач нужно дополнительно проверить наличие эпика, а для эпика — расчёт статуса.
        /* void createNewSubtask(Subtask subtask);

        ArrayList<Subtask> getAllSubtasks();

        void deleteAllSubtasks();

        Subtask getSubtaskById(int id);
        void updateSubtask(Subtask subtask);
        void deleteSubtaskById(int id);
        void deleteSubtaskByIdEpic(int epicId);
        ArrayList<Subtask> getAllEpicSubtasks(int id);


        List<Task> getHistory();*/

    }

}

