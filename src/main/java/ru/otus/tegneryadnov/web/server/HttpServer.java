package ru.otus.tegneryadnov.web.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.tegneryadnov.web.server.application.Storage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class HttpServer {
    private final static int PROCESSED_REQUESTS = 4;
    private final int port;
    private Dispatcher dispatcher;
    private static boolean serverSocketIsWorking;
    private static final Logger logger = LogManager.getLogger(HttpServer.class.getName());

    static {
        logger.info("debug=" + logger.isDebugEnabled());
    }

    public HttpServer(int port) {
        this.port = port;
    }

    public void start() {
        ExecutorService service = null;
        serverSocketIsWorking = true;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            service = Executors.newFixedThreadPool(PROCESSED_REQUESTS);
            logger.info("Сервер запущен на порту: " + port);
            this.dispatcher = new Dispatcher();
            logger.info("Диспетчер проинициализирован");
            Storage.init();
            while (serverSocketIsWorking) {
                Socket socket = serverSocket.accept();
                service.execute(() ->
                {
                    try {
                        byte[] buffer = new byte[8192];
                        int n = socket.getInputStream().read(buffer);
                        if (n > -1) {
                            String rawRequest = new String(buffer, 0, n);
                            HttpRequest request = new HttpRequest(rawRequest);
                            if (logger.isDebugEnabled()) {
                                logger.debug(request.getInfo(logger.isDebugEnabled()));
                            } else {
                                logger.info(request.getInfo(logger.isDebugEnabled()));
                            }
                            if (request.getUri().equalsIgnoreCase("/shutdown")) {
                                if (serverSocketIsWorking) {
                                    logger.info("Поступила команда SHUTDOWN. Сервер выключается...");
                                    synchronized (HttpServer.class) {
                                        serverSocketIsWorking = false;
                                    }
                                }
                                return;
                            }
                            dispatcher.execute(request, socket.getOutputStream());
                        }
                    } catch (IOException e) {
                        logger.error(e);
                        throw new RuntimeException(e);
                    } finally {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            logger.error("Ошибка закрытия сокета " + e);
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        } catch (IOException e) {
            logger.error(e);
        } finally {
            if (service != null) {
                service.shutdown();
                try {
                    boolean done = service.awaitTermination(5, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    logger.error("Ошибка закрытия пула потоков " + e);
                    throw new RuntimeException(e);
                }
                logger.info("Программа завершена");
            }
        }
    }
}
