package test;

import org.junit.jupiter.api.Assertions;
import tasks.*;
import states.TaskState;
import org.junit.jupiter.api.Test;

public class TaskTest {
    @Test
    public void testTasksEqualityById() {
        Task task1 = new Task("Task 1", "Description 1", TaskState.NEW);
        Task task2 = new Task("Task 2", "Description 2", TaskState.IN_PROGRESS);

        task1.setId(1);
        task2.setId(1);

        Assertions.assertTrue(task1.getId() == task2.getId(), "Задачи должны быть одинаковыми по идентификатору");
        ;
    }
}