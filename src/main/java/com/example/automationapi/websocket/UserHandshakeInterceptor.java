package com.example.automationapi.websocket;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Component
public class UserHandshakeInterceptor implements HandshakeInterceptor {

    private String normalize(String mobile) {
        if (mobile == null) return null;
        mobile = mobile.trim();

        if (mobile.startsWith("+91")) return mobile;
        if (mobile.startsWith("91")) return "+" + mobile;
        if (mobile.startsWith("+")) return mobile;

        return "+91" + mobile;
    }

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            org.springframework.http.server.ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes) {

        if (request instanceof ServletServerHttpRequest servletRequest) {

            String raw =
                    servletRequest.getServletRequest().getParameter("mobile");

            String normalized = normalize(raw);

            System.out.println("🤝 WS Handshake raw mobile = " + raw);
            System.out.println("🤝 WS Handshake normalized mobile = " + normalized);

            if (normalized != null && !normalized.isBlank()) {
                attributes.put("user", normalized);
            }
        }

        return true;
    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            org.springframework.http.server.ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception) {
    }
}
