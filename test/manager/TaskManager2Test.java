package manager;

import manager.memory.InMemoryTaskManager;
import models.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class TaskManager2Test<T extends TaskManager> {
    protected TaskManager taskManager;
    Task task;
    Task task2;
    Task task3;
    Task task4;
    Epic epic;
    Epic epic2;
    Subtask subtask;
    Subtask subtask2;
    Subtask subtask3;

    @BeforeAll
    void beforeAll() {
        task = new Task("Task 1", "Description 1", 10, LocalDateTime.of(2023, 1, 31, 21, 20));
        task2 = new Task("Task 1", "Description 1", 10, LocalDateTime.of(2023, 1, 31, 21, 28));
        task3 = new Task("Task 3", "Description 3", 10, (LocalDateTime) null);
        task4 = new Task("Task 1", "Description 1", 10, LocalDateTime.of(2023, 1, 31, 21, 31));
        epic = new Epic("Epic 1", "Description 1");
        epic2 = new Epic("Epic 2", "Description 2");
        subtask = new Subtask("Subtask 1", "Description 1", 10, LocalDateTime.of(2023, 1, 31, 21, 20), 1);
        subtask2 = new Subtask("Subtask 2", "Description 2", 10, LocalDateTime.of(2023, 1, 31, 21, 35), 10);
        subtask3 = new Subtask("Subtask 3", "Description 3", 10, LocalDateTime.of(2023, 1, 31, 21, 35), 1);
    }

    @BeforeEach
    void beforeEach() {
        taskManager = new InMemoryTaskManager();
        taskManager.createNewTask(task);
        taskManager.createNewTask(task2);
    }

    @Test
    void createNewTask() {
        //taskManager.createNewTask(task);
        assertEquals(1, task.getId(), "1.Присвоен неверный id");
        assertTrue(taskManager.getAllTasks().contains(task), "2.Задача не была добавлена");
        assertEquals(1, taskManager.getAllTasks().size(), "3.Неверное количество задач");

        assertFalse(taskManager.getAllTasks().contains(task2), "4.Задача не будет создана, одновременно может выполняться только одна задача");
        assertEquals(TaskType.TASK, task.typeClass(), "Неверное значение класса задач");
        assertNull(task3.getStartTime(), "Неверное значение");
        assertNull(task3.getEndTime(), "Неверное значение");
    }

    @Test
    void createNewEpic() {
        taskManager.createNewEpic(epic);
        assertEquals(1, epic.getId(), "1.Присвоен неверный id");
        assertEquals(TaskStatus.NEW, epic.getStatus(), "1.Присвоен неверный статус");
        assertEquals(epic, taskManager.getEpicById(1), "Возвращает неверный эпик");
        assertEquals(1, taskManager.getAllEpics().size(), "Неверное количество эпиков");
    }

    @Test
    void createNewSubtask() {
        taskManager.createNewEpic(epic);
        taskManager.createNewTask(task);
        taskManager.createNewSubtask(subtask);
        assertEquals(0, taskManager.getAllSubtasks().size(), "Сабтаск не добавлен из-за пересечения по времени");
        taskManager.createNewSubtask(subtask2);
        assertNull(taskManager.getEpicById(subtask2.getEpicId()), "Нет такого эпика");
        taskManager.createNewSubtask(subtask3);
        assertEquals(subtask3, taskManager.getSubtaskById(3));
    }

    @Test
    void getAllTasks() {
        taskManager.createNewTask(task);
        assertEquals(1, taskManager.getAllTasks().size(), "5.Неверное количество задач");
        taskManager.createNewTask(task4);
        assertEquals(2, taskManager.getAllTasks().size(), "5.Неверное количество задач");
    }

    @Test
    void getAllEpics() {
        taskManager.createNewEpic(epic);
        assertEquals(1, taskManager.getAllEpics().size(), "Неверное количество эпиков");
        taskManager.createNewEpic(epic2);
        assertEquals(2, taskManager.getAllEpics().size(), "Неверное количество эпиков");
    }

    @Test
    void getAllSubtasks() {
        taskManager.createNewEpic(epic);
        assertEquals(0, taskManager.getAllSubtasks().size(), "Неверное количество сабтасков");
        taskManager.createNewSubtask(subtask);
        assertEquals(1, taskManager.getAllSubtasks().size(), "Неверное количество сабтасков");
        taskManager.createNewSubtask(subtask3);
        assertEquals(2, taskManager.getAllSubtasks().size(), "Неверное количество сабтасков");
    }}
/*
    @Test
    void deleteAllTasks() {
        Task task = new Task("Task 1", "Description 1", 10, LocalDateTime.of(2023, 1, 31, 21, 20));
        Task task2 = new Task("Task 1", "Description 1", 10, LocalDateTime.of(2023, 1, 31, 21, 38));
        taskManager.createNewTask(task);
        taskManager.createNewTask(task2);
        assertEquals(2, taskManager.getAllTasks().size(), "6.Неверное количество задач");
        taskManager.deleteAllTasks();
        assertEquals(0, taskManager.getAllTasks().size(), "6.Удалены не все задачи");
        assertTrue(taskManager.getAllTasks().isEmpty(), "Неверное количество задач");
    }

    @Test
    void deleteAllEpics() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createNewEpic(epic);
        Epic epic2 = new Epic("Epic 2", "Description 2");
        taskManager.createNewEpic(epic2);
        assertEquals(2, taskManager.getAllEpics().size(), "Неверное количество эпиков");
        taskManager.deleteAllEpics();
        assertEquals(0, taskManager.getAllEpics().size(), "Неверное количество эпиков");
        assertTrue(taskManager.getAllEpics().isEmpty(), "Неверное количество эпиков");

    }

    @Test
    void deleteAllSubtasks() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createNewEpic(epic);
        Subtask subtask = new Subtask("Subtask 1", "Description 1", 10, LocalDateTime.of(2023, 1, 31, 21, 20), 1);
        taskManager.createNewSubtask(subtask);
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", 5, LocalDateTime.of(2023, 1, 31, 21, 31), 1);
        taskManager.createNewSubtask(subtask);
        taskManager.deleteAllSubtasks();
        assertEquals(0, taskManager.getAllSubtasks().size(), "Неверное количество сабтасков");
    }

    @Test
    void getTaskById() {
        Task task = new Task("Task 1", "Description 1", 10, LocalDateTime.of(2023, 1, 31, 21, 20));
        Task task2 = new Task("Task 1", "Description 1", 10, LocalDateTime.of(2023, 1, 31, 21, 31));
        taskManager.createNewTask(task);
        assertEquals(task, taskManager.getTaskById(1), "6.Возвращает не ту задачу");
        taskManager.createNewTask(task2);
        assertEquals(task2, taskManager.getTaskById(2), "6.Возвращает не ту задачу");
        assertNull(taskManager.getTaskById(3), "6.Нет задачи с таким id");
    }

    @Test
    void getEpicById() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createNewEpic(epic);
        assertEquals(epic, taskManager.getEpicById(1), "Получен неверный эпик");
        assertNull(taskManager.getEpicById(10), "Невозможно вывести эпик с несуществующим id");
    }

    @Test
    void getSubtaskById() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createNewEpic(epic);
        Subtask subtask = new Subtask("Subtask 1", "Description 1", 10, LocalDateTime.of(2023, 1, 31, 21, 20), 1);
        taskManager.createNewSubtask(subtask);
        assertEquals(subtask, taskManager.getSubtaskById(2), "Неверный сабтаск");
        assertNull(taskManager.getSubtaskById(10), "Нет сабстаска с таким id");
    }

    @Test
    void updateTask() {
        Task task = new Task("Task 1", "Description 1", 10, LocalDateTime.of(2023, 1, 31, 21, 20));
        taskManager.createNewTask(task);
        Task task1_1 = new Task("Task 1_1", "Description 1_1", 1, TaskStatus.IN_PROGRESS, 10, LocalDateTime.of(2023, 1, 31, 21, 20));
        taskManager.updateTask(task1_1);
        assertNotEquals(task, taskManager.getTaskById(1), "Задачи должны быть разными");

        Task task2 = new Task("Task 2", "Description 2", 2, TaskStatus.NEW, 3, LocalDateTime.of(2023, 1, 31, 21, 15));
        Task task2_1 = new Task("Task 2_1", "Description 2_1", 2, TaskStatus.IN_PROGRESS, 10, LocalDateTime.of(2023, 1, 31, 21, 15));
        taskManager.createNewTask(task2);
        taskManager.updateTask(task2_1);
        assertNotEquals(task2, task2_1, "Обновление задачи не должно было произойти");

        Task task3 = new Task("Task 3", "Description 3", 3, TaskStatus.IN_PROGRESS, 10, LocalDateTime.of(2023, 1, 31, 21, 45));
        taskManager.updateTask(task3);
        assertNull(taskManager.getTaskById(3), "Нет задачи с таким id");
    }

    @Test
    void updateEpic() {
        Epic epic = new Epic("Epic 1", "Description 1");
        Epic epic1_1 = new Epic("Epic 1_1", "Description 1_1", 1, TaskStatus.IN_PROGRESS, 10, LocalDateTime.of(2023, 1, 31, 21, 15));
        taskManager.createNewEpic(epic);
        taskManager.updateEpic(epic1_1);
        assertNotEquals(epic1_1, taskManager.getEpicById(1), "Задачи должны быть разными");
        Epic epic2 = new Epic("Epic 2", "Description 2", 2, TaskStatus.NEW, 10, LocalDateTime.of(2023, 1, 31, 21, 35));
        taskManager.updateEpic(epic2);
        assertNull(taskManager.getTaskById(2), "Нет эпика с таким id");
    }

    @Test
    void updateSubtask() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createNewEpic(epic);
        Subtask subtask = new Subtask("Subtask 1", "Description 1", 5, LocalDateTime.of(2023, 1, 31, 21, 30), 1);
        taskManager.createNewSubtask(subtask);
        Subtask subtask1_1 = new Subtask("Subtask 1_1", "Description 1_1", 2, TaskStatus.IN_PROGRESS, 5, LocalDateTime.of(2023, 1, 31, 21, 30), 1);
        taskManager.updateSubtask(subtask1_1);
        assertEquals(subtask1_1, taskManager.getSubtaskById(2), "Сабтаск не обновлен");

        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", 10, TaskStatus.IN_PROGRESS, 5, LocalDateTime.of(2023, 1, 31, 21, 45), 10);
        taskManager.updateSubtask(subtask2);
        assertNull(taskManager.getSubtaskById(subtask2.getEpicId()), "Нет такого сабтаска");

        Subtask subtask3 = new Subtask("Subtask 3", "Description 3", 2, TaskStatus.IN_PROGRESS, 10, LocalDateTime.of(2023, 1, 31, 21, 33), 1);
        taskManager.updateSubtask(subtask3);
        assertNotEquals(subtask3, taskManager.getSubtaskById(2), "Обновление не произойдет из-за пересечения по времени");
    }

    @Test
    void deleteTaskById() {
        Task task = new Task("Task 1", "Description 1", 10, LocalDateTime.of(2023, 1, 31, 21, 20));
        taskManager.createNewTask(task);
        taskManager.deleteTaskById(task.getId());
        assertEquals(0, taskManager.getAllTasks().size(), "Задача не удалена");
        taskManager.createNewTask(task);
        taskManager.deleteTaskById(10);
        assertEquals(1, taskManager.getAllTasks().size(), "Нет задачи с таким id");
        assertNull(taskManager.getTaskById(10), "Нет задачи с таким id");
    }

    @Test
    void deleteSubtaskById() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createNewEpic(epic);
        Subtask subtask = new Subtask("Subtask 1", "Description 1", 5, LocalDateTime.of(2023, 1, 31, 21, 30), 1);
        taskManager.createNewSubtask(subtask);
        taskManager.deleteSubtaskById(10);
        assertEquals(1, taskManager.getAllSubtasks().size(), "Сабтаск c неверным id");
        taskManager.deleteSubtaskById(subtask.getId());
        assertTrue(taskManager.getAllSubtasks().isEmpty(), "Сабтаск не удален");
    }

    @Test
    void deleteSubtaskByIdEpic() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createNewEpic(epic);
        Subtask subtask = new Subtask("Subtask 1", "Description 1", 5, LocalDateTime.of(2023, 1, 31, 21, 30), 1);
        taskManager.createNewSubtask(subtask);
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", 5, LocalDateTime.of(2023, 1, 31, 21, 40), 1);
        taskManager.createNewSubtask(subtask);
        taskManager.deleteSubtaskByIdEpic(1);
        assertTrue(taskManager.getAllSubtasks().isEmpty(), "Список сабтасков должен быть пуст");
        taskManager.deleteSubtaskByIdEpic(10);
        assertNull(taskManager.getEpicById(10), "Эпика с таким id нет");
    }

    @Test
    void deleteEpicById() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createNewEpic(epic);
        assertEquals(1, taskManager.getAllEpics().size(), "Неверное количество эпиков");
        taskManager.deleteEpicById(1);
        assertEquals(0, taskManager.getAllEpics().size(), "Неверное количество эпиков");
        taskManager.createNewEpic(epic);
        taskManager.deleteEpicById(10);
        assertEquals(1, taskManager.getAllEpics().size(), "Нет эпика с таким id");
    }

    @Test
    void getAllEpicSubtasks() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createNewEpic(epic);
        assertEquals(0, taskManager.getAllEpicSubtasks(1).size(), "Неверное количество сабтасков");
        Subtask subtask = new Subtask("Subtask 1", "Description 1", 5, LocalDateTime.of(2023, 1, 31, 21, 30), 1);
        taskManager.createNewSubtask(subtask);
        assertEquals(1, taskManager.getAllEpicSubtasks(1).size(), "Неверное количество сабтасков");
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", 5, LocalDateTime.of(2023, 1, 31, 21, 40), 1);
        taskManager.createNewSubtask(subtask);
        assertEquals(2, taskManager.getAllEpicSubtasks(1).size(), "Неверное количество сабтасков");

        assertNull(taskManager.getAllEpicSubtasks(10), "Эпика с таким id нет");

    }

    @Test
    void getHistory() {
        Task task = new Task("Task 1", "Description 1", 8, LocalDateTime.of(2023, 1, 31, 21, 20));
        taskManager.createNewTask(task);//id=1
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createNewEpic(epic);//id=2
        Subtask subtask = new Subtask("Subtask 1", "Description 1", 5, LocalDateTime.of(2023, 1, 31, 21, 30), 2);
        taskManager.createNewSubtask(subtask);//id=3
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", 5, LocalDateTime.of(2023, 1, 31, 21, 40), 2);
        taskManager.createNewSubtask(subtask2);//id=4
        assertTrue(taskManager.getHistory().isEmpty(), "История должны быть пуста");
        taskManager.getTaskById(1);
        taskManager.getEpicById(2);
        assertEquals(2, taskManager.getHistory().size(), "История отображается неверно");
        taskManager.getSubtaskById(3);
        taskManager.getSubtaskById(4);
        assertEquals(4, taskManager.getHistory().size(), "История отображается неверно");
    }

    @Test
    void getPrioritizedTasks() {
        Task task = new Task("Task 1", "Description 1", 8, LocalDateTime.of(2023, 1, 31, 21, 20));
        taskManager.createNewTask(task);//id=1
        assertEquals(1, taskManager.getPrioritizedTasks().size(), "Неверное количество задач");
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createNewEpic(epic);//id=2
        assertEquals(1, taskManager.getPrioritizedTasks().size(), "Неверное количество задач");

        Subtask subtask = new Subtask("Subtask 1", "Description 1", 5, LocalDateTime.of(2023, 1, 31, 21, 30), 2);
        taskManager.createNewSubtask(subtask);//id=3
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", 5, LocalDateTime.of(2023, 1, 31, 21, 40), 2);
        taskManager.createNewSubtask(subtask2);//id=4
        assertEquals(3, taskManager.getPrioritizedTasks().size(), "Неверное количество задач");
    }*/


