package test;

import savingfiles.TaskType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import states.TaskState;
import tasks.Epic;
import tasks.Subtask;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubtaskTest {

    @Test
    public void testSubtasksEqualityById() {
        Epic epic = new Epic(0, TaskType.EPIC, "Epic 1", TaskState.NEW, "Description 1", Duration.ofHours(1), LocalDateTime.now());
        Subtask subtask1 = new Subtask(3, TaskType.SUBTASK, "Subtask 1", TaskState.NEW, "Description 1", Duration.ofHours(1), LocalDateTime.now(), 0);
        Subtask subtask2 = new Subtask(3, TaskType.SUBTASK, "Subtask 1", TaskState.NEW, "Description 1", Duration.ofHours(1), LocalDateTime.now(), 0);

        subtask1.setId(3);
        subtask2.setId(3);

        Assertions.assertTrue(subtask1.getId() == subtask2.getId(), "Задачи должны быть одинаковыми по идентификатору");
    }

    @Test
    void testGetEndTime() {
        LocalDateTime startTime = LocalDateTime.of(2023, 12, 1, 10, 0, 0);
        Duration duration = Duration.ofHours(2);
        Subtask subtask = new Subtask(0, TaskType.SUBTASK, "Subtask 1", TaskState.NEW, "Description 1", duration, startTime, 1);

        assertEquals(startTime.plusHours(2), subtask.getEndTime());
    }
}
