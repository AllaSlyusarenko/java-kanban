package manager.http;

import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.KVServer;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest {
    KVServer kvServer;
    HttpTaskServer httpTaskServer;
    TaskManager taskManager = httpTaskServer.getTaskManager();

    @BeforeEach
    void init() throws IOException {
        kvServer = new KVServer(); //8078
        kvServer.start();
        httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
    }


    @Test
    void loadTasks() {
        System.out.println(httpTaskServer.getTaskManager());

    }
    @Test
    void loadSubtasks() {

    }


    @Test
    void loadEpics() {

    }
    @Test
    void loadHistory() {

    }

    @AfterEach
    void stop(){
        kvServer.stop();
        httpTaskServer.stop();
    }
}