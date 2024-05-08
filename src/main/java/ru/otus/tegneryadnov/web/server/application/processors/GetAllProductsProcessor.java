package ru.otus.tegneryadnov.web.server.application.processors;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.tegneryadnov.web.server.HttpRequest;
import ru.otus.tegneryadnov.web.server.application.Item;
import ru.otus.tegneryadnov.web.server.application.Storage;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class GetAllProductsProcessor implements RequestProcessor{
    private static final Logger logger = LogManager.getLogger(GetAllProductsProcessor.class.getName());
    static {
        logger.info("debug="+logger.isDebugEnabled());
    }
    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        List<Item> items = Storage.getItems();
        Gson gson = new Gson();
        String result = "HTTP/1.1 200 OK\r\nContent-Type: application/json\r\n\r\n" + gson.toJson(items);
        output.write(result.getBytes(StandardCharsets.UTF_8));
        if(logger.isDebugEnabled()) {
            logger.debug(result);
        }
    }
}
