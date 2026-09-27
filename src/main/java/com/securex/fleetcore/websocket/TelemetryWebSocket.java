package com.securex.fleetcore.websocket;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/ws/telemetry")
public class TelemetryWebSocket {

    // Thread-safe collection to hold all connected dashboard clients
    private static final Set<Session> sessions = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        System.out.println("WebSocket Connected: Dispatcher Dashboard [" + session.getId() + "]");
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        System.out.println("WebSocket Disconnected: Dispatcher Dashboard [" + session.getId() + "]");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        sessions.remove(session);
        System.err.println("WebSocket Error on session " + session.getId() + ": " + throwable.getMessage());
    }

    // Static method called by the REST controller to push data to all screens
    public static void broadcast(String jsonMessage) {
        for (Session session : sessions) {
            if (session.isOpen()) {
                try {
                    session.getBasicRemote().sendText(jsonMessage);
                } catch (IOException e) {
                    System.err.println("Failed to send live telemetry to session " + session.getId());
                }
            }
        }
    }
}