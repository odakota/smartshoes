package com.odakota.tms.system.service.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author haidv
 * @version 1.0
 */
@Slf4j
@Component
@ServerEndpoint("/notification/{userId}")
public class WebSocket {

    private static Set<WebSocket> webSockets = new CopyOnWriteArraySet<>();
    private static Map<Long, Session> sessionPool = new HashMap<>();
    private Session session;

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "userId") Long userId) {
        this.session = session;
        webSockets.add(this);
        sessionPool.put(userId, session);
        log.info("[Websocket] There are new connections, the total is:" + webSockets.size());
    }

    @OnClose
    public void onClose() {
        webSockets.remove(this);
        log.info("[Websocket] Connection is disconnected, the total is:" + webSockets.size());
    }

    @OnMessage
    public void onMessage(String message) {
        session.getAsyncRemote().sendText(message);
    }

    // this is a broadcast message
    public void sendAllMessage(String message) {
        for (WebSocket webSocket : webSockets) {
            if (webSocket.session.isOpen()) {
                webSocket.session.getAsyncRemote().sendText(message);
            }
        }
        log.info("[Websocket] Broadcast message:" + message);
    }

    // this is a single point message
    public void sendOneMessage(Long userId, String message) {
        Session ses = sessionPool.get(userId);
        if (ses != null && ses.isOpen()) {
            ses.getAsyncRemote().sendText(message);
        }
        log.info("[Websocket] single point message:" + message);
    }

    // this is a single point message multiple
    public void onMessage(List<Long> userIds, String message) {
        for (Long userId : userIds) {
            Session ses = sessionPool.get(userId);
            if (ses != null && ses.isOpen()) {
                ses.getAsyncRemote().sendText(message);
            }
        }
        log.info("[Websocket] single point message multiple:" + message);
    }
}
