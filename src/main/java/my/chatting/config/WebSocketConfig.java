package my.chatting.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // 메시지 브로커를 구성하는 메서드
    // 클라이언트로부터의 메시지를 처리하고 응답을 전달하는 방법을 설정합니다.
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 간단한 메모리 기반 메시지 브로커를 활성화하고, 해당 브로커의 목적지 접두사를 "/sub"로 설정
        registry.enableSimpleBroker("/sub");
        
        // 애플리케이션에서 처리할 메시지의 접두사를 "/pub"로 설정
        registry.setApplicationDestinationPrefixes("/pub");
    }

    // STOMP 엔드포인트를 등록하는 메서드
    // 클라이언트가 웹소켓 서버에 연결할 수 있는 엔드포인트를 설정합니다.
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // "/ws-stomp" 엔드포인트를 추가하고, 모든 도메인에서의 요청을 허용하며 SockJS 지원을 활성화
        registry.addEndpoint("/ws-stomp").setAllowedOriginPatterns("*").withSockJS();
    }
}