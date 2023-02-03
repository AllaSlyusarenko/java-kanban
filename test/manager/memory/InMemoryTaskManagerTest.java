package manager.memory;


import manager.TaskManagerTest;
import org.junit.jupiter.api.BeforeEach;


public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    void beforeEach() {
        taskManager = new InMemoryTaskManager();
    }

    /*создание InMemoryTaskManager, что не пустой
            get all task не пустой
    и приоритный список не пустой*/

 /*возможно создан манаджер, чтобы не задваились*/
}