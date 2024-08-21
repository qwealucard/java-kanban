package tests;

import history.InMemoryHistoryManager;
import interfaces.*;
import memory.InMemoryTaskManager;
import org.junit.Test;
import utils.Manager;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ManagerTest {
    @Test
    public void testGetDefaultTaskManager() {
        TaskManager taskManager = Manager.getDefault();
        assertNotNull(taskManager);
    }

    @Test
    public void testGetDefaultHistoryManager() {
        Manager manager = new Manager();
        HistoryManager historyManager = manager.getDefaultHistory();
        assertNotNull(historyManager);
    }
}
