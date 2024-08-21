package interface_class;

import tasks.Task;
import java.util.List;
public interface HistoryManager {
    void add(Task task);

    List<Task> getViewedTaskHistory();
}
