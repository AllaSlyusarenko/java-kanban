package manager.file;

import manager.TaskManagerTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    private File file1;

    @BeforeEach
    void beforeEach() {
        file1 = new File("test/file1.csv");
        taskManager = new FileBackedTasksManager(file1);
        initTasks();
    }

    @AfterEach
    void deleteFile() {
        file1.delete();
    }

    @Test
    void save() {
    }

    @Test
    void loadFromFileEmpty() {
        FileBackedTasksManager fileBackedTasksManager2 = FileBackedTasksManager.loadFromFile(file1);
        assertEquals(taskManager.getAllTasks().size(), fileBackedTasksManager2.getAllTasks().size());
        assertEquals(taskManager.getAllSubtasks().size(), fileBackedTasksManager2.getAllSubtasks().size());
        assertEquals(taskManager.getAllEpics().size(), fileBackedTasksManager2.getAllEpics().size());
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
    }

    @Test
    void loadFromFile() {
        taskManager.createNewEpic(epic);
        taskManager.createNewTask(task);
        taskManager.createNewTask(task2);
        taskManager.createNewSubtask(subtask);
        taskManager.createNewSubtask(subtask2);
        FileBackedTasksManager fileBackedTasksManager2 = FileBackedTasksManager.loadFromFile(file1);
        fileBackedTasksManager2.getTaskById(2);
        fileBackedTasksManager2.getEpicById(1);
        fileBackedTasksManager2.getTaskById(3);
        fileBackedTasksManager2.getSubtaskById(4);
        fileBackedTasksManager2.getSubtaskById(5);
        fileBackedTasksManager2.getEpicById(1);
        assertEquals(taskManager.getAllTasks().size(), fileBackedTasksManager2.getAllTasks().size());
        assertEquals(taskManager.getAllSubtasks().size(), fileBackedTasksManager2.getAllSubtasks().size());
        assertEquals(taskManager.getAllEpics().size(), fileBackedTasksManager2.getAllEpics().size());
    }
}