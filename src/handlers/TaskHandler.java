package handlers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import interfaces.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {

    private final Gson gson;

    public TaskHandler(TaskManager taskManager, Gson gson) {
        super(taskManager);
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        System.out.println("Received request: " + requestMethod + " " + path);

        switch (requestMethod) {
            case "GET":
                try {
                    handleGetRequests(path, exchange);
                } catch (NotFoundException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "POST":
                try {
                    handlePostRequests(path, exchange);
                } catch (NotFoundException | TaskConflictException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "DELETE":
                try {
                    handleDeleteRequests(path, exchange);
                } catch (NotFoundException e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                sendNotFound(exchange);
                break;
        }
    }

    private void handleGetRequests(String path, HttpExchange exchange) throws IOException, NotFoundException {
        if (Pattern.matches("^/tasks$", path)) {
            try {
                List<Task> tasks = taskManager.getAllTasks();
                sendText(exchange, gson.toJson(tasks), 200);
            } catch (Exception e) {
                sendInternalServerError(exchange);
                throw new IOException("Ошибка при получении всех задач", e);
            }
        } else if (Pattern.matches("^/tasks/\\d+$", path)) {
            try {
                int id = Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));
                Task task = taskManager.getTaskByID(id);
                if (task != null) {
                    sendText(exchange, gson.toJson(task), 200);
                } else {
                    sendNotFound(exchange);
                }
            } catch (NumberFormatException e) {
                sendBadRequest(exchange);
                throw new IllegalArgumentException("Неверный формат ID задачи", e);
            } catch (Exception e) {
                sendInternalServerError(exchange);
                throw new IOException("Ошибка при получении задачи по ID", e);
            }
        } else {
            sendNotFound(exchange);
            throw new NotFoundException("Ресурс не найден");
        }
    }

    private void handlePostRequests(String path, HttpExchange exchange) throws IOException, NotFoundException, TaskConflictException {
        if (Pattern.matches("^/tasks$", path)) {
            try {
                String requestBody = readBody(exchange);
                Task newTask = gson.fromJson(requestBody, Task.class);
                taskManager.addNewTask(newTask);
                sendText(exchange, gson.toJson(newTask), 201);
            } catch (JsonSyntaxException e) {
                sendBadRequest(exchange);
                throw new IllegalArgumentException("Неверный формат JSON", e);
            } catch (Exception e) {
                if (e.getCause() instanceof TaskConflictException) {
                    sendHasInteractions(exchange);
                    throw new TaskConflictException("Пересечение задач по времени");
                } else {
                    sendInternalServerError(exchange);
                    throw new IOException("Ошибка при создании таска", e);
                }
            }

        } else if (Pattern.matches("^/tasks/\\d+$", path)) {
            try {
                String requestBody = readBody(exchange);
                Task updatedTask = gson.fromJson(requestBody, Task.class);
                taskManager.updateTask(updatedTask);
                sendText(exchange, gson.toJson(updatedTask), 201);
            } catch (NumberFormatException e) {
                sendNotFound(exchange);
                throw new IllegalArgumentException("Неверный формат ID задачи", e);
            } catch (Exception e) {
                if (e.getCause() instanceof TaskConflictException) {
                    sendHasInteractions(exchange);
                    throw new TaskConflictException("Пересечение задач по времени");
                } else {
                    sendInternalServerError(exchange);
                    throw new IOException("Ошибка при создании таска", e);
                }
            }
        } else {
            sendNotFound(exchange);
            throw new NotFoundException("Ресурс не найден");
        }
    }

    private void handleDeleteRequests(String path, HttpExchange exchange) throws IOException, NotFoundException {
        if (Pattern.matches("^/tasks/\\d+$", path)) {
            try {
                int id = Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));
                taskManager.deleteTaskById(id);
                exchange.sendResponseHeaders(200, 0);
                exchange.getResponseBody().close();
            } catch (NumberFormatException e) {
                sendBadRequest(exchange);
                throw new IllegalArgumentException("Неверный формат ID задачи", e);
            } catch (Exception e) {
                sendInternalServerError(exchange);
                throw new IOException("Ошибка при удалении задачи", e);

            }
        } else {
            sendNotFound(exchange);
            throw new NotFoundException("Ресурс не найден");

        }
    }
}
