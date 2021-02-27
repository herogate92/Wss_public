package herogate.websocket.handler;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import herogate.websocket.domain.Con_log;
import herogate.websocket.repo.Con_logRepository;

public class CustomWebSocketHandler extends TextWebSocketHandler {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private Map<String, WebSocketSession> users = new ConcurrentHashMap<String, WebSocketSession>();
	private String bot_session_id = "";
	@Autowired
	Con_logRepository con_logRepo;
	
	@Value("${header_xcode}")
	String HEADER_XCODE;
	
	// 연결 후
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {

		logger.info(session.getId() + " 연결 됨");

		String xcode = "";
		String userAgent = "";
		String USER_AGENT = session.getHandshakeHeaders().USER_AGENT;
		if (session.getHandshakeHeaders().get("xcode") != null) {
			xcode = Optional.ofNullable(session.getHandshakeHeaders().get("xcode").get(0)).orElse("");
			logger.info("xcode::" + xcode);

		}
		if (session.getHandshakeHeaders().get(session.getHandshakeHeaders().USER_AGENT) != null) {
			userAgent = Optional.ofNullable(session.getHandshakeHeaders().get(USER_AGENT).get(0)).orElse("");
			logger.info("userAgent::" + userAgent);
		}

		if (HEADER_XCODE.equals(xcode) && !"websocket-sharp/1.0".equals(userAgent)) {
			this.users.put(session.getId(), session);
			logger.info("users.put::" + session.getId());
		}

		else if ("Hash".equals(xcode) && "websocket-sharp/1.0".equals(userAgent)) {
			bot_session_id = session.getId();
			logger.info("bot_session_id::" + bot_session_id);
		}

		logger.info("getAcceptedProtocol::" + session.getAcceptedProtocol().toString());
		String header = session.getHandshakeHeaders().toString();
		String msg = "after connection :: " + session.getId();
		String ip = session.getRemoteAddress().getAddress().getHostAddress();
		String xrealIP = session.getHandshakeHeaders().get("X-Real-IP").get(0);
		logger.info(ip);
		Con_log con_log = Con_log.builder().header(header).msg(msg).ipaddr(xrealIP).build();
		con_logRepo.save(con_log);
	}

	// 연결 종료후
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		logger.info(session.getId() + " 연결 종료됨");
		logger.info(status.getReason());
		this.users.remove(session.getId());

		String header = session.toString();
		String msg = "after connection :: " + session.getId() + " REASON ::" + status.getReason();
		String ip = session.getRemoteAddress().getHostString();
		Con_log con_log = Con_log.builder().header(header).msg(msg).ipaddr(ip).build();

		con_logRepo.save(con_log);
	}

	// 메시지 헨들
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		// xcode가 잇는 client만 sendmesage를 사용할 수 있음 없으면 걍 return
		if (bot_session_id.equals(session.getId())) {
			for (WebSocketSession s : this.users.values()) {
				s.sendMessage(message);
				logger.info(s.getId() + "에 메시지 발송: " + (String) message.getPayload());
			}
		} else {
			logger.info(session.getId() + "로부터 메시지 수신: " + (String) message.getPayload());
			logger.info("session.toString()::" + session.toString());
		}
		String header = session.getId().toString();
		String msg = (String) message.getPayload();
		String ip = session.getRemoteAddress().getHostString();

		Con_log con_log = Con_log.builder().header(header).msg(msg).ipaddr(ip).build();
		con_logRepo.save(con_log);
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		logger.info(session.getId() + " 익셉션 발생: " + exception.getMessage());
	}
}
