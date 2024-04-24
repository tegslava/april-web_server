package ru.otus.tegneryadnov.web.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
    private final static int PROCESSED_REQUESTS = 4;
    private final int port;
    private Dispatcher dispatcher;

    public HttpServer(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            ExecutorService service = Executors.newFixedThreadPool(PROCESSED_REQUESTS);
            System.out.println("Сервер запущен на порту: " + port);
            this.dispatcher = new Dispatcher();
            System.out.println("Диспетчер проинициализирован");
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                byte[] buffer = new byte[8192];
                int n = socket.getInputStream().read(buffer);
                if (n > -1) {
                    String rawRequest = new String(buffer, 0, n);
                    HttpRequest request = new HttpRequest(rawRequest);
                    request.info(true);
                    if (request.getUri().equalsIgnoreCase("/shutdown")) {
                        System.out.println("Поступила команда SHUTDOWN");
                        if (!socket.isClosed()) {
                            socket.close();
                        }
                        break;
                    }
                    service.execute(() ->
                    {
                        try {
                            dispatcher.execute(request, socket.getOutputStream());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } finally {
                            try {
                                socket.close();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                } else if (!socket.isClosed()) {
                    socket.close();
                }
            }
            service.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
