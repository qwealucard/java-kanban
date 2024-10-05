package test;

import saving_files.TaskType;
import org.junit.jupiter.api.Assertions;
import tasks.*;
import states.TaskState;
import org.junit.jupiter.api.Test;

public class TaskTest {
    @Test
    public void testTasksEqualityById() {
        Task task1 = new Task(1, TaskType.TASK, "Task 1", TaskState.NEW, "Description 1");
        Task task2 = new Task(1, TaskType.TASK, "Task 1", TaskState.NEW, "Description 1");

        task1.setId(1);
        task2.setId(1);

        Assertions.assertTrue(task1.getId() == task2.getId(), "Задачи должны быть одинаковыми по идентификатору");
    }
}