package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import interfaces.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {

    private final Gson gson;

    public HistoryHandler(TaskManager taskManager, Gson gson) {
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
            default:
                sendNotFound(exchange);
                break;
        }
    }

    private void handleGetRequests(String path, HttpExchange exchange) throws IOException, NotFoundException {
        if (Pattern.matches("^/history$", path)) {
            try {
                List<Task> tasks = taskManager.getHistory();
                sendText(exchange, gson.toJson(tasks), 200);
            } catch (Exception e) {
                sendInternalServerError(exchange);
                throw new IOException("Ошибка при получении истории задач", e);
            }
        } else {
            sendNotFound(exchange);
            throw new NotFoundException("Ресурс не найден");
        }
    }
}
