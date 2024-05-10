package ru.otus.tegneryadnov.web.server.application.processors;

import com.google.gson.Gson;
import ru.otus.tegneryadnov.web.server.HttpRequest;
import ru.otus.tegneryadnov.web.server.application.Item;
import ru.otus.tegneryadnov.web.server.application.Storage;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ReplaceOrCreateProductProcessor implements RequestProcessor {
    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        Gson gson = new Gson();
        Item item = gson.fromJson(httpRequest.getBody(), Item.class);
        String statusCode = "200 OK";
        if (!Storage.replaceItem(item)) {
            Storage.add(item);
            statusCode = "201 Created";
        }
        String response = String.format("HTTP/1.1 %s\r\nContent-Type: application/json\r\n\r\n" + httpRequest.getBody(), statusCode);
        output.write(response.getBytes(StandardCharsets.UTF_8));
    }
}
