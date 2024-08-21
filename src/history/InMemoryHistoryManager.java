package history;

import tasks.Task;
import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements interfaces.HistoryManager {
    private final List<Task> viewedTaskHistory = new ArrayList<>();

    @Override
    public void add(Task task) {
        viewedTaskHistory.add(task);
        if (viewedTaskHistory.size() > 10) {
            viewedTaskHistory.removeFirst();
        }
    }

    @Override
    public List<Task> getViewedTaskHistory() {
        return viewedTaskHistory;
    }

}

