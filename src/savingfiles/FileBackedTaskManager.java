package savingfiles;

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
            System.out.println("Ошибка при создании заголовка " + e.getMessage());
        }
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Task task : tasks.values()) {
                writer.write(task.toString());

                writer.newLine();
            }
            for (Epic epic : epics.values()) {
                writer.write(epic.toString());

                writer.newLine();
            }
            for (Subtask subtask : subtasks.values()) {
                writer.write(subtask.toString());

                writer.newLine();
            }
            System.out.println("Задачи сохранены в файл");
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
                    taskManager.epics.put(task.getId(), (Epic) task);
                } else if (task instanceof Subtask) {
                    Subtask subtask = (Subtask) task;
                    Epic parentEpic = taskManager.epics.get(subtask.getParentId());
                    if (parentEpic != null) {
                        parentEpic.addSubtask(subtask);
                    } else {
                        throw new RuntimeException("Эпик данного сабтаска не найден: " + subtask.getId());
                    }
                    taskManager.subtasks.put(task.getId(), subtask);
                } else {
                    taskManager.tasks.put(task.getId(), task);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("При сохранении задач в файл произошла ошибка", e);
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
    public int addNewTask(Task task) {
        super.addNewTask(task);
        save();
        return task.getId();
    }

    @Override
    public int addNewTask(Epic epic) {
        super.addNewTask(epic);
        save();
        return epic.getId();
    }

    @Override
    public int addNewTask(Subtask subtask) {
        super.addNewTask(subtask);
        save();
        return subtask.getId();
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

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    public void saveToFile() {
        save();
    }
}
