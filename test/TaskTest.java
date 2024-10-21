import savingfiles.TaskType;
import org.junit.jupiter.api.Assertions;
import tasks.*;
import states.TaskState;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskTest {
    @Test
    public void testTasksEqualityById() {
        Task task1 = new Task(1, TaskType.TASK, "Task 1", TaskState.NEW, "Description 1", Duration.ofHours(1), LocalDateTime.now());
        Task task2 = new Task(1, TaskType.TASK, "Task 1", TaskState.NEW, "Description 1", Duration.ofHours(1), LocalDateTime.now());

        task1.setId(1);
        task2.setId(1);

        Assertions.assertTrue(task1.getId() == task2.getId(), "Задачи должны быть одинаковыми по идентификатору");
    }

    @Test
    void testGetEndTime() {
        LocalDateTime startTime = LocalDateTime.of(2023, 12, 1, 10, 0, 0);
        Duration duration = Duration.ofHours(2);
        Task task = new Task(0, TaskType.TASK, "Task 1", TaskState.NEW, "Description 1", duration, startTime);

        assertEquals(startTime.plusHours(2), task.getEndTime());
    }
}