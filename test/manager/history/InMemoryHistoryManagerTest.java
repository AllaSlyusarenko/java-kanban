package manager.history;

import models.Epic;
import models.Task;
import models.TaskStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InMemoryHistoryManagerTest {
    HistoryManager historyManager;
    protected Task task;
    protected Task task2;
    protected Epic epic;

    @BeforeAll
    protected void initTasks() {
        task = new Task("Task 1", "Description 1", 1, TaskStatus.NEW, 5, LocalDateTime.of(2023, 1, 31, 21, 20));
        task2 = new Task("Task 2", "Description 2", 2, TaskStatus.IN_PROGRESS, 5, LocalDateTime.of(2023, 1, 31, 21, 28));
        epic = new Epic("Epic 1", "Description 1", 3, TaskStatus.IN_PROGRESS, 5, LocalDateTime.of(2023, 1, 31, 21, 38));
    }

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void add() {
        historyManager.add(task);
        assertEquals(1, historyManager.getHistory().size(), "Неверное количество задач в истории");
    }

    @Test
    void addTwice() {
        historyManager.add(task);
        historyManager.add(task2);
        historyManager.add(task);
        assertEquals(2, historyManager.getHistory().size(), "Неверное количество задач в истории");
    }

    @Test
    void addNull() {
        historyManager.add(null);
        assertEquals(0, historyManager.getHistory().size(), "Неверное количество задач в истории");
    }

    @Test
    void getHistory() {
        assertNotNull(historyManager.getHistory(), "Должен быть возвращен список");
        assertEquals(new ArrayList<>(), historyManager.getHistory(), "Должен быть пустой список");
    }

    @Test
    void removeFirst() {
        historyManager.add(task);
        historyManager.add(task2);
        historyManager.add(epic);
        historyManager.remove(1);
        assertEquals(2, historyManager.getHistory().size(), "Неверное количество задач в истории");
        assertEquals(2, historyManager.getHistory().get(0).getId(), "Неверный порядок задач в истории");
    }

    @Test
    void removeMiddle() {
        historyManager.add(task);
        historyManager.add(task2);
        historyManager.add(epic);
        historyManager.remove(2);
        assertEquals(2, historyManager.getHistory().size(), "Неверное количество задач в истории");
        assertEquals(1, historyManager.getHistory().get(0).getId(), "Неверный порядок задач в истории");
        assertEquals(3, historyManager.getHistory().get(1).getId(), "Неверный порядок задач в истории");
    }

    @Test
    void removeLast() {
        historyManager.add(task);
        historyManager.add(task2);
        historyManager.add(epic);
        historyManager.remove(3);
        assertEquals(2, historyManager.getHistory().size(), "Неверное количество задач в истории");
        assertEquals(1, historyManager.getHistory().get(0).getId(), "Неверный порядок задач в истории");
        assertEquals(2, historyManager.getHistory().get(1).getId(), "Неверный порядок задач в истории");
    }

    @Test
    void removeWithWrongIdFromEmptyHistory() {
        historyManager.remove(1);
        assertEquals(0, historyManager.getHistory().size(), "Неверное количество задач в истории");
        assertEquals(new ArrayList<>(), historyManager.getHistory(), "Неверное количество задач в истории");
    }
}