package interfaces;

import history.InMemoryHistoryManager;
import tasks.Task;

import java.util.ArrayList;

import java.util.Map;

public interface HistoryManager {
    void linkLast(Task task);

    ArrayList<Task> getTasks();

    void removeNode(InMemoryHistoryManager.TaskNode node);

    void addToHistory(Task task);

    void removeHistory(int id);

    Map<Integer, InMemoryHistoryManager.TaskNode> getNodeMap();
}
