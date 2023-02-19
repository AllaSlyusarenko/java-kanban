package manager.http;

import manager.TaskManagerTest;

import models.Epic;
import models.Subtask;
import models.Task;
import org.junit.jupiter.api.*;
import server.KVServer;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    KVServer kvServer;

    @BeforeEach
    void init() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        taskManager = new HttpTaskManager(KVServer.PORT);
        initTasks();
    }

    @AfterEach
    void clear() {
        taskManager.deleteAllTasks();
        taskManager.deleteAllSubtasks();
        taskManager.deleteAllEpics();
        kvServer.stop();
    }

    @Test
    void loadTasks() {
        taskManager.createNewTask(task);
        assertNotNull(taskManager.getAllTasks(), "Возвращает не пустой список задач");
        assertEquals(1, taskManager.getAllTasks().size(), "Возвращает не пустой список задач");
        taskManager.createNewTask(task2);
        assertEquals(2, taskManager.getAllTasks().size(), "Возвращает не пустой список задач");
    }

    @Test
    void loadSubtasks() {
        taskManager.createNewEpic(epic);
        int idEpic = epic.getId();
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask 2", 5, LocalDateTime.of(2022, 3, 6, 15, 30), idEpic);
        taskManager.createNewSubtask(subtask2);
        assertNotNull(taskManager.getAllSubtasks(), "Возвращает не пустой список подзадач");
        assertEquals(1, taskManager.getAllSubtasks().size(), "Возвращает не пустой список подзадач");
        Subtask subtask3 = new Subtask("Subtask 3", "Subtask 3", 5, LocalDateTime.of(2022, 3, 8, 15, 30), idEpic);
        taskManager.createNewSubtask(subtask3);
        assertEquals(2, taskManager.getAllSubtasks().size(), "Возвращает не пустой список подзадач");
    }

    @Test
    void loadEpics() {
        Epic epic1 = new Epic("Epic 1", "Epic 1");
        taskManager.createNewEpic(epic1);
        int idEpic = epic1.getId();
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask 2", 5, LocalDateTime.of(2022, 3, 6, 15, 30), idEpic);
        taskManager.createNewSubtask(subtask2);
        assertNotNull(taskManager.getAllEpics(), "Возвращает не пустой список эпиков");
        assertEquals(1, taskManager.getAllEpics().size(), "Возвращает не пустой список эпиков");
        Epic epic3 = new Epic("Epic 3", "Epic 3");
        taskManager.createNewEpic(epic3);
        assertEquals(2, taskManager.getAllEpics().size(), "Возвращает не пустой список эпиков");
    }

    @Test
    void loadHistory() {
        taskManager.createNewTask(task);
        int idTask = task.getId();
        taskManager.getTaskById(idTask);
        assertNotNull(taskManager.getHistory(), "Возвращает не пустой список истории");
        assertEquals(1, taskManager.getHistory().size(), "Возвращает не пустой список истории");
        assertEquals(task, taskManager.getHistory().get(0));

        Epic epic2 = new Epic("Epic 2", "Epic 2");
        taskManager.createNewEpic(epic2);
        int idEpic = epic2.getId();
        taskManager.getEpicById(idEpic);
        assertEquals(2, taskManager.getHistory().size(), "Возвращает не пустой список истории");
        assertEquals(epic2, taskManager.getHistory().get(1));

        Subtask subtask3 = new Subtask("Subtask 3", "Subtask 3", 5, LocalDateTime.of(2022, 3, 6, 15, 30), idEpic);
        taskManager.createNewSubtask(subtask3);
        int idSubtask = subtask3.getId();
        taskManager.getSubtaskById(idSubtask);
        assertEquals(3, taskManager.getHistory().size(), "Возвращает не пустой список истории");
        assertEquals(subtask3, taskManager.getHistory().get(2));
    }
}