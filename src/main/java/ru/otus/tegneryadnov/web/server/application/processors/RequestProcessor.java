package ru.otus.tegneryadnov.web.server.application.processors;

import ru.otus.tegneryadnov.web.server.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;

public interface RequestProcessor {
    void execute(HttpRequest httpRequest, OutputStream output) throws IOException;
}
