package SavedTask;

import interfaces.TaskManager;
import memory.InMemoryTaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {

    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static void createFileHeader(File file) { //Использовать при создании конкретного файла
        try {
            List<String> header = new ArrayList<>();
            header.add("id,type,name,status,description,epic");
            Files.write(file.toPath(), header);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
            System.out.println("Задача сохранена в файл");
        } catch (IOException e) {
            System.out.println("При сохранении задачи в файл произошла ошибка");
            e.printStackTrace();
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager taskManager = new FileBackedTaskManager(file);

        try {
            List<String> lines = Files.readAllLines(file.toPath());
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                Task task = Task.fromString(line);
                if (task instanceof Epic) {
                    taskManager.getEpics().put(task.getId(),(Epic) task);
                } else if (task instanceof Subtask) {
                    taskManager.getSubtasks().put(task.getId(), (Subtask) task);
                } else {
                    taskManager.getTasks().put(task.getId(),task);
                }
            }
        } catch (IOException e) {
            System.out.println("При загрузке задач произошла ошибка");
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
