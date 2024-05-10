package ru.otus.tegneryadnov.web.server.application.processors;

import ru.otus.tegneryadnov.web.server.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class UnknownOperationRequestProcessor implements RequestProcessor {
    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        String response = "HTTP/1.1 400 Bad Request\r\nContent-Type: text/html\r\n\r\n<html><link rel=\"icon\" href=\"data:,\"><body><h1>UNKNOWN OPERATION REQUEST!!!</h1></body></html>";
        output.write(response.getBytes(StandardCharsets.UTF_8));
    }
}
