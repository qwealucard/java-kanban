package tests;

import history.InMemoryHistoryManager;
import interface_class.HistoryManager;
import interface_class.TaskManager;
import memory.InMemoryTaskManager;
import org.junit.Test;
import states.TaskState;
import tasks.Task;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InMemoryHistoryManagerTest {
    @Test
    public void testAddToHistory() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        TaskManager taskManager = new InMemoryTaskManager();
        Task task = new Task("Task 1", "Description 1", TaskState.NEW);

        historyManager.add(task);

        final List<Task> history = taskManager.getHistory();
        assertNotNull(history);
        assertEquals("Размер истории должен быть 1", 1, history.size());
    }
}
