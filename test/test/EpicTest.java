package test;

import savingfiles.TaskType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import states.TaskState;
import tasks.Epic;

public class EpicTest {
    @Test
    public void testEpicsEqualityById() {
        Epic epic1 = new Epic(2, TaskType.EPIC, "Epic 1", TaskState.NEW, "Description 1");
        Epic epic2 = new Epic(2, TaskType.EPIC, "Epic 2", TaskState.NEW, "Description 1");

        Assertions.assertTrue(epic1.getId() == epic2.getId(), "Задачи должны быть одинаковыми по идентификатору");
    }
}