package me.jiangcai.dhs;

import com.sun.net.httpserver.HttpServer;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author CJ
 */
public class ServerTest {

    private Server server;
    private HttpServer httpServer;
    private int status;
    private int port;

    @Before
    public void start() throws IOException {
        port = 50000 + new Random().nextInt(10000);
        httpServer = HttpServer.create(new InetSocketAddress(port), 100);
        status = 200;
        server = new Server(httpServer, status);
    }

    @After
    public void close() {
        httpServer.stop(0);
    }

    @Test
    public void test() throws IOException {
        // 访问它 应当打印出我们想要的响应
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            String urlBuilder = "http://localhost:" + httpServer.getAddress().getPort() + "/" +
                    UUID.randomUUID().toString();

            HttpPost post = new HttpPost(urlBuilder);
            try (CloseableHttpResponse response = client.execute(post)) {
                assertThat(response.getStatusLine().getStatusCode())
                        .isEqualTo(status);
            }

            Path logPath = server.getCurrentLogPath();
            // 在它的上级然后循环所有目录
            Files.list(logPath.getParent()).forEach(path -> {
                try {
                    try (BufferedReader reader = Files.newBufferedReader(path, Charset.forName("UTF-8"))) {
                        reader.lines().forEach(System.out::println);
                    }
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            });

        }
    }

}