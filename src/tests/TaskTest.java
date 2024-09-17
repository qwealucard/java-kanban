package tests;

import tasks.*;
import states.TaskState;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TaskTest {
    @Test
    public void testTasksEqualityById() {
        Task task1 = new Task("Task 1", "Description 1", TaskState.NEW);
        Task task2 = new Task("Task 2", "Description 2", TaskState.IN_PROGRESS);

        task1.setId(1);
        task2.setId(1);

        assertTrue("Задачи должны быть одинаковыми по идентификатору", task1.getId() == task2.getId());
        ;
    }
}