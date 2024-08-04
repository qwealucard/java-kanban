import java.util.ArrayList;
import java.util.List;

public class TaskManager {

    private List<Task> tasks = new ArrayList<>();

    private int currentId = 0;

    public List<Task> getAllTasks() {
        List<Task> tmpTasks = new ArrayList<Task>();
        for (Task task : tasks) {
            if (!((task instanceof Epic) || (task instanceof Subtask))) {
                tmpTasks.add(task);
            }
        }
        return tmpTasks;
    }

    public List<Epic> getAllEpics() {
        List<Epic> tmpEpics = new ArrayList<Epic>();
        for (Task task : tasks) {
            if ((task instanceof Epic)) {
                tmpEpics.add((Epic) task);
            }
        }
        return tmpEpics;
    }

    public List<Subtask> getAllSubtasks() {
        List<Subtask> tmpSubtasks = new ArrayList<Subtask>();
        for (Task task : tasks) {
            if (task instanceof Subtask) {
                tmpSubtasks.add((Subtask) task);
            }
        }
        return tmpSubtasks;
    }

    public void deleteAllTasks() {
        tasks.removeIf(task -> !((task instanceof Epic) || (task instanceof Subtask)));
    }

    public void deleteAllEpics() {
        tasks.removeIf(task -> (task instanceof Subtask));
        tasks.removeIf(task -> (task instanceof Epic));

    }

    public void deleteAllSubtasks() {
        tasks.removeIf(task -> {
            if (task instanceof Subtask) {
                ((Subtask) task).getParent().getSubtasks().remove(task);
                ((Subtask) task).updateState(TaskState.NEW);
                return true;
            }
            return false;
    });

    }

    public Task getTaskByIdOrReturnNull(int id) {
        for (Task task : tasks) {
            if (id == task.getId()) {
                return task;
            } else {
                System.out.println("Задачи с таким идентификатором нет");
            }
        }
        return null;
    }

    public void addNewTask(Task newTask) {
        newTask.setId(currentId);
        tasks.add(newTask);
        currentId++;
    }

    public void updateTask(Task updatedTask) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == updatedTask.getId()) {
                tasks.set(i, updatedTask);
                break;
            } else if (i + 1 == tasks.size()) {
                tasks.add(updatedTask);
            }
        }
    }

    public void deleteTaskById(int currentId) {
        tasks.removeIf(task -> task.id == currentId);
    }
    public List<Task> getTasks() {
        return tasks;
    }
}
