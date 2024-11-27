package websocket;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class WebSocketConfigurator extends ServerEndpointConfig.Configurator {
	
    @Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
        // HTTP 세션 가져오기
    	HttpSession session = (HttpSession) request.getHttpSession();

    	// WebSocket에 HttpSession 추가
        config.getUserProperties().put(HttpSession.class.getName(), session);
    }
}







