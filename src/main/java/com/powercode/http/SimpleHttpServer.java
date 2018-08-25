package com.powercode.http;

import com.powercode.http.exception.PortInUseException;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Optional;

public class SimpleHttpServer {
    public static void main(String[] args) {

    }

    public ServerSocket createServerSocket(Integer port) throws PortInUseException {
        final int serverPort = Optional.ofNullable(port).orElse(0);

        try {
            return new ServerSocket(serverPort);
        } catch (IOException e) {
            throw new PortInUseException(String.format("Unable to create server listening on port %d. Possibly port in use", serverPort), e);
        }
    }

}
