package me.jiangcai.dhs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import me.jiangcai.dhs.model.ProcessInfo;
import me.jiangcai.dhs.model.RequestInfo;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * @author CJ
 */
class Server implements HttpHandler {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    private final HttpServer server;
    private final int status;

    Server(HttpServer server, int status) {
        this.server = server;
        this.status = status;

        init();
    }

    @Override
    protected void finalize() throws Throwable {
        server.stop(0);
    }

    private void init() {
        server.createContext("/", this);
        server.start();
    }

    public void handle(HttpExchange httpExchange) throws IOException {

        Path toFile = getCurrentLogPath();
        Files.createDirectories(toFile.getParent());

        try (BufferedWriter writer = Files.newBufferedWriter(toFile, Charset.forName("UTF-8")
                , StandardOpenOption.CREATE_NEW)) {
            ProcessInfo processInfo = new ProcessInfo();
            RequestInfo requestInfo = new RequestInfo(httpExchange.getRequestMethod(), httpExchange.getRequestURI(), httpExchange.getRequestHeaders(), httpExchange.getRequestBody());
            processInfo.setRequest(requestInfo);
//            processInfo.setResponse(response);
//            processInfo.setHandler(handler);
//            processInfo.setModelAndView(modelAndView);
            objectMapper.writeValue(writer, processInfo);
        }

        httpExchange.sendResponseHeaders(status, 0);
        httpExchange.close();

    }

    Path getCurrentLogPath() {
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyyMMdd", Locale.CHINA);
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HHmmss-SSS", Locale.CHINA);

        return Paths.get(".", "logs", dateFormat.format(time), timeFormat.format(time) + ".json");
    }
}
