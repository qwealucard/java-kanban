package saving_files;

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

    public void save() throws ManagerSaveException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Task task : getAllTasks()) {
                if (task instanceof Epic) {
                    writer.write(task.toString());
                    writer.newLine();
                } else if (task instanceof Subtask) {
                    writer.write(task.toString());
                    writer.newLine();
                } else {
                    writer.write(task.toString());
                    writer.newLine();
                }
            }
            System.out.println("Задача сохранена в файл");
        } catch (IOException e) {
            throw new ManagerSaveException("При сохранении задач в файл произошла ошибка", e);
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) throws ManagerSaveException {
        FileBackedTaskManager taskManager = new FileBackedTaskManager(file);

        try {
            List<String> lines = Files.readAllLines(file.toPath());
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                Task task = Task.fromString(line);
                if (task instanceof Epic) {
                    taskManager.epics.put(task.getId(),(Epic) task);
                } else if (task instanceof Subtask) {
                    taskManager.subtasks.put(task.getId(), (Subtask) task);
                } else {
                    taskManager.tasks.put(task.getId(),task);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("При сохранении задач в файл произошла ошибка", e);
        }
        return taskManager;
    }

    @Override
    public void deleteAllTasks() throws ManagerSaveException {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllSubtasks() throws ManagerSaveException {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteAllEpics() throws ManagerSaveException {
        super.deleteAllEpics();
        save();
    }

    @Override
    public int addNewTask(Task task) throws ManagerSaveException {
        super.addNewTask(task);
        save();
        return task.getId();
    }

    @Override
    public int addNewTask(Epic epic) throws ManagerSaveException {
        super.addNewTask(epic);
        save();
        return epic.getId();
    }

    @Override
    public int addNewTask(Subtask subtask) throws ManagerSaveException {
        super.addNewTask(subtask);
        save();
        return subtask.getId();
    }

    @Override
    public void updateTask(Epic epic) throws ManagerSaveException {
        super.updateTask(epic);
        save();
    }

    @Override
    public void updateTask(Subtask subtask) throws ManagerSaveException {
        super.updateTask(subtask);
        save();
    }

    @Override
    public void updateTask(Task task) throws ManagerSaveException {
        super.updateTask(task);
        save();
    }

    @Override
    public void deleteTaskById(int id) throws ManagerSaveException {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) throws ManagerSaveException {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) throws ManagerSaveException {
        super.deleteSubtaskById(id);
        save();
    }
}
