import savingfiles.TaskType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import states.TaskState;
import tasks.Epic;
import tasks.Subtask;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class EpicTest {
    @Test
    public void testEpicsEqualityById() {
        Epic epic1 = new Epic(2, TaskType.EPIC, "Epic 1", TaskState.NEW, "Description 1", Duration.ofHours(1), LocalDateTime.now());
        Epic epic2 = new Epic(2, TaskType.EPIC, "Epic 2", TaskState.NEW, "Description 1", Duration.ofHours(1), LocalDateTime.now());

        Assertions.assertTrue(epic1.getId() == epic2.getId(), "Задачи должны быть одинаковыми по идентификатору");
    }

    @Test
    void testCalculateEpicStatus_AllNew() {
        Epic epic = new Epic(1, TaskType.EPIC, "Epic Title", TaskState.NEW, "Epic Description", Duration.ZERO, LocalDateTime.now());
        epic.addNewTask(new Subtask(1, TaskType.SUBTASK, "Subtask 1", TaskState.NEW, "Description 1", Duration.ofHours(1), LocalDateTime.now(), 1));
        epic.addNewTask(new Subtask(2, TaskType.SUBTASK, "Subtask 2", TaskState.NEW, "Description 2", Duration.ofHours(2), LocalDateTime.now(), 1));
        epic.addNewTask(new Subtask(3, TaskType.SUBTASK, "Subtask 3", TaskState.NEW, "Description 3", Duration.ofHours(3), LocalDateTime.now(), 1));
        assertEquals(TaskState.NEW, epic.getState());
    }

    @Test
    void testCalculateEpicStatus_AllDone() {
        Epic epic = new Epic(0, TaskType.EPIC, "Epic Title", TaskState.NEW, "Epic Description", Duration.ZERO, LocalDateTime.now());
        epic.addNewTask(new Subtask(0, TaskType.SUBTASK, "Subtask 1", TaskState.DONE, "Description 1", Duration.ofHours(1), LocalDateTime.now(), 0));
        epic.addNewTask(new Subtask(1, TaskType.SUBTASK, "Subtask 2", TaskState.DONE, "Description 2", Duration.ofHours(2), LocalDateTime.now(), 0));
        epic.addNewTask(new Subtask(2, TaskType.SUBTASK, "Subtask 3", TaskState.DONE, "Description 3", Duration.ofHours(3), LocalDateTime.now(), 0));
        assertEquals(TaskState.DONE, epic.getState());
    }

    @Test
    void testCalculateEpicStatus_NewAndDone() {
        Epic epic = new Epic(1, TaskType.EPIC, "Epic Title", TaskState.NEW, "Epic Description", Duration.ZERO, LocalDateTime.now());
        epic.addNewTask(new Subtask(0, TaskType.SUBTASK, "Subtask 1", TaskState.NEW, "Description 1", Duration.ofHours(1), LocalDateTime.now(), 1));
        epic.addNewTask(new Subtask(1, TaskType.SUBTASK, "Subtask 2", TaskState.DONE, "Description 2", Duration.ofHours(2), LocalDateTime.now(), 1));
        epic.addNewTask(new Subtask(2, TaskType.SUBTASK, "Subtask 3", TaskState.NEW, "Description 3", Duration.ofHours(3), LocalDateTime.now(), 1));
        assertEquals(TaskState.IN_PROGRESS, epic.getState());
    }

    @Test
    void testCalculateEpicStatus_InProgress() {
        Epic epic = new Epic(1, TaskType.EPIC, "Epic Title", TaskState.NEW, "Epic Description", Duration.ZERO, LocalDateTime.now());
        epic.addNewTask(new Subtask(0, TaskType.SUBTASK, "Subtask 1", TaskState.IN_PROGRESS, "Description 1", Duration.ofHours(1), LocalDateTime.now(), 1));
        epic.addNewTask(new Subtask(1, TaskType.SUBTASK, "Subtask 2", TaskState.DONE, "Description 2", Duration.ofHours(2), LocalDateTime.now(), 1));
        epic.addNewTask(new Subtask(2, TaskType.SUBTASK, "Subtask 3", TaskState.NEW, "Description 3", Duration.ofHours(3), LocalDateTime.now(), 1));
        assertEquals(TaskState.IN_PROGRESS, epic.getState());
    }

    @Test
    void testCalculateDuration_EmptySubtasks() {
        Epic epic = new Epic(0, TaskType.EPIC, "Epic 1", TaskState.NEW, "Description 1", null, null);
        assertEquals(null, epic.getDuration());
    }

    @Test
    void testCalculateDuration_MultipleSubtasks() {
        Epic epic = new Epic(0, TaskType.EPIC, "Epic 1", TaskState.NEW, "Description 1", null, null);
        epic.addNewTask(new Subtask(1, TaskType.SUBTASK, "Subtask 1", TaskState.NEW, "Description 1", Duration.ofHours(1), LocalDateTime.now(), 0));
        epic.addNewTask(new Subtask(2, TaskType.SUBTASK, "Subtask 2", TaskState.NEW, "Description 2", Duration.ofHours(2), LocalDateTime.now(), 0));


        assertEquals(Duration.ofHours(3), epic.getTotalDuration());
    }

    @Test
    void testCalculateStartTime_EmptySubtasks() {
        Epic epic = new Epic(0, TaskType.EPIC, "Epic 1", TaskState.NEW, "Description 1", null, null);
        assertNull(epic.getEarliestStartTime());
    }

    @Test
    void testCalculateStartTime_MultipleSubtasks() {
        Epic epic = new Epic(0, TaskType.EPIC, "Epic 1", TaskState.NEW, "Description 1", null, null);
        LocalDateTime startTime1 = LocalDateTime.of(2023, 12, 1, 10, 0, 0);
        LocalDateTime startTime2 = LocalDateTime.of(2023, 12, 1, 11, 0, 0);
        LocalDateTime time = LocalDateTime.of(2023, 12, 1, 13, 0, 0);

        epic.addNewTask(new Subtask(1, TaskType.SUBTASK, "Subtask 1", TaskState.NEW, "Description 1", Duration.ofHours(1), startTime1, 0));
        epic.addNewTask(new Subtask(2, TaskType.SUBTASK, "Subtask 2", TaskState.NEW, "Description 2", Duration.ofHours(2), startTime2, 0));

        assertEquals(time, epic.getEndTime());
    }

    @Test
    void testCalculateEndTime_EmptySubtasks() {
        Epic epic = new Epic(0, TaskType.EPIC, "Epic 1", TaskState.NEW, "Description 1", null, null);
        assertNull(epic.getEndTime());
    }

    @Test
    void testCalculateEndTime_MultipleSubtasks() {
        Epic epic = new Epic(0, TaskType.EPIC, "Epic 1", TaskState.NEW, "Description 1", null, null);
        LocalDateTime startTime1 = LocalDateTime.of(2023, 12, 1, 10, 0, 0);
        LocalDateTime startTime2 = LocalDateTime.of(2023, 12, 1, 15, 0, 0);
        epic.addNewTask(new Subtask(1, TaskType.SUBTASK, "Subtask 1", TaskState.NEW, "Description 1", Duration.ofHours(1), startTime1, 0));
        epic.addNewTask(new Subtask(2, TaskType.SUBTASK, "Subtask 2", TaskState.NEW, "Description 2", Duration.ofHours(2), startTime2, 0));

        assertEquals(startTime2.plusHours(2), epic.getEndTime());
    }
}