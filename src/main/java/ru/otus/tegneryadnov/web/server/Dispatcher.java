package ru.otus.tegneryadnov.web.server;

import ru.otus.tegneryadnov.web.server.application.processors.*;
/*import ru.otus.tegneryadnov.web.server.processors.CalculatorRequestProcessor;
import ru.otus.tegneryadnov.web.server.processors.HelloWorldRequestProcessor;
import ru.otus.tegneryadnov.web.server.processors.RequestProcessor;
import ru.otus.tegneryadnov.web.server.processors.UnknownOperationRequestProcessor;*/

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class Dispatcher {
    private final Map<String, RequestProcessor> router;
    private final RequestProcessor unknownOperationRequestProcessor;

    public Dispatcher() {
        this.router = new HashMap<>();
        this.router.put("GET /calc", new CalculatorRequestProcessor());
        this.router.put("GET /hello", new HelloWorldRequestProcessor());
        this.router.put("GET /items", new GetAllProductsProcessor());
        this.router.put("POST /items", new CreateNewProductProcessor());
        this.router.put("PUT /items", new ReplaceOrCreateProductProcessor());
        this.unknownOperationRequestProcessor = new UnknownOperationRequestProcessor();
    }

    public void execute(HttpRequest httpRequest, OutputStream outputStream) throws IOException {
        if (!router.containsKey(httpRequest.getRouteKey())) {
            unknownOperationRequestProcessor.execute(httpRequest, outputStream);
            return;
        }
        router.get(httpRequest.getRouteKey()).execute(httpRequest, outputStream);
    }
}
