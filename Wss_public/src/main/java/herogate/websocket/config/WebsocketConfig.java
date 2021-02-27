package herogate.websocket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import herogate.websocket.Intercepter.CustomHandshakeIntercepter;
import herogate.websocket.handler.CustomWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebsocketConfig implements WebSocketConfigurer {

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(customHandler(), new String[] { "/liveprice" })
				.addInterceptors(customHandshakeIntercepter()).setAllowedOrigins("*")
				// .setAllowedOrigins("http://www.232.kr")
				.setHandshakeHandler(new DefaultHandshakeHandler());
	}

	@Bean
	public CustomWebSocketHandler customHandler() {
		return new CustomWebSocketHandler();
	}

	@Bean
	public CustomHandshakeIntercepter customHandshakeIntercepter() {
		return new CustomHandshakeIntercepter();
	}
}