package me.jiangcai.dhs.model;

import com.sun.net.httpserver.Headers;
import lombok.Data;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;

/**
 * @author CJ
 */
@Data
public class RequestInfo {

    private final String method;
    private final String uri;
    private final Headers headers;
    private final String content;

    public RequestInfo(String method, URI uri, Headers headers, InputStream content) throws IOException {
        this.method = method;
        this.uri = uri.toString();
        this.headers = headers;
        this.content = StreamUtils.copyToString(content, Charset.forName("UTF-8"));
    }
}
