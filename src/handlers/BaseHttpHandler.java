package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import interfaces.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class BaseHttpHandler implements HttpHandler {

    protected TaskManager taskManager;

    public BaseHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendNotFound(HttpExchange h) throws IOException {
        h.sendResponseHeaders(404, 0);
        h.getResponseBody().close();
    }

    protected void sendHasInteractions(HttpExchange h) throws IOException {
        h.sendResponseHeaders(406, 0);
        h.getResponseBody().close();
    }

    protected void sendBadRequest(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(400, 0);
        exchange.getResponseBody().close();
    }

    protected void sendInternalServerError(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(500, 0);
        exchange.getResponseBody().close();
    }

    protected String readBody(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }
}
