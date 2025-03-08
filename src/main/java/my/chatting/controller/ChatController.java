package my.chatting.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.chatting.entity.ChatMessage;
import my.chatting.entity.Type;
import my.chatting.service.RedisPublisher;
import my.chatting.repository.ChatRoomRepository;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
@Slf4j
public class ChatController {

    private final RedisPublisher redisPublisher;
    private final ChatRoomRepository chatRoomRepository;

    /**
     * websocket "/pub/chat/message"로 들어오는 메시징 처리
     */

    @MessageMapping("/chat/message")
    public void message(ChatMessage message) {
        log.info("Received message: {}", message);

        if (Type.ENTER.equals(message.getType())) {
            chatRoomRepository.enterChatRoom(message.getRoomId());
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
            log.info("User {} entered room {}", message.getSender(), message.getRoomId());
        }

        redisPublisher.publish(chatRoomRepository.getTopic(message.getRoomId()), message);
    }
}