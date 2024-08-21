package tests;

import org.junit.Test;
import states.TaskState;
import tasks.Epic;

import static org.junit.Assert.assertTrue;

public class EpicTest {
    @Test
    public void testEpicsEqualityById() {
        Epic epic1 = new Epic("Epic 1", "Description 1", TaskState.NEW);
        Epic epic2 = new Epic("Epic 2", "Description 2", TaskState.IN_PROGRESS);

        epic1.setId(2);
        epic2.setId(2);

        assertTrue("Задачи должны быть одинаковыми по идентификатору", epic1.getId() == epic2.getId());

    }
}