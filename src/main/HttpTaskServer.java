package main;

import customtypeadapter.DurationTypeAdapter;
import customtypeadapter.LocalDateTimeAdapter;
import serializeanddeserialize.TaskDeserializer;
import serializeanddeserialize.TaskSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import handlers.*;
import interfaces.TaskManager;
import tasks.Task;
import utils.Manager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private final int port;
    private final String tasksPath;
    private final String subtasksPath;
    private final String epicsPath;
    private final String historyPath;
    private final String prioritizedPath;

    private final Gson gson;
    private final TaskManager taskManager;

    private HttpServer server;

    public HttpTaskServer(int port,
                          String tasksPath,
                          String subtasksPath,
                          String epicsPath,
                          String historyPath,
                          String prioritizedPath,
                          TaskManager taskManager) {
        this.port = port;
        this.tasksPath = tasksPath;
        this.subtasksPath = subtasksPath;
        this.epicsPath = epicsPath;
        this.historyPath = historyPath;
        this.prioritizedPath = prioritizedPath;
        this.taskManager = taskManager;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Task.class, new TaskSerializer())
                .registerTypeAdapter(Task.class, new TaskDeserializer())
                .create();
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext(tasksPath, new TaskHandler(taskManager, gson));
        server.createContext(subtasksPath, new SubtaskHandler(taskManager, gson));
        server.createContext(epicsPath, new EpicHandler(taskManager, gson));
        server.createContext(historyPath, new HistoryHandler(taskManager, gson));
        server.createContext(prioritizedPath, new PrioritizedHandler(taskManager, gson));

        server.start();
        System.out.println("Сервер запущен на порту " + port);
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
        }
        System.out.println("Сервер остановлен.");
    }

    public static void main(String[] args) throws Exception {
        TaskManager taskManager = Manager.getDefault();
        HttpTaskServer server = new HttpTaskServer(
                8080,
                "/tasks",
                "/subtasks",
                "/epics",
                "/history",
                "/prioritized",
                taskManager
        );
        server.start();
    }
}
