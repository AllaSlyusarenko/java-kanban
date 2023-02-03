package manager.file;

import manager.TaskManagerTest;
import models.Epic;
import models.Task;
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
    private File file2;
    FileBackedTasksManager fileBackedTasksManager1;

    @Test
    void save() {
        /*file1 = new File("src/file1.csv");
        fileBackedTasksManager1 = new FileBackedTasksManager(file1);
        assertEquals(0,fileBackedTasksManager1.getAllTasks().size(),"Неверное количество задач");
        Task task1 = new Task("Task1", "description task1", 9, LocalDateTime.of(2022, 3, 8, 16, 30));
        fileBackedTasksManager1.createNewTask(task1);
        assertEquals(1,fileBackedTasksManager1.getAllTasks().size(),"Неверное количество задач");
        Epic epic3 = new Epic("Epic3", "description epic3");
        fileBackedTasksManager1.createNewEpic(epic3);
        assertEquals(1,fileBackedTasksManager1.getAllEpics().size(),"Неверное количество задач");*/
    }

    @Test
    void loadFromFile() {
        file2 = new File("src/file2.csv");
    }


}