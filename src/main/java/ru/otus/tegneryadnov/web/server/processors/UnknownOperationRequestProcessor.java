package ru.otus.tegneryadnov.web.server.processors;

import ru.otus.tegneryadnov.web.server.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class UnknownOperationRequestProcessor implements RequestProcessor {
    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        String response = "HTTP/1.1 404 Not Found\r\nContent-Type: text/html\r\n\r\n<html><head><meta charset=\"utf-8\"></head><body><h1>Увы, страница не найдена! :(</h1></body></html>";
        output.write(response.getBytes(StandardCharsets.UTF_8));
    }
}
