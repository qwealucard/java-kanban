package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import interfaces.TaskManager;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.util.List;

import static main.HttpTaskServer.SUBTASKS_PATH;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {

    public SubtaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        if (requestMethod.equals("GET") && path.equals(SUBTASKS_PATH)) {
            try {
                List<Task> subtasks = taskManager.getAllSubtasks();
                Gson gson = new Gson();
                String jsonResponse = gson.toJson(subtasks);
                sendText(exchange, jsonResponse);
            } catch (IOException e){
                sendNotFound(exchange);
            }
        } else if (requestMethod.equals("GET") && path.matches(SUBTASKS_PATH + "/\\d+")) {

            int id = Integer.parseInt(path.substring(SUBTASKS_PATH.length() + 1));
            try {
                Subtask subtask = taskManager.getSubtaskByID(id);

                Gson gson = new Gson();
                String jsonResponse = gson.toJson(subtask);
                sendText(exchange, jsonResponse);
            } catch(IOException e) {
                sendNotFound(exchange);
            }
        }else if (requestMethod.equals("POST") && path.equals(SUBTASKS_PATH)) {

            String requestBody = readBody(exchange);
            Gson gson = new Gson();
            Subtask newSubtask = gson.fromJson(requestBody, Subtask.class);
            try {
                taskManager.addNewTask(newSubtask);

                exchange.sendResponseHeaders(201, 0);
                exchange.getResponseBody().close();
            }catch (IOException e){

                sendHasInteractions(exchange);
            }
        }else if (requestMethod.equals("DELETE") && path.matches(SUBTASKS_PATH + "/\\d+")) {
            int id = Integer.parseInt(path.substring(SUBTASKS_PATH.length() + 1));

            try {
                taskManager.deleteSubtaskById(id);
                exchange.sendResponseHeaders(200, 0);
                exchange.getResponseBody().close();
            } catch (IOException e) {
                sendNotFound(exchange);
            }
        } else {
            exchange.sendResponseHeaders(400, 0);
            exchange.getResponseBody().close();
        }
    }
}
