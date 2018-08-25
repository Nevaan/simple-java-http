package com.powercode.http;

import com.powercode.http.exception.PortInUseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimpleHttpServerTest {

    SimpleHttpServer serverUnderTest;

    @BeforeEach
    void initialize() {
        serverUnderTest = new SimpleHttpServer();
    }

    @Test
    void shouldCreateServerSocketIfNotPassed() throws PortInUseException {
        ServerSocket socket = serverUnderTest.createServerSocket(null);
        assertFalse(socket.equals(null));
        cleanup(socket);
    }

    @Test
    void shouldCreateServerSocketWithPort() throws PortInUseException {
        final int testPort = 8084;

        ServerSocket socket = serverUnderTest.createServerSocket(testPort);
        assertFalse(socket.equals(null));
        assertTrue(socket.getLocalPort() == testPort);
        cleanup(socket);
    }

    @Test
    void shouldThrowExceptionIfSocketAlreadyExists() throws PortInUseException {
        final int testPort = 8084;
        ServerSocket socket = serverUnderTest.createServerSocket(testPort);
        assertFalse(socket.equals(null));
        assertThrows(PortInUseException.class, () -> serverUnderTest.createServerSocket(testPort));
        cleanup(socket);
    }

    void cleanup(ServerSocket socket) {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
