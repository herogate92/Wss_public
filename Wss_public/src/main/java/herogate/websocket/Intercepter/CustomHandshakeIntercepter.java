package herogate.websocket.Intercepter;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

public class CustomHandshakeIntercepter extends HttpSessionHandshakeInterceptor {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		try {
			logger.info("Before Handshake");
			logger.info("request.getHeaders()::" + request.getHeaders().toString());
			logger.info("request.getHeaders().getOrigin()::" + request.getHeaders().get(request.getHeaders().ORIGIN));
			logger.info("wsHandler.toString()::" + wsHandler.toString());

			if (request.getHeaders().get(request.getHeaders().ORIGIN) != null) {
				String origin = Optional.ofNullable(request.getHeaders().get(request.getHeaders().ORIGIN).get(0))
						.orElse("");
				logger.info("origin::" + origin);
			}
			if (request.getHeaders().get("xcode") != null) {
				String xcode = Optional.ofNullable(request.getHeaders().get("xcode").get(0)).orElse("");
				logger.info("xcode::" + xcode);

			}
			if (request.getHeaders().get(request.getHeaders().USER_AGENT) != null) {
				String userAgent = Optional.ofNullable(request.getHeaders().get(request.getHeaders().USER_AGENT).get(0))
						.orElse("");
				logger.info("userAgent::" + userAgent);
			}

			return super.beforeHandshake(request, response, wsHandler, attributes);
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatusCode(HttpStatus.BAD_REQUEST);
			return false;
		}

	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception ex) {
		logger.info("After Handshake");
		super.afterHandshake(request, response, wsHandler, ex);
	}
}
