package utils;

import history.InMemoryHistoryManager;
import interfaces.*;
import memory.InMemoryTaskManager;

public final class Manager {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
