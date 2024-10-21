import savingfiles.TaskType;
import history.InMemoryHistoryManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import states.TaskState;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InMemoryHistoryManagerTest {
    @Test
    public void testAddTaskAndNodeMap() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task(0, TaskType.TASK, "Task 1", TaskState.NEW, "Description 1", Duration.ofHours(1), LocalDateTime.now());
        Task task2 = new Task(1, TaskType.TASK, "Task 2", TaskState.NEW, "Description 2", Duration.ofHours(1), LocalDateTime.now());
        task1.setId(0);
        task2.setId(1);

        historyManager.add(task1);
        historyManager.add(task2);
        List<Task> historyList1 = historyManager.getViewedTaskHistory();
        assertNotNull(historyList1);
        Assertions.assertEquals(2, historyList1.size());
    }

    @Test
    public void testRemoveTaskFromHistory() {

        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        Task task1 = new Task(0, TaskType.TASK, "Task 1", TaskState.NEW, "Description 1", Duration.ofHours(1), LocalDateTime.now());
        Task task2 = new Task(1, TaskType.TASK, "Task 2", TaskState.NEW, "Description 2", Duration.ofHours(1), LocalDateTime.now());
        task1.setId(0);
        task2.setId(1);

        historyManager.add(task1);
        historyManager.add(task2);

        historyManager.remove(task2.getId());

        List<Task> historyList1 = historyManager.getViewedTaskHistory();

        assertNotNull(historyList1);
        Assertions.assertEquals(1, historyList1.size());
    }

    @Test
    public void testRemoveFirstNode() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        Task task1 = new Task(0, TaskType.TASK, "Task 1", TaskState.NEW, "Description 1", Duration.ofHours(1), LocalDateTime.now());
        Task task2 = new Task(1, TaskType.TASK, "Task 2", TaskState.NEW, "Description 2", Duration.ofHours(1), LocalDateTime.now());
        task1.setId(0);
        task2.setId(1);

        historyManager.add(task1);
        historyManager.add(task2);

        historyManager.remove(0);

        List<Task> tasks = historyManager.getTasks();

        Assertions.assertEquals(1, tasks.size());
        Assertions.assertEquals(task2, tasks.get(0));
    }

    @Test
    public void testRemoveLastNode() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        Task task1 = new Task(0, TaskType.TASK, "Task 1", TaskState.NEW, "Description 1", Duration.ofHours(1), LocalDateTime.now());
        Task task2 = new Task(1, TaskType.TASK, "Task 2", TaskState.NEW, "Description 2", Duration.ofHours(1), LocalDateTime.now());
        Task task3 = new Task(2, TaskType.TASK, "Task 3", TaskState.NEW, "Description 3", Duration.ofHours(1), LocalDateTime.now());
        task1.setId(0);
        task2.setId(1);
        task3.setId(2);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(0);

        List<Task> tasks = historyManager.getTasks();

        Assertions.assertEquals(2, tasks.size());
        Assertions.assertEquals(task2, tasks.get(0));
    }

    @Test
    public void testRemoveIntermediateNode() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        Task task1 = new Task(0, TaskType.TASK, "Task 1", TaskState.NEW, "Description 1", Duration.ofHours(1), LocalDateTime.now());
        Task task2 = new Task(1, TaskType.TASK, "Task 2", TaskState.NEW, "Description 2", Duration.ofHours(1), LocalDateTime.now());
        Task task3 = new Task(2, TaskType.TASK, "Task 3", TaskState.NEW, "Description 3", Duration.ofHours(1), LocalDateTime.now());
        Task task4 = new Task(3, TaskType.TASK, "Task 4", TaskState.NEW, "Description 4", Duration.ofHours(1), LocalDateTime.now());
        task1.setId(0);
        task2.setId(1);
        task3.setId(2);
        task4.setId(3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task4);

        historyManager.remove(2);

        List<Task> tasks = historyManager.getTasks();

        Assertions.assertEquals(3, tasks.size());
        Assertions.assertEquals(task1, tasks.get(0));
        Assertions.assertEquals(task2, tasks.get(1));
        Assertions.assertEquals(task4, tasks.get(2));
    }

    @Test
    public void testAddExistingTask() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        Task task1 = new Task(0, TaskType.TASK, "Task 1", TaskState.NEW, "Description 1", Duration.ofHours(1), LocalDateTime.now());
        task1.setId(0);

        historyManager.add(task1);
        historyManager.add(task1);

        List<Task> tasks = historyManager.getTasks();

        Assertions.assertEquals(1, tasks.size());
        Assertions.assertEquals(task1, tasks.get(0));
    }

    @Test
    public void testAddFirstElementToEmptyHistory() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        Task task1 = new Task(0, TaskType.TASK, "Task 1", TaskState.NEW, "Description 1", Duration.ofHours(1), LocalDateTime.now());
        task1.setId(0);

        historyManager.add(task1);

        List<Task> tasks = historyManager.getTasks();

        Assertions.assertEquals(1, tasks.size());
        Assertions.assertEquals(task1, tasks.get(0));
    }

    @Test
    public void testRemoveOnlyElement() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        Task task1 = new Task(0, TaskType.TASK, "Task 1", TaskState.NEW, "Description 1", Duration.ofHours(1), LocalDateTime.now());
        task1.setId(0);

        historyManager.add(task1);

        historyManager.remove(0);

        List<Task> tasks = historyManager.getTasks();

        Assertions.assertEquals(0, tasks.size());
    }
}
