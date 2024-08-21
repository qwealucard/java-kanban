package tests;

import history.InMemoryHistoryManager;
import memory.InMemoryTaskManager;
import org.junit.Test;
import utilClass.Manager;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ManagerTest {
    @Test
    public void testGetDefaultTaskManager() {
        InMemoryTaskManager taskManager = Manager.getDefault();
        assertNotNull(taskManager);
    }

    @Test
    public void testGetDefaultHistoryManager() {
        Manager manager = new Manager();
        InMemoryHistoryManager historyManager = manager.getDefaultHistory();
        assertNotNull(historyManager);
    }
}
