package interfaces;

import tasks.Task;

import java.util.List;

public interface HistoryManager {

    void add(Task task);

    void removeHistory(int id);

    List<Task> getHistoryList();
}
