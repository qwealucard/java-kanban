package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import interfaces.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.util.List;

import static main.HttpTaskServer.EPICS_PATH;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {

    public EpicHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        if (requestMethod.equals("GET") && path.equals(EPICS_PATH)) {
            try {
                List<Task> epics = taskManager.getAllEpics();
                Gson gson = new Gson();
                String jsonResponse = gson.toJson(epics);
                sendText(exchange, jsonResponse);
            } catch (IOException e) {
                sendNotFound(exchange);
            }
        } else if (requestMethod.equals("GET") && path.matches(EPICS_PATH + "/\\d+")) {

            int id = Integer.parseInt(path.substring(EPICS_PATH.length() + 1));
            try {
                Epic epic = taskManager.getEpicByID(id);

                Gson gson = new Gson();
                String jsonResponse = gson.toJson(epic);
                sendText(exchange, jsonResponse);
            } catch (IOException e) {
                sendNotFound(exchange);
            }
        } else if (requestMethod.equals("POST") && path.equals(EPICS_PATH)) {

            String requestBody = readBody(exchange);
            Gson gson = new Gson();
            Epic newEpic = gson.fromJson(requestBody, Epic.class);
            try {
                taskManager.addNewTask(newEpic);

                exchange.sendResponseHeaders(201, 0);
                exchange.getResponseBody().close();
            } catch (IOException e) {

                sendHasInteractions(exchange);
            }
        } else if (requestMethod.equals("DELETE") && path.matches(EPICS_PATH + "/\\d+")) {
            int id = Integer.parseInt(path.substring(EPICS_PATH.length() + 1));

            try {
                taskManager.deleteEpicById(id);
                exchange.sendResponseHeaders(200, 0);
                exchange.getResponseBody().close();
            } catch (IOException e) {
                sendNotFound(exchange);
            }
        }else if (requestMethod.equals("GET") && path.matches(EPICS_PATH + "/\\d+/subtasks")) {

                int epicId = Integer.parseInt(path.substring(EPICS_PATH.length() + 1, path.lastIndexOf("/")));
                try {
                    List<Subtask> subtasks = taskManager.getSubtasksByEpic(taskManager.getEpicByID(epicId));


                    Gson gson = new Gson();
                    String jsonResponse = gson.toJson(subtasks);
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


