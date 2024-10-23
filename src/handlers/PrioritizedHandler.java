package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import interfaces.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.List;

import static main.HttpTaskServer.PRIORITIZED_PATH;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    TaskManager taskManager;

    public PrioritizedHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        if (requestMethod.equals("GET") && path.equals(PRIORITIZED_PATH)) {
            try {
                List<Task> tasks = taskManager.getPrioritizedTasks();
                Gson gson = new Gson();
                String jsonResponse = gson.toJson(tasks);
                sendText(exchange, jsonResponse);
            } catch (IOException e) {
                sendNotFound(exchange);
            }
        } else {
            exchange.sendResponseHeaders(400, 0);
            exchange.getResponseBody().close();
        }
    }
}
