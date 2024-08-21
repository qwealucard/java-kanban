package history;

import tasks.Task;
import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements interface_class.HistoryManager {
    private final static List<Task> viewedTaskHistory = new ArrayList<>();

    @Override
    public void add(Task task) {
        viewedTaskHistory.add(task);
    }

    @Override
    public List<Task> getViewedTaskHistory() {
        return new ArrayList<>(viewedTaskHistory);
    }

}

