package manager.file;

import manager.TaskManagerTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.Format;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    private File file1;

    @BeforeEach
    void beforeEach() throws IOException {
        initTasks();
        file1 = new File("test/file1.csv");
        Files.createFile(file1.toPath());
        taskManager = new FileBackedTasksManager(file1);
    }

    @AfterEach
    void deleteFile() {
        assertTrue(file1.delete(), "Файл удалён без ошибок");
    }

    @Test
    void loadFromFileEmpty() {
        FileBackedTasksManager fileBackedTasksManager2 = FileBackedTasksManager.loadFromFile(file1);
        assertEquals(taskManager.getAllTasks().size(), fileBackedTasksManager2.getAllTasks().size());
        assertEquals(taskManager.getAllSubtasks().size(), fileBackedTasksManager2.getAllSubtasks().size());
        assertEquals(taskManager.getAllEpics().size(), fileBackedTasksManager2.getAllEpics().size());
        assertEquals(0, taskManager.getHistory().size(), "История пуста");
    }

    @Test
    void loadFromFileWithoutHistory() {
        taskManager.createNewEpic(epic);
        taskManager.createNewTask(task);
        taskManager.createNewTask(task2);
        taskManager.createNewSubtask(subtask);
        taskManager.createNewSubtask(subtask2);
        FileBackedTasksManager fileBackedTasksManager2 = FileBackedTasksManager.loadFromFile(file1);
        assertEquals(taskManager.getAllTasks().size(), fileBackedTasksManager2.getAllTasks().size());
        assertEquals(taskManager.getAllSubtasks().size(), fileBackedTasksManager2.getAllSubtasks().size());
        assertEquals(taskManager.getAllEpics().size(), fileBackedTasksManager2.getAllEpics().size());
        assertEquals(0, taskManager.getHistory().size(), "История пуста");
    }

    @Test
    void loadFromFile() {
        taskManager.createNewEpic(epic);
        taskManager.createNewTask(task);
        taskManager.createNewTask(task2);
        taskManager.createNewSubtask(subtask);
        taskManager.createNewSubtask(subtask2);
        taskManager.getTaskById(2);
        taskManager.getEpicById(1);
        taskManager.getTaskById(3);
        taskManager.getSubtaskById(4);
        taskManager.getSubtaskById(5);
        taskManager.getEpicById(1);
        FileBackedTasksManager fileBackedTasksManager2 = FileBackedTasksManager.loadFromFile(file1);
        assertEquals(taskManager.getAllTasks().size(), fileBackedTasksManager2.getAllTasks().size());
        assertEquals(taskManager.getAllSubtasks().size(), fileBackedTasksManager2.getAllSubtasks().size());
        assertEquals(taskManager.getAllEpics().size(), fileBackedTasksManager2.getAllEpics().size());
        assertEquals(taskManager.getPrioritizedTasks(), fileBackedTasksManager2.getPrioritizedTasks(), "Отсортированные списки должны быть равны");
    }
}