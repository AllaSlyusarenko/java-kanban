package manager;

import models.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected TaskManager taskManager;
    protected Task task;
    protected Task task2;
    protected Task task1_1;
    protected Task task2_1;
    protected Epic epic;
    protected Epic epic2;
    protected Epic epic1_1;
    protected Epic epic2_1;
    protected Subtask subtask;
    protected Subtask subtask2;
    protected Subtask subtask1_1;
    protected Subtask subtask2_1;

    protected void initTasks() {
        task = new Task("Task 1", "Description 1", 5, LocalDateTime.of(2023, 1, 31, 21, 20));
        task2 = new Task("Task 2", "Description 2", 5, LocalDateTime.of(2023, 1, 31, 21, 28));
        task1_1 = new Task("Task 1_1", "Description 1_1", 1, TaskStatus.IN_PROGRESS, 10, LocalDateTime.of(2023, 1, 31, 21, 20));
        task2_1 = new Task("Task 2_1", "Description 2_1", 2, TaskStatus.DONE, 5, LocalDateTime.of(2023, 1, 31, 21, 28));
        epic = new Epic("Epic 1", "Description 1");
        epic2 = new Epic("Epic 1", "Description 1");
        epic1_1 = new Epic("Epic 1_1", "Description 1_1", 1, TaskStatus.NEW, 0, (LocalDateTime) null);
        epic2_1 = new Epic("Epic 2_1", "Description 2_1", 2, TaskStatus.NEW, 0, (LocalDateTime) null);
        subtask = new Subtask("Subtask 1", "Description 1", 5, LocalDateTime.of(2023, 1, 31, 21, 50), 1);
        subtask2 = new Subtask("Subtask 2", "Description 2", 5, LocalDateTime.of(2023, 1, 31, 21, 40), 1);
        subtask1_1 = new Subtask("Subtask 1_1", "Description 1_1", 2, TaskStatus.IN_PROGRESS, 5, LocalDateTime.of(2023, 1, 31, 21, 30), 1);
        subtask2_1 = new Subtask("Subtask 2_1", "Description 2_1", 3, TaskStatus.DONE, 5, LocalDateTime.of(2023, 1, 31, 21, 40), 1);
    }

    @Test
    void getAllTasks() {
        taskManager.createNewTask(task);
        assertEquals(1, taskManager.getAllTasks().size(), "Неверное количество задач");
        taskManager.createNewTask(task2);
        assertEquals(2, taskManager.getAllTasks().size(), "Неверное количество задач");
    }

    @Test
    void getAllTasksWithEmptyTasks() {
        assertEquals(new ArrayList(), taskManager.getAllTasks(), "Неверное количество задач");
    }

    @Test
    void createNewTaskWithEmptyTasks() {
        taskManager.createNewTask(task);
        assertTrue(taskManager.getAllTasks().contains(task), "Задача не была добавлена");
        assertEquals(1, taskManager.getAllTasks().size(), "Неверное количество задач");
    }

    @Test
    void createNewTask() {
        taskManager.createNewTask(task);
        taskManager.createNewTask(task2);
        assertTrue(taskManager.getAllTasks().contains(task2), "Задача не была добавлена");
        assertEquals(2, taskManager.getAllTasks().size(), "Неверное количество задач");
    }

    @Test
    void getTaskTypeTask() {
        taskManager.createNewTask(task);
        assertEquals(TaskType.TASK, task.typeClass(), "Неверное значение класса задач");
    }

    @Test
    void deleteAllTasks() {
        taskManager.createNewTask(task);
        taskManager.createNewTask(task2);
        taskManager.deleteAllTasks();
        assertEquals(0, taskManager.getAllTasks().size(), "Удалены не все задачи");
        assertEquals(new ArrayList<>(), taskManager.getAllTasks(), "Должен быть возвращен пустой список");
    }

    void deleteAllTasksWithEmptyTasks() {
        taskManager.deleteAllTasks();
        assertEquals(0, taskManager.getAllTasks().size(), "Удалены не все задачи");
        assertTrue(taskManager.getAllTasks().isEmpty(), "Неверное количество задач");
    }

    @Test
    void getTaskById() {
        taskManager.createNewTask(task);
        assertEquals(task, taskManager.getTaskById(1), "Возвращает не ту задачу");
        taskManager.createNewTask(task2);
        assertEquals(task2, taskManager.getTaskById(task2.getId()), "Возвращает не ту задачу");
    }

    @Test
    void getTaskByWrongId() {
        taskManager.createNewTask(task);
        taskManager.createNewTask(task2);
        assertNull(taskManager.getTaskById(3), "Нет задачи с таким id");
    }

    @Test
    void getTaskByIdWithEmptyTasks() {
        assertNull(taskManager.getTaskById(1), "Задачи не существует");
    }

    @Test
    void updateTask() {
        taskManager.createNewTask(task);
        taskManager.updateTask(task1_1);
        assertNotEquals(task, taskManager.getTaskById(1), "Задачи должны быть разными");
    }

    @Test
    void updateTaskWithEmptyTasks() {
        taskManager.updateTask(task1_1);
        assertEquals(0, taskManager.getAllTasks().size(), "Обновление задачи не должно было произойти");
        assertEquals(new ArrayList<>(), taskManager.getAllTasks(), "Должен быть возвращен пустой список");
    }

    @Test
    void updateTaskWithWrongId() {
        taskManager.createNewTask(task);
        taskManager.updateTask(task2_1);
        assertEquals(task, taskManager.getTaskById(1), "Обновление задачи не должно было произойти");
    }

    @Test
    void deleteTaskById() {
        taskManager.createNewTask(task);
        taskManager.deleteTaskById(1);
        assertEquals(0, taskManager.getAllTasks().size(), "Задача не удалена");
    }

    @Test
    void deleteTaskByWrongId() {
        taskManager.createNewTask(task);
        taskManager.deleteTaskById(10);
        assertNull(taskManager.getTaskById(10), "Нет задачи с таким id");
    }

    @Test
    void deleteTaskByIdWithEmptyTasks() {
        taskManager.deleteTaskById(10);
        assertNull(taskManager.getTaskById(10), "Нет задачи с таким id");
    }

    @Test
    void createNewEpicWithEmptyEpics() {
        taskManager.createNewEpic(epic);
        assertEquals(TaskStatus.NEW, epic.getStatus(), "Присвоен неверный статус");
        assertEquals(epic, taskManager.getEpicById(1), "Возвращает неверный эпик");
        assertEquals(1, taskManager.getAllEpics().size(), "Неверное количество эпиков");
    }

    @Test
    void createNewEpic() {
        taskManager.createNewEpic(epic);
        taskManager.createNewEpic(epic2);
        assertEquals(epic2, taskManager.getEpicById(2), "Возвращает неверный эпик");
        assertEquals(2, taskManager.getAllEpics().size(), "Неверное количество эпиков");
    }

    @Test
    void getTaskTypeEpic() {
        taskManager.createNewEpic(epic);
        assertEquals(TaskType.EPIC, epic.typeClass(), "Неверное значение класса задач");
    }

    @Test
    void getAllEpics() {
        taskManager.createNewEpic(epic);
        assertEquals(1, taskManager.getAllEpics().size(), "Неверное количество эпиков");
        taskManager.createNewEpic(epic2);
        assertEquals(2, taskManager.getAllEpics().size(), "Неверное количество эпиков");
    }

    @Test
    void getAllEpicsWithEmptyEpics() {
        assertEquals(new ArrayList(), taskManager.getAllEpics(), "Неверное количество эпиков");
    }

    @Test
    void deleteAllEpicsWithEmptyEpics() {
        taskManager.deleteAllEpics();
        assertEquals(0, taskManager.getAllEpics().size(), "Неверное количество эпиков");
        assertTrue(taskManager.getAllEpics().isEmpty(), "Неверное количество эпиков");
    }

    @Test
    void deleteAllEpics() {
        taskManager.createNewEpic(epic);
        taskManager.createNewEpic(epic2);
        taskManager.deleteAllEpics();
        assertEquals(0, taskManager.getAllEpics().size(), "Неверное количество эпиков");
        assertTrue(taskManager.getAllEpics().isEmpty(), "Неверное количество эпиков");
    }

    @Test
    void getEpicById() {
        taskManager.createNewEpic(epic);
        assertEquals(epic, taskManager.getEpicById(1), "Получен неверный эпик");
    }

    @Test
    void getEpicByIdWithEmptyEpics() {
        assertNull(taskManager.getEpicById(1), "Невозможно вывести эпик из пустого списка эпиков");
    }

    @Test
    void getEpicByWrongId() {
        taskManager.createNewEpic(epic);
        assertNull(taskManager.getEpicById(2), "Невозможно вывести эпик с неверным id");
    }

    @Test
    void updateEpic() {
        taskManager.createNewEpic(epic);
        taskManager.updateEpic(epic1_1);
        assertEquals(epic1_1, taskManager.getEpicById(1), "Задачи должны быть разными");
        Epic epic2 = new Epic("Epic 2", "Description 2", 2, TaskStatus.NEW, 10, LocalDateTime.of(2023, 1, 31, 21, 35));
        taskManager.updateEpic(epic2);
        assertNull(taskManager.getTaskById(epic2.getId()), "Нет эпика с таким id");
    }

    @Test
    void updateEpicWithWrongId() {
        taskManager.createNewEpic(epic);
        taskManager.updateEpic(epic2_1);
        assertNull(taskManager.getTaskById(2), "Нет эпика с таким id");
    }

    @Test
    void updateEpicWithEmptyEpics() {
        taskManager.updateEpic(epic2_1);
        assertNull(taskManager.getTaskById(2), "Нет эпика с таким id");
    }

    @Test
    void deleteEpicById() {
        taskManager.createNewEpic(epic);
        assertEquals(1, taskManager.getAllEpics().size(), "Неверное количество эпиков");
        taskManager.deleteEpicById(1);
        assertEquals(0, taskManager.getAllEpics().size(), "Неверное количество эпиков");
    }

    @Test
    void deleteEpicByWrongId() {
        taskManager.createNewEpic(epic);
        assertEquals(1, taskManager.getAllEpics().size(), "Неверное количество эпиков");
        taskManager.deleteEpicById(10);
        assertEquals(1, taskManager.getAllEpics().size(), "Нет эпика с таким id");
    }

    @Test
    void getNewEpicStatus() {
        taskManager.createNewEpic(epic);
        assertEquals(TaskStatus.NEW, epic.getStatus(), "Присвоен неверный статус");
    }

    @Test
    void getNewEpicStatusAllNew() {
        taskManager.createNewEpic(epic);
        taskManager.createNewSubtask(subtask);
        taskManager.createNewSubtask(subtask2);
        assertEquals(TaskStatus.NEW, epic.getStatus(), "Присвоен неверный статус");
    }

    @Test
    void getNewEpicStatusNewDone() {
        taskManager.createNewEpic(epic);
        taskManager.createNewSubtask(subtask);
        Subtask subtask3 = new Subtask("Subtask 3", "Description 3", 3, TaskStatus.DONE, 5, LocalDateTime.of(2023, 2, 25, 21, 40), 1);
        taskManager.createNewSubtask(subtask3);
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(), "Присвоен неверный статус");
    }

    @Test
    void getNewEpicStatusAllDone() {
        taskManager.createNewEpic(epic);
        Subtask subtask3 = new Subtask("Subtask 3", "Description 3", 2, TaskStatus.DONE, 5, LocalDateTime.of(2023, 2, 25, 21, 40), 1);
        taskManager.createNewSubtask(subtask3);
        Subtask subtask4 = new Subtask("Subtask 4", "Description 4", 3, TaskStatus.DONE, 5, LocalDateTime.of(2023, 2, 25, 21, 50), 1);
        taskManager.createNewSubtask(subtask4);
        assertEquals(TaskStatus.DONE, epic.getStatus(), "Присвоен неверный статус");
    }

    @Test
    void getNewEpicStatusInProgress() {
        taskManager.createNewEpic(epic);
        Subtask subtask3 = new Subtask("Subtask 3", "Description 3", 2, TaskStatus.IN_PROGRESS, 5, LocalDateTime.of(2023, 2, 25, 21, 40), 1);
        taskManager.createNewSubtask(subtask3);
        Subtask subtask4 = new Subtask("Subtask 4", "Description 4", 3, TaskStatus.IN_PROGRESS, 5, LocalDateTime.of(2023, 2, 25, 21, 50), 1);
        taskManager.createNewSubtask(subtask4);
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(), "Присвоен неверный статус");
    }

    @Test
    void createNewSubtask() {
        taskManager.createNewEpic(epic);
        taskManager.createNewSubtask(subtask);
        assertEquals(1, taskManager.getAllSubtasks().size(), "Неверное количество сабтасков");
        taskManager.createNewSubtask(subtask2);
        assertEquals(2, taskManager.getAllSubtasks().size(), "Неверное количество сабтасков");
    }

    @Test
    void createNewSubtaskWithEmptySubtasks() {
        taskManager.createNewEpic(epic);
        taskManager.createNewSubtask(subtask);
        assertEquals(1, taskManager.getAllSubtasks().size(), "Неверное количество сабтасков");
    }

    @Test
    void getAllSubtasks() {
        taskManager.createNewEpic(epic);
        assertEquals(0, taskManager.getAllSubtasks().size(), "Неверное количество сабтасков");
        taskManager.createNewSubtask(subtask);
        assertEquals(1, taskManager.getAllSubtasks().size(), "Неверное количество сабтасков");
        taskManager.createNewSubtask(subtask2);
        assertEquals(2, taskManager.getAllSubtasks().size(), "Неверное количество сабтасков");
    }

    @Test
    void getAllSubtasksWithEmptySubtasks() {
        taskManager.createNewEpic(epic);
        assertEquals(0, taskManager.getAllSubtasks().size(), "Неверное количество сабтасков");
    }

    @Test
    void getAllSubtasksByEpicWithEmptyEpics() {
        assertEquals(new ArrayList(), taskManager.getAllSubtasks(), "Неверное количество сабтасков");
    }

    @Test
    void deleteAllSubtasks() {
        taskManager.createNewEpic(epic);
        taskManager.createNewSubtask(subtask);
        taskManager.createNewSubtask(subtask2);
        assertEquals(2, taskManager.getAllSubtasks().size(), "Неверное количество сабтасков");
        taskManager.deleteAllSubtasks();
        assertEquals(0, taskManager.getAllSubtasks().size(), "Неверное количество сабтасков");
    }

    @Test
    void deleteAllSubtasksWithEmptySubtasks() {
        taskManager.createNewEpic(epic);
        taskManager.deleteAllSubtasks();
        assertTrue(taskManager.getAllSubtasks().isEmpty(), "Неверное количество сабтасков");
    }

    @Test
    void getSubtaskById() {
        taskManager.createNewEpic(epic);
        taskManager.createNewSubtask(subtask);
        assertEquals(subtask, taskManager.getSubtaskById(2), "Неверный сабтаск");
    }

    @Test
    void getSubtaskByWrongId() {
        taskManager.createNewEpic(epic);
        taskManager.createNewSubtask(subtask);
        assertNull(taskManager.getSubtaskById(10), "Нет сабстаска с таким id");
    }

    @Test
    void getSubtaskByIdWithEmptySubtasks() {
        taskManager.createNewEpic(epic);
        assertNull(taskManager.getSubtaskById(2), "Cабтаск не существует");
    }

    @Test
    void getSubtaskByIdEpic() {
        taskManager.createNewEpic(epic);
        taskManager.createNewSubtask(subtask);
        taskManager.createNewSubtask(subtask2);
        assertEquals(2, taskManager.getAllEpicSubtasks(1).size(), "Cабтаск не существует");
    }

    @Test
    void updateSubtask() {
        taskManager.createNewEpic(epic);
        taskManager.createNewSubtask(subtask);
        taskManager.updateSubtask(subtask1_1);
        assertEquals(subtask1_1, taskManager.getSubtaskById(2), "Сабтаск не обновлен");
    }

    @Test
    void updateSubtaskWithWrongId() {
        taskManager.createNewEpic(epic);
        taskManager.createNewSubtask(subtask);
        taskManager.updateSubtask(subtask2_1);
        assertNotEquals(subtask2_1, taskManager.getSubtaskById(2), "Сабтаск не обновлен");
    }

    @Test
    void updateSubtaskWithEmptySubtasks() {
        taskManager.updateSubtask(subtask1_1);
        assertEquals(0, taskManager.getAllSubtasks().size(), "Сабтаск не обновлен");
    }

    @Test
    void deleteSubtaskById() {
        taskManager.createNewEpic(epic);
        taskManager.createNewSubtask(subtask);
        taskManager.deleteSubtaskById(subtask.getId());
        assertTrue(taskManager.getAllSubtasks().isEmpty(), "Сабтаск не удален");
    }

    @Test
    void deleteSubtaskByIdWithEmptySubstaks() {
        taskManager.createNewEpic(epic);
        taskManager.deleteSubtaskById(2);
        assertEquals(0, taskManager.getAllSubtasks().size(), "Список сабтасков должен быть пуст");
    }

    @Test
    void deleteSubtaskByWrongId() {
        taskManager.createNewEpic(epic);
        taskManager.createNewSubtask(subtask);
        taskManager.deleteSubtaskById(3);
        assertEquals(1, taskManager.getAllSubtasks().size(), "Сабтаск c неверным id");
    }

    @Test
    void getAllEpicSubtasks() {
        taskManager.createNewEpic(epic);
        assertEquals(0, taskManager.getAllEpicSubtasks(1).size(), "Неверное количество сабтасков");
        taskManager.createNewSubtask(subtask);
        assertEquals(1, taskManager.getAllEpicSubtasks(1).size(), "Неверное количество сабтасков");
        taskManager.createNewSubtask(subtask2);
        assertEquals(2, taskManager.getAllEpicSubtasks(1).size(), "Неверное количество сабтасков");
    }

    @Test
    void getAllSubtasksByWrongEpicId() {
        taskManager.createNewEpic(epic);
        taskManager.createNewSubtask(subtask);
        taskManager.createNewSubtask(subtask2);
        assertNull(taskManager.getAllEpicSubtasks(10), "Эпика с таким id нет");
    }

    @Test
    void getHistory() {
        taskManager.createNewEpic(epic);//id=1
        taskManager.createNewTask(task);//id=2
        taskManager.createNewSubtask(subtask);//id=3
        taskManager.createNewSubtask(subtask2);//id=4
        taskManager.getEpicById(1);
        taskManager.getTaskById(2);
        assertEquals(2, taskManager.getHistory().size(), "История отображается неверно");
        taskManager.getSubtaskById(3);
        taskManager.getSubtaskById(4);
        assertEquals(4, taskManager.getHistory().size(), "История отображается неверно");
    }

    @Test
    void getHistoryWithEmptyTasks() {
        taskManager.createNewTask(task);//id=1
        taskManager.createNewEpic(epic);//id=2
        taskManager.createNewSubtask(subtask);//id=3
        taskManager.createNewSubtask(subtask2);//id=4
        assertTrue(taskManager.getHistory().isEmpty(), "История должны быть пуста");
    }

    @Test
    void getPrioritizedTasks() {
        taskManager.createNewEpic(epic);//id=1
        assertEquals(0, taskManager.getPrioritizedTasks().size(), "Неверное количество задач");
        taskManager.createNewTask(task);//id=2
        assertEquals(1, taskManager.getPrioritizedTasks().size(), "Неверное количество задач");
        taskManager.createNewSubtask(subtask);//id=3
        taskManager.createNewSubtask(subtask2);//id=4
        assertEquals(3, taskManager.getPrioritizedTasks().size(), "Неверное количество задач");
    }

    @Test
    void getPrioritizedTasksWithEmptyTasks() {
        taskManager.createNewEpic(epic);
        assertEquals(new ArrayList(){}, taskManager.getPrioritizedTasks(), "Неверное количество задач");
    }

    @Test
    void validateTimeWithWrongTimeTask() {
        taskManager.createNewTask(task);
        assertEquals(1, taskManager.getPrioritizedTasks().size(), "Неверное количество задач");
        Task task3 = new Task("Task 1", "Description 1", 5, LocalDateTime.of(2023, 1, 31, 21, 23));
        taskManager.createNewTask(task3);
        assertEquals(1, taskManager.getPrioritizedTasks().size(), "Неверное количество задач");
    }

    @Test
    void validateTimeWithWrongTimeSubtask() {
        taskManager.createNewEpic(epic);
        assertEquals(0, taskManager.getPrioritizedTasks().size(), "Неверное количество задач");
        taskManager.createNewTask(task);
        assertEquals(1, taskManager.getPrioritizedTasks().size(), "Неверное количество задач");
        Subtask subtask3 = new Subtask("Subtask 1", "Description 1", 5, LocalDateTime.of(2023, 1, 31, 21, 24), 1);
        taskManager.createNewTask(subtask3);
        assertEquals(1, taskManager.getPrioritizedTasks().size(), "Неверное количество задач");
    }

    @Test
    void validateWithNullTime() {
        Task task3 = new Task("Task 3", "Description 3", 10, (LocalDateTime) null);
        taskManager.createNewTask(task);
        assertNull(task3.getStartTime(), "Неверное значение");
        assertNull(task3.getEndTime(), "Неверное значение");
    }
}

