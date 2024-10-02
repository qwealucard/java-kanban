package SavedTask;

import interfaces.TaskManager;
import memory.InMemoryTaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {

    private File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }
//    InMemoryTaskManager manager = new InMemoryTaskManager();

    public void save() {

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            for(Task task : getTasks().values()) {
                writer.write(task.toString());
                writer.newLine();
            }
            for(Epic epic : getEpics().values()) {
                writer.write(epic.toString());
                writer.newLine();
            }
            for(Subtask subtask : getSubtasks().values()) {
                writer.write(subtask.toString());
                writer.newLine();
            }
            System.out.println("Tasks saved to file successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while saving the tasks to file.");
            e.printStackTrace();
        }
    }
    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager taskManager = new FileBackedTaskManager(file);

        try {
            List<String> lines = Files.readAllLines(file.toPath());
            for (int i = 0; i < lines.size() - 1; i++) {
                String line = lines.get(i);
                Task task = Task.fromString(line);
                System.out.println("Загружена задача: " + task);

                if (task instanceof Epic) {
                    taskManager.addNewTask((Epic) task);
                } else if (task instanceof Subtask) {
                    taskManager.addNewTask((Subtask) task);
                } else {
                    taskManager.addNewTask(task);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return taskManager;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void addNewTask(Task task) {
        super.addNewTask(task);
        save();
    }

    @Override
    public void addNewTask(Epic epic) {
        super.addNewTask(epic);
        save();
    }

    @Override
    public void addNewTask(Subtask subtask) {
        super.addNewTask(subtask);
        save();
    }

    @Override
    public void updateTask(Epic epic) {
        super.updateTask(epic);
        save();
    }

    @Override
    public void updateTask(Subtask subtask) {
        super.updateTask(subtask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }
}
