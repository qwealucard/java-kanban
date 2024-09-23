package test;

import history.InMemoryHistoryManager;
import memory.InMemoryTaskManager;
import org.junit.Test;
import states.TaskState;
import tasks.Task;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InMemoryHistoryManagerTest {
    @Test
    public void testAddTaskAndNodeMap() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Task task1 = new Task("Task 1", "Description 1", TaskState.NEW);
        Task task2 = new Task("Task 2", "Description 2", TaskState.IN_PROGRESS);

        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);

        Map<Integer, InMemoryHistoryManager.TaskNode> nodeMap = taskManager.getHistoryManager().getNodeMap();
        assertNotNull(nodeMap);
        assertEquals(2, nodeMap.size());
        assertTrue(nodeMap.containsKey(task1.getId()));
        assertTrue(nodeMap.containsKey(task2.getId()));
    }

    @Test
    public void testRemoveTaskFromHistory() {

        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        Task task1 = new Task("Task 1", "Description 1", TaskState.NEW);
        Task task2 = new Task("Task 2", "Description 2", TaskState.IN_PROGRESS);

        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);

        taskManager.getHistoryManager().removeHistory(task2.getId());

        Map<Integer, InMemoryHistoryManager.TaskNode> history = taskManager.getHistoryManager().getNodeMap();

        assertNotNull(history);
        assertEquals(1, history.size());
    }

    @Test
    public void testRemoveFirstNode() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        Task task1 = new Task("Task 1", "Description 1", TaskState.NEW);
        Task task2 = new Task("Task 2", "Description 2", TaskState.IN_PROGRESS);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.removeHistory(1);

        List<Task> tasks = historyManager.getTasks();
        assertEquals(1, tasks.size());
        assertEquals(task2, tasks.get(0));
    }

    @Test
    public void testRemoveLastNode() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        Task task1 = new Task("Task 1", "Description 1", TaskState.NEW);
        Task task2 = new Task("Task 2", "Description 2", TaskState.IN_PROGRESS);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.removeHistory(1);

        List<Task> tasks = historyManager.getTasks();
        assertEquals(1, tasks.size());
        assertEquals(task2, tasks.get(0));
    }

    @Test
    public void testRemoveIntermediateNode() {
        InMemoryTaskManager manager = new InMemoryTaskManager();

        Task task1 = new Task("Task 1", "Description 1", TaskState.NEW);
        Task task2 = new Task("Task 2", "Description 2", TaskState.IN_PROGRESS);
        Task task3 = new Task("Task 3", "Description 3", TaskState.DONE);
        Task task4 = new Task("Task 4", "Description 4", TaskState.NEW);

        manager.addNewTask(task1);
        manager.addNewTask(task2);
        manager.addNewTask(task3);
        manager.addNewTask(task4);

        manager.getHistoryManager().removeHistory(1);

        List<Task> tasks = manager.getHistoryManager().getTasks();
        assertEquals(3, tasks.size());
        assertEquals(task1, tasks.get(0));
        assertEquals(task3, tasks.get(1));
        assertEquals(task4, tasks.get(2));
    }

    @Test
    public void testAddExistingTask() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        Task task1 = new Task("Task 1", "Description 1", TaskState.NEW);

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

        historyManager.add(task1);

        List<Task> tasks = historyManager.getTasks();
        assertEquals(1, tasks.size());
        assertEquals(task1, tasks.get(0));
    }

    @Test
    public void testRemoveOnlyElement() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        Task task1 = new Task("Task 1", "Description 1", TaskState.NEW);

        historyManager.add(task1);
        historyManager.removeHistory(0);

        List<Task> tasks = historyManager.getTasks();
        assertEquals(0, tasks.size());
    }
}
