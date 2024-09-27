package test;

import interfaces.*;
import org.junit.jupiter.api.Test;
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
