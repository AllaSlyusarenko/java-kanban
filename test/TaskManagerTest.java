import manager.TaskManager;
import manager.memory.InMemoryTaskManager;
import models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public abstract class TaskManagerTest<T extends TaskManager> {
    protected TaskManager taskManager;

    @BeforeEach
    void beforeEach() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void createNewTask() {
        Task task = new Task("Task 1", "Description 1", 10, LocalDateTime.of(2023, 1, 31, 21, 20));
        Task task2 = new Task("Task 1", "Description 1", 10, LocalDateTime.of(2023, 1, 31, 21, 28));
        taskManager.createNewTask(task);
        assertEquals(1, task.getId(), "1.Присвоен неверный id");
        assertTrue(taskManager.getAllTasks().contains(task), "2.Задача не была добавлена");
        assertEquals(1, taskManager.getAllTasks().size(), "3.Неверное количество задач");
        taskManager.createNewTask(task2);
        assertFalse(taskManager.getAllTasks().contains(task2), "4.Задача не будет создана, одновременно может выполняться только одна задача");
        assertEquals(TaskType.TASK, task.typeClass(), "Неверное значение класса задач");
        Task task3 = new Task("Task 3", "Description 3", 10, (LocalDateTime) null);
        assertNull(task3.getStartTime(), "Неверное значение");
        assertNull(task3.getEndTime(), "Неверное значение");
    }

    @Test
    void createNewEpic() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createNewEpic(epic);
        assertEquals(1, epic.getId(), "1.Присвоен неверный id");
        assertEquals(TaskStatus.NEW, epic.getStatus(), "1.Присвоен неверный статус");
        assertEquals(epic, taskManager.getEpicById(1), "Возвращает неверный эпик");
        assertEquals(1, taskManager.getAllEpics().size(), "Неверное количество эпиков");
    }

    @Test
    void createNewSubtask() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createNewEpic(epic);
        Task task = new Task("Task 1", "Description 1", 10, LocalDateTime.of(2023, 1, 31, 21, 20));
        taskManager.createNewTask(task);
        Subtask subtask = new Subtask("Subtask 1", "Description 1", 10, LocalDateTime.of(2023, 1, 31, 21, 20), 1);
        taskManager.createNewSubtask(subtask);
        assertEquals(0, taskManager.getAllSubtasks().size(), "Сабтаск не добавлен из-за пересечения по времени");

        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", 10, LocalDateTime.of(2023, 1, 31, 21, 35), 10);
        taskManager.createNewSubtask(subtask2);
        assertNull(taskManager.getEpicById(subtask2.getEpicId()), "Нет такого эпика");

        Subtask subtask3 = new Subtask("Subtask 3", "Description 3", 10, LocalDateTime.of(2023, 1, 31, 21, 35), 1);
        taskManager.createNewSubtask(subtask3);
        assertEquals(subtask3, taskManager.getSubtaskById(3));
    }

    @Test
    void getAllTasks() {
        Task task = new Task("Task 1", "Description 1", 10, LocalDateTime.of(2023, 1, 31, 21, 20));
        Task task2 = new Task("Task 1", "Description 1", 10, LocalDateTime.of(2023, 1, 31, 21, 31));
        taskManager.createNewTask(task);
        assertEquals(1, taskManager.getAllTasks().size(), "5.Неверное количество задач");
        taskManager.createNewTask(task2);
        assertEquals(2, taskManager.getAllTasks().size(), "5.Неверное количество задач");
    }

    @Test
    void getAllEpics() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createNewEpic(epic);
        assertEquals(1, taskManager.getAllEpics().size(), "Неверное количество эпиков");
        Epic epic2 = new Epic("Epic 2", "Description 2");
        taskManager.createNewEpic(epic2);
        assertEquals(2, taskManager.getAllEpics().size(), "Неверное количество эпиков");
    }

    @Test
    void getAllSubtasks() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createNewEpic(epic);
        assertEquals(0, taskManager.getAllSubtasks().size(), "Неверное количество сабтасков");
        Subtask subtask = new Subtask("Subtask 1", "Description 1", 10, LocalDateTime.of(2023, 1, 31, 21, 20), 1);
        taskManager.createNewSubtask(subtask);
        assertEquals(1, taskManager.getAllSubtasks().size(), "Неверное количество сабтасков");
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", 5, LocalDateTime.of(2023, 1, 31, 21, 31), 1);
        taskManager.createNewSubtask(subtask);
        assertEquals(2, taskManager.getAllSubtasks().size(), "Неверное количество сабтасков");
    }

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
        Task task1_1 = new Task("Task 1_1", "Description 1_1", 1, TaskStatus.IN_PROGRESS, 10, LocalDateTime.of(2023, 1, 31, 21, 20));
        taskManager.createNewTask(task);
        taskManager.updateTask(task1_1);
        assertEquals(task1_1, taskManager.getTaskById(1), "Задачи должны быть разными");

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
        assertEquals(1, taskManager.getAllSubtasks().size(),"Сабтаск c неверным id");
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
        assertNull(taskManager.getEpicById(10),"Эпика с таким id нет");
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
    }

    @Test
    void getHistory() {
    }

    @Test
    void getPrioritizedTasks() {
    }


}

