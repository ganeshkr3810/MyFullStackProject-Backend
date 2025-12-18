package com.example.automationapi.config;

import java.security.Principal;
import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import com.example.automationapi.websocket.UserHandshakeInterceptor;
import com.example.automationapi.websocket.UserPrincipal;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final UserHandshakeInterceptor handshakeInterceptor;

    public WebSocketConfig(UserHandshakeInterceptor handshakeInterceptor) {
        this.handshakeInterceptor = handshakeInterceptor;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/queue");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .setHandshakeHandler(new DefaultHandshakeHandler() {

                    @Override
                    protected Principal determineUser(
                            ServerHttpRequest request,
                            WebSocketHandler wsHandler,
                            Map<String, Object> attributes) {

                        Object mobile = attributes.get("user");

                        // 🔍 DEBUG (safe to keep)
                        System.out.println("🧑 determineUser called, mobile = " + mobile);

                        if (mobile == null) {
                            return null;
                        }

                        return new UserPrincipal(mobile.toString());
                    }
                })
                .withSockJS()
                // 🔥🔥🔥 CRITICAL FIX 🔥🔥🔥
                // Interceptor MUST be registered here for SockJS
                .setInterceptors(handshakeInterceptor);
    }
}
