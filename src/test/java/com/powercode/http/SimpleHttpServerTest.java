package com.powercode.http;

import com.powercode.http.exception.PortInUseException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimpleHttpServerTest {

    final int testPort = 8084;

    @Test
    void shouldCreateServerSocketIfNotPassed() throws PortInUseException {
        ServerSocket socket = SimpleHttpServer.createServerSocket(null);
        assertFalse(socket.equals(null));
        cleanup(socket);
    }

    @Test
    void shouldCreateServerSocketWithPort() throws PortInUseException {
        ServerSocket socket = SimpleHttpServer.createServerSocket(testPort);
        assertFalse(socket.equals(null));
        assertTrue(socket.getLocalPort() == testPort);
        cleanup(socket);
    }

    @Test
    void shouldThrowExceptionIfSocketAlreadyExists() throws PortInUseException {
        ServerSocket socket = SimpleHttpServer.createServerSocket(testPort);
        assertFalse(socket.equals(null));
        assertThrows(PortInUseException.class, () -> SimpleHttpServer.createServerSocket(testPort));
        cleanup(socket);
    }

    @Test
    void shouldCloseSocket() throws PortInUseException {
        ServerSocket socket = SimpleHttpServer.createServerSocket(testPort);
        assertFalse(socket.isClosed());
        SimpleHttpServer.closeServerSocket(socket);
        assertTrue(socket.isClosed());
    }

    void cleanup(ServerSocket socket) {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
