package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import interfaces.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.List;

import static main.HttpTaskServer.gson;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {

    public TaskHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;

    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        System.out.println("Received request: " + requestMethod + " " + path);
        System.out.println(taskManager.getAllTasks());

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
        if (path.equals("/tasks")) {
            List<Task> tasks = taskManager.getAllTasks();
            sendText(exchange, gson.toJson(tasks));
        } else if (path.matches("/tasks/\\d+")) {
            int id = Integer.parseInt(path.substring("/tasks/".length()));
            Task task = taskManager.getTaskByID(id);
            sendText(exchange, String.valueOf(task));
            if (task != null) {
                sendText(exchange, gson.toJson(task));
            } else {
                sendNotFound(exchange);
            }
        } else {
            sendNotFound(exchange);
        }
    }

    private void handlePostRequests(String path, HttpExchange exchange) throws IOException {
        if (path.equals("/tasks")) {
            try {
                String requestBody = readBody(exchange);
                Task newTask = gson.fromJson(requestBody, Task.class);
                taskManager.addNewTask(newTask);
                sendText(exchange, gson.toJson(newTask));
                exchange.sendResponseHeaders(201, 0);
                exchange.getResponseBody().close();

            } catch (IOException e) {
                e.printStackTrace();
                sendHasInteractions(exchange);
            } catch (Exception e) {
                e.printStackTrace();
                sendNotFound(exchange);
            }
        } else {
            sendNotFound(exchange);
        }
    }

    private void handleDeleteRequests(String path, HttpExchange exchange) throws IOException {
        if (path.matches("/tasks/\\d+")) {
            int id = Integer.parseInt(path.substring("/tasks/".length()));
            try {
                taskManager.deleteTaskById(id);
                exchange.sendResponseHeaders(200, 0);
            } catch (IOException e) {
                sendNotFound(exchange);
            }
        } else {
            sendNotFound(exchange);
        }
    }
}