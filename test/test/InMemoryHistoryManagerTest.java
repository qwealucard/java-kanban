package test;

import history.InMemoryHistoryManager;
import org.junit.Test;
import states.TaskState;
import tasks.Task;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InMemoryHistoryManagerTest {
    @Test
    public void testAddTaskAndNodeMap() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task("Task 1", "Description 1", TaskState.NEW);
        task1.setId(0);
        Task task2 = new Task("Task 2", "Description 2", TaskState.IN_PROGRESS);
        task2.setId(1);

        historyManager.add(task1);
        historyManager.add(task2);
        List<Task> historyList1 = historyManager.getHistoryList();
        assertNotNull(historyList1);
        assertEquals(2, historyList1.size());
    }

    @Test
    public void testRemoveTaskFromHistory() {

        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        Task task1 = new Task("Task 1", "Description 1", TaskState.NEW);
        task1.setId(0);
        Task task2 = new Task("Task 2", "Description 2", TaskState.IN_PROGRESS);
        task2.setId(1);

        historyManager.add(task1);
        historyManager.add(task2);

        historyManager.remove(task2.getId());

        List<Task> historyList1 = historyManager.getHistoryList();

        assertNotNull(historyList1);
        assertEquals(1, historyList1.size());
    }

    @Test
    public void testRemoveFirstNode() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        Task task1 = new Task("Task 1", "Description 1", TaskState.NEW);
        task1.setId(0);
        Task task2 = new Task("Task 2", "Description 2", TaskState.IN_PROGRESS);
        task2.setId(1);

        historyManager.add(task1);
        historyManager.add(task2);

        historyManager.remove(0);

        List<Task> tasks = historyManager.getTasks();

        assertEquals(1, tasks.size());
        assertEquals(task2, tasks.get(0));
    }

    @Test
    public void testRemoveLastNode() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        Task task1 = new Task("Task 1", "Description 1", TaskState.NEW);
        task1.setId(0);
        Task task2 = new Task("Task 2", "Description 2", TaskState.IN_PROGRESS);
        task2.setId(1);
        Task task3 = new Task("Task 3", "Description 3", TaskState.IN_PROGRESS);
        task3.setId(2);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(0);

        List<Task> tasks = historyManager.getTasks();

        assertEquals(2, tasks.size());
        assertEquals(task2, tasks.get(0));
    }

    @Test
    public void testRemoveIntermediateNode() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        Task task1 = new Task("Task 1", "Description 1", TaskState.NEW);
        task1.setId(0);
        Task task2 = new Task("Task 2", "Description 2", TaskState.IN_PROGRESS);
        task2.setId(1);
        Task task3 = new Task("Task 3", "Description 3", TaskState.DONE);
        task3.setId(2);
        Task task4 = new Task("Task 4", "Description 4", TaskState.NEW);
        task4.setId(3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task4);

        historyManager.remove(2);

        List<Task> tasks = historyManager.getTasks();

        assertEquals(3, tasks.size());
        assertEquals(task1, tasks.get(0));
        assertEquals(task2, tasks.get(1));
        assertEquals(task4, tasks.get(2));
    }

    @Test
    public void testAddExistingTask() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        Task task1 = new Task("Task 1", "Description 1", TaskState.NEW);
        task1.setId(0);

        historyManager.add(task1);
        historyManager.add(task1);

        List<Task> tasks = historyManager.getTasks();

        assertEquals(1, tasks.size());
        assertEquals(task1, tasks.get(0));
    }

    @Test
    public void testAddFirstElementToEmptyHistory() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        Task task1 = new Task("Task 1", "Description 1", TaskState.NEW);
        task1.setId(0);

        historyManager.add(task1);

        List<Task> tasks = historyManager.getTasks();

        assertEquals(1, tasks.size());
        assertEquals(task1, tasks.get(0));
    }

    @Test
    public void testRemoveOnlyElement() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        Task task1 = new Task("Task 1", "Description 1", TaskState.NEW);
        task1.setId(0);

        historyManager.add(task1);

        historyManager.remove(0);

        List<Task> tasks = historyManager.getTasks();

        assertEquals(0, tasks.size());
    }
}
