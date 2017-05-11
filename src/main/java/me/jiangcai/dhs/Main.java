package me.jiangcai.dhs;

import com.sun.net.httpserver.HttpServer;
import org.springframework.util.NumberUtils;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author CJ
 */
public class Main {

    public static void main(String[] args) throws IOException {
        int port = 80;
        if (args.length > 0) {
            port = NumberUtils.parseNumber(args[0], Integer.class);
        }
        int status = 200;
        if (args.length > 1) {
            status = NumberUtils.parseNumber(args[1], Integer.class);
        }
        HttpServer server
                = HttpServer.create(new InetSocketAddress(port)
                , 100);
        new Server(server, status);
    }
}
