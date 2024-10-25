package handlers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import interfaces.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {

    private final Gson gson;

    public EpicHandler(TaskManager taskManager, Gson gson) {
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
                } catch (NotFoundException e) {
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
        if (Pattern.matches("^/epics$", path)) {
            try {
                List<Task> epics = taskManager.getAllEpics();
                sendText(exchange, gson.toJson(epics), 200);
            } catch (Exception e) {
                sendInternalServerError(exchange);
                throw new IOException("Ошибка при получении всех эпиков", e);
            }
        } else if (Pattern.matches("^/epics/\\d+$", path)) {
            try {
                int id = Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));
                Epic epic = taskManager.getEpicByID(id);
                if (epic != null) {
                    sendText(exchange, gson.toJson(epic), 200);
                } else {
                    sendNotFound(exchange);
                }
            } catch (NumberFormatException e) {
                sendBadRequest(exchange);
                throw new IllegalArgumentException("Неверный формат ID эпика", e);
            } catch (Exception e) {
                sendInternalServerError(exchange);
                throw new IOException("Ошибка при получении эпика по ID", e);
            }
        } else if (Pattern.matches("^/epics/\\d+/subtasks$", path)) {
            try {
                int epicId = Integer.parseInt(path.substring(path.lastIndexOf('/') - 1, path.lastIndexOf('/')));
                Epic epic = taskManager.getEpicByID(epicId);
                if (epic != null) {
                    List<Subtask> subtasks = taskManager.getSubtasksByEpic(epic);
                    sendText(exchange, gson.toJson(subtasks), 200);
                } else {
                    sendNotFound(exchange);
                }
            } catch (NumberFormatException e) {
                sendBadRequest(exchange);
                throw new IllegalArgumentException("Неверный формат ID эпика", e);
            } catch (Exception e) {
                sendInternalServerError(exchange);
                throw new IOException("Ошибка при получении сабтасков по эпику", e);
            }
        } else {
            sendNotFound(exchange);
            throw new NotFoundException("Ресурс не найден");
        }
    }

    private void handlePostRequests(String path, HttpExchange exchange) throws IOException, NotFoundException {
        if (Pattern.matches("^/epics$", path)) {
            try {
                String requestBody = readBody(exchange);
                Epic epic = gson.fromJson(requestBody, Epic.class);
                taskManager.addNewTask(epic);
                sendText(exchange, gson.toJson(epic), 201);

            } catch (JsonSyntaxException e) {
                sendBadRequest(exchange);
                throw new IllegalArgumentException("Неверный формат JSON", e);
            } catch (IOException e) {
                sendHasInteractions(exchange);
                throw new IOException("Ошибка при чтении тела запроса", e);
            } catch (Exception e) {
                sendInternalServerError(exchange);
                throw new IOException("Ошибка при создании эпика", e);
            }
        } else {
            sendNotFound(exchange);
            throw new NotFoundException("Ресурс не найден");
        }
    }

    private void handleDeleteRequests(String path, HttpExchange exchange) throws IOException, NotFoundException {
        if (Pattern.matches("^/epics/\\d+$", path)) {
            try {
                int id = Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));
                taskManager.deleteEpicById(id);
                exchange.sendResponseHeaders(200, 0);
                exchange.getResponseBody().close();
            } catch (NumberFormatException e) {
                sendBadRequest(exchange);
                throw new IllegalArgumentException("Неверный формат ID эпика", e);
            } catch (Exception e) {
                sendInternalServerError(exchange);
                throw new IOException("Ошибка при удалении эпика", e);
            }
        } else {
            sendNotFound(exchange);
            throw new NotFoundException("Ресурс не найден");

        }
    }
}


