package tests;

import history.InMemoryHistoryManager;
import interfaces.HistoryManager;
import interfaces.TaskManager;
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
        Task task = new Task("Task 1", "Description 1", TaskState.NEW);

        historyManager.add(task);

        final List<Task> history = historyManager.getViewedTaskHistory();

        assertNotNull(history);
        assertEquals("Размер истории должен быть 1", 1, history.size());
    }

    @Test
    public void testTaskVersioningInHistoryManager() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task originalTask = new Task("Задача", "Описание", TaskState.NEW);
        historyManager.add(originalTask);

        Task modifiedTask = new Task(originalTask.getName(), originalTask.getDescription(), originalTask.getState());
        modifiedTask.setDescription("Измененное описание");
        historyManager.add(modifiedTask);

        Task modifiedVersion = historyManager.getViewedTaskHistory().getFirst();

        assertEquals("Описание", modifiedVersion.getDescription());
        assertEquals(TaskState.NEW, modifiedTask.getState());
    }
}
