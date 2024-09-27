package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import states.TaskState;
import tasks.Epic;
import tasks.Subtask;

public class SubtaskTest {

    @Test
    public void testSubtasksEqualityById() {
        Epic epic = new Epic("Epic", "Description", TaskState.NEW);
        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", TaskState.NEW, epic);
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", TaskState.IN_PROGRESS, epic);

        subtask1.setId(3);
        subtask2.setId(3);

        Assertions.assertTrue(subtask1.getId() == subtask2.getId(), "Задачи должны быть одинаковыми по идентификатору");
    }
}
