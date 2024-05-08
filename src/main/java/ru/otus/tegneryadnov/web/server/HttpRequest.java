package ru.otus.tegneryadnov.web.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpRequest {
    private final String rawRequest;
    private String uri;
    private HttpMethod method;
    private Map<String, String> parameters;
    private String body;

    public String getRouteKey() {
        return String.format("%s %s", method, uri);
    }

    public String getBody() {
        return body;
    }

    public String getUri() {
        return uri;
    }

    public String getParameter(String key) {
        return parameters.get(key);
    }

    public HttpRequest(String rawRequest) {
        this.rawRequest = rawRequest;
        parseRequestLine();
        tryToParseBody();
    }

    public void tryToParseBody() {
        if ((method == HttpMethod.GET) || ((method == HttpMethod.DELETE))) {
            return;
        }
        if ((method == HttpMethod.POST) || (method == HttpMethod.PUT)) {
            List<String> lines = rawRequest.lines().collect(Collectors.toList());
            int splitLine = -1;
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).isEmpty()) {
                    splitLine = i;
                    break;
                }
            }
            if (splitLine > -1) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = splitLine + 1; i < lines.size(); i++) {
                    stringBuilder.append(lines.get(i));
                }
                this.body = stringBuilder.toString();
            }
        }
    }

    public void parseRequestLine() {
        int startIndex = rawRequest.indexOf(' ');
        int endIndex = rawRequest.indexOf(' ', startIndex + 1);
        this.uri = rawRequest.substring(startIndex + 1, endIndex);
        this.method = HttpMethod.valueOf(rawRequest.substring(0, startIndex));
        this.parameters = new HashMap<>();
        if (uri.contains("?")) {
            String[] elements = uri.split("[?]");
            this.uri = elements[0];
            String[] keysValues = elements[1].split("&");
            for (String o : keysValues) {
                String[] keyValue = o.split("=");
                this.parameters.put(keyValue[0], keyValue[1]);
            }
        }
    }

    public void info(boolean showRawRequest) {
        if (showRawRequest) {
            System.out.println(rawRequest);
        }
        System.out.println("URI: " + uri);
        System.out.println("HTTP-method: " + method);
        System.out.println("Parameters: " + parameters);
        System.out.println("Body: " + body);
    }

    public StringBuilder getInfo(boolean showRawRequest) {
        StringBuilder stringBuilder = new StringBuilder();
        if (showRawRequest) {
            stringBuilder.append(rawRequest);
        }
        stringBuilder.append(String.format("\nURI: %s", uri))
                .append(String.format("\nHTTP-method: %s", method))
                .append(String.format("\nParameters: %s", parameters))
                .append(String.format("\nBody: %s\n", body));
        return stringBuilder;
    }
}
