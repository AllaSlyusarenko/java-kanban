import manager.TaskManager;
import models.Task;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest <T extends TaskManager> {
    HashMap<Integer, Task> users = new HashMap<>();

    @Test
    void test() {
        users.put(1, new Task("1","sgfhf"));
        users.put(2, new Task("2","sgfhf2"));
        assertAll(
                () ->assertEquals(2,users.size()),
                ()->assertEquals(2,users.size())


        );
    }
}

