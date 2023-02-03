package manager.file;

import manager.TaskManagerTest;
import manager.memory.InMemoryTaskManager;
import models.Epic;
import models.Subtask;
import models.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    /*Дополнительно для FileBackedTasksManager — проверка работы по сохранению и восстановлению состояния. Граничные условия:
    a. Пустой список задач.
    b. Эпик без подзадач.
    c. Пустой список истории.*/

    private File file1;

    FileBackedTasksManager fileBackedTasksManager2;

    @BeforeEach
    void beforeEach() {
        file1 = new File("test/file1.csv");
        taskManager = new FileBackedTasksManager(file1);
        fileBackedTasksManager2 = FileBackedTasksManager.loadFromFile(file1);
    }
    @AfterEach


    @Test
    void save() {
        /*assertEquals(0,taskManager.getAllTasks().size(),"Неверное количество задач");
        Task task1 = new Task("Task1", "description task1", 9, LocalDateTime.of(2022, 3, 8, 16, 30));
        taskManager.createNewTask(task1);
        assertEquals(1,taskManager.getAllTasks().size(),"Неверное количество задач");
        Epic epic2 = new Epic("Epic2", "description epic2");
        taskManager.createNewEpic(epic2);
        assertEquals(1,taskManager.getAllEpics().size(),"Неверное количество задач");
        assertEquals(0, taskManager.getAllSubtasks().size(), "Неверное количество сабтасков");
        assertEquals(0,taskManager.getHistory().size(),"История должна быть пустая");
        Subtask subtask3 = new Subtask("Subtask 3", "Description 3", 10, LocalDateTime.of(2023, 1, 31, 21, 35), 2);
        taskManager.createNewSubtask(subtask3);
        assertEquals(1,taskManager.getAllSubtasks().size(), "Неверное количество сабтасков");*/
    }

    @Test
    void loadFromFile() {




    }


}