package handlers;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import interfaces.TaskManager;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import static main.HttpTaskServer.gson;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {

    public SubtaskHandler(TaskManager taskManager) {
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
        if (path.equals("/subtasks")) {
            try {
                List<Task> subtasks = taskManager.getAllSubtasks();
                sendText(exchange, gson.toJson(subtasks));
                exchange.sendResponseHeaders(200, 0);
                exchange.getResponseBody().close();
            } catch (Exception e) {
                e.printStackTrace();
                sendInternalServerError(exchange);
            }
        } else if (Pattern.matches("^/subtasks/\\d+$", path)) {
            try {
                int id = Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));
                Subtask subtask = taskManager.getSubtaskByID(id);
                if (subtask != null) {
                    sendText(exchange, gson.toJson(subtask));
                    exchange.sendResponseHeaders(200, 0);
                    exchange.getResponseBody().close();
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
        if (Pattern.matches("^/subtasks$", path)) {
            try {
                String requestBody = readBody(exchange);
                Subtask subtask = gson.fromJson(requestBody, Subtask.class);
                taskManager.addNewTask(subtask);
                sendText(exchange, gson.toJson(subtask));
                exchange.sendResponseHeaders(201, 0);
                exchange.getResponseBody().close();

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
        } else if (Pattern.matches("^/subtasks/\\d+$", path)) {
            try {
                String requestBody = readBody(exchange);
                Subtask updatedTask = gson.fromJson(requestBody, Subtask.class);
                taskManager.updateTask(updatedTask);
                sendText(exchange, gson.toJson(updatedTask));
                exchange.sendResponseHeaders(201, 0);
                exchange.getResponseBody().close();
            } catch (NumberFormatException e) {
                e.printStackTrace();
                sendNotFound(exchange);
            } catch (IOException e) {
                e.printStackTrace();
                sendHasInteractions(exchange);
            } catch (Exception e) {
                e.printStackTrace();
                sendInternalServerError(exchange);
            }
        } else {
            sendNotFound(exchange);
        }
    }

    private void handleDeleteRequests(String path, HttpExchange exchange) throws IOException {
        if (Pattern.matches("^/subtasks/\\d+$", path)) {
            try {
                int id = Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));
                taskManager.deleteSubtaskById(id);
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


