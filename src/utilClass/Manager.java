package utilClass;

import history.InMemoryHistoryManager;
import memory.InMemoryTaskManager;

public final class Manager {
    public static InMemoryTaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public InMemoryHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
