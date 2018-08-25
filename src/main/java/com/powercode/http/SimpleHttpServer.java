package com.powercode.http;

import com.powercode.http.exception.PortInUseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;
import org.slf4j.*;


public class SimpleHttpServer {

    private static final Logger logger = LoggerFactory.getLogger(SimpleHttpServer.class);

    public static void main(String[] args) {

        final ServerSocket socket;
        final int port = 8085;

        logger.info("Server startup. Using port {}", port);

        try {
            socket = createServerSocket(port);
        } catch (PortInUseException e) {
            logger.error("Fatal error during initialization: unable to create socket on port {}.", port);
            logger.error("Closing server...");
            return;
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> closeServerSocket(socket)));

        while (true) {
            try (final Socket client = socket.accept()) {
                logger.info("Request incoming...");
                InputStreamReader clientInputStream = new InputStreamReader(client.getInputStream());
                BufferedReader reader = new BufferedReader(clientInputStream);
                String line = reader.readLine();
                while (!line.isEmpty()) {
                    logger.debug(line);
                    line = reader.readLine();
                }

                String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + new Date();
                logger.info("Sending response...");
                logger.debug("Response: \n{}\n", httpResponse);
                client.getOutputStream().write(httpResponse.getBytes(StandardCharsets.UTF_8));
            } catch (IOException | NullPointerException e) {
                logger.debug("Unexpected error occurred while handling request");
            }
        }

    }

    public static ServerSocket createServerSocket(Integer port) throws PortInUseException {
        final int serverPort = Optional.ofNullable(port).orElseGet(() -> {
            logger.info("No port provided. Using dynamically allocated port number");
            return 0;
        });

        try {
            return new ServerSocket(serverPort);
        } catch (IOException e) {
            throw new PortInUseException(String.format("Unable to create server listening on port %d. Possibly port in use", serverPort), e);
        }
    }

    public static void closeServerSocket(ServerSocket socket) {
        try {
            socket.close();
            logger.info("Server socket closed successfully");
        } catch (IOException e) {
            logger.warn("Error while closing socket, resource may not been cleared properly.");
        }
    }

}
