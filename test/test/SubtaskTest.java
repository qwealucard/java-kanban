package test;

import savingfiles.TaskType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import states.TaskState;
import tasks.Epic;
import tasks.Subtask;

public class SubtaskTest {

    @Test
    public void testSubtasksEqualityById() {
        Epic epic = new Epic(0, TaskType.EPIC, "Epic 1", TaskState.NEW, "Description 1");
        Subtask subtask1 = new Subtask(3, TaskType.SUBTASK, "Subtask 1", TaskState.NEW, "Description 1", 0);
        Subtask subtask2 = new Subtask(3, TaskType.SUBTASK, "Subtask 1", TaskState.NEW, "Description 1", 0);

        subtask1.setId(3);
        subtask2.setId(3);

        Assertions.assertTrue(subtask1.getId() == subtask2.getId(), "Задачи должны быть одинаковыми по идентификатору");
    }
}
