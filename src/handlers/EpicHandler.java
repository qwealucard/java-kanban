package handlers;

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

import static main.HttpTaskServer.gson;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {

    public EpicHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        System.out.println("Received request: " + requestMethod + " " + path);

        switch (requestMethod) {
            case "GET":
                handleGetRequests(path, exchange);
                break;
            case "POST":
                handlePostRequests(path, exchange);
                break;
            case "DELETE":
                handleDeleteRequests(path, exchange);
                break;
            default:
                sendNotFound(exchange);
                break;
        }
    }

    private void handleGetRequests(String path, HttpExchange exchange) throws IOException {
        if (Pattern.matches("^/epics$", path)) {
            try {
                List<Task> epics = taskManager.getAllEpics();
                sendText(exchange, gson.toJson(epics), 200);
            } catch (Exception e) {
                e.printStackTrace();
                sendInternalServerError(exchange);
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
                e.printStackTrace();
                sendBadRequest(exchange);
            } catch (Exception e) {
                e.printStackTrace();
                sendInternalServerError(exchange);
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
                e.printStackTrace();
                sendBadRequest(exchange);
            } catch (Exception e) {
                e.printStackTrace();
                sendInternalServerError(exchange);
            }
        } else {
            sendNotFound(exchange);
        }
    }

    private void handlePostRequests(String path, HttpExchange exchange) throws IOException {
        if (Pattern.matches("^/epics$", path)) {
            try {
                String requestBody = readBody(exchange);
                Epic epic = gson.fromJson(requestBody, Epic.class);
                taskManager.addNewTask(epic);
                sendText(exchange, gson.toJson(epic), 201);

            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                sendBadRequest(exchange);
            } catch (IOException e) {
                e.printStackTrace();
                sendHasInteractions(exchange);
            } catch (Exception e) {
                e.printStackTrace();
                sendInternalServerError(exchange);
            }
        }
    }

    private void handleDeleteRequests(String path, HttpExchange exchange) throws IOException {
        if (Pattern.matches("^/epics/\\d+$", path)) {
            try {
                int id = Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));
                taskManager.deleteEpicById(id);
                exchange.sendResponseHeaders(200, 0);
                exchange.getResponseBody().close();
            } catch (NumberFormatException e) {
                e.printStackTrace();
                sendBadRequest(exchange);
            } catch (Exception e) {
                e.printStackTrace();
                sendInternalServerError(exchange);
            }
        } else {
            sendNotFound(exchange);
        }
    }
}


