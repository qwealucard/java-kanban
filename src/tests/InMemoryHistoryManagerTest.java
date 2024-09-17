package tests;

import history.InMemoryHistoryManager;
import memory.InMemoryTaskManager;
import org.junit.Test;
import states.TaskState;
import tasks.Task;

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
}
