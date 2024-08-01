import java.util.ArrayList;

public class TaskManager {
    public ArrayList<Task> tasks = new ArrayList<>();

    private int currentId = 0;

    public ArrayList<Task> getAll() {
        return tasks;
    }

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> tmpTasks = new ArrayList<Task>();
        for (Task task : tasks) {
            if (!((task instanceof Epic) || (task instanceof Subtask))) {
                tmpTasks.add(task);
            }
        }
        return tmpTasks;
    }

    public ArrayList<Epic> getAllEpics() {
        ArrayList<Epic> tmpEpics = new ArrayList<Epic>();
        for (Task task : tasks) {
            if ((task instanceof Epic)) {
                tmpEpics.add((Epic) task);
            }
        }
        return tmpEpics;
    }

    public ArrayList<Subtask> getAllSubtasks() {
        ArrayList<Subtask> tmpSubtasks = new ArrayList<Subtask>();
        for (Task task : tasks) {
            if (task instanceof Subtask) {
                tmpSubtasks.add((Subtask) task);
            }
        }
        return tmpSubtasks;
    }

    public void clearAll() {
        tasks.clear();
    }

    public void deleteAllTasks() {
        tasks.removeIf(task -> !((task instanceof Epic) || (task instanceof Subtask)));
    }

    public void deleteAllEpics() {
        tasks.removeIf(task -> !(task instanceof Epic));
    }

    public void deleteAllSubtasks() {
        tasks.removeIf(task -> !(task instanceof Subtask));
    }

    public Task getTaskByIdOrReturnNull(int id) {
        for (Task task : tasks) {
            if (id == task.id) {
                return task;
            } else {
                System.out.println("Задачи с таким идентификатором нет");
            }
        }
        return null;
    }

    public void addNewTask(Task newTask) {
        newTask.id = currentId;
        tasks.add(newTask);
        currentId++;
    }

    public void updateTask(Task updatedTask) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).id == updatedTask.id) {
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
}
