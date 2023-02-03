package manager.history;

import models.Epic;
import models.Subtask;
import models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;
    HistoryManager historyManager;

    @BeforeEach
    void setUp(){
        //наполнение исходными данными
    }

    /*Для HistoryManager — тесты для всех методов интерфейса. Граничные условия:
    a. Пустая история задач.
            b. Дублирование.
    с. Удаление из истории: начало, середина, конец.*/
    //public void add(Task task)
    //public List<Task> getHistory()
    //public void remove(int id)

    @Test
    void add() {
    }

    @Test
    void getHistory() {
    }

    @Test
    void remove() {
    }
}