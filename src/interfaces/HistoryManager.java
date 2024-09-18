package interfaces;

import tasks.Task;

public interface HistoryManager {
    void linkLast(Task task);

    void addToHistory(Task task);

    void removeHistory(int id);
}
