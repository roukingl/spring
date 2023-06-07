package com.example.java_gobang.config;

import com.example.java_gobang.component.GameHandler;
import com.example.java_gobang.component.MatchHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private MatchHandler matchHandler;

    @Autowired
    private GameHandler gameHandler;

    // 关联前端url和后端实现类
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 通过 .addInterceptors(new HttpSessionHandshakeInterceptor()
        // 这个操作来把 HttpSession 里的属性放到 WebSocket 的 session 中
        // 然后就可以在 WebSocket 代码中 WebSocketSession 里拿到 HttpSession 中的 attribute.
        registry.addHandler(matchHandler, "/findmatch")
                .addInterceptors(new HttpSessionHandshakeInterceptor());
        registry.addHandler(gameHandler, "/game")
                .addInterceptors(new HttpSessionHandshakeInterceptor());
    }
}
