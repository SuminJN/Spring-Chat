package my.chatting.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.chatting.entity.Chat;
import my.chatting.entity.Type;
import my.chatting.repository.ChatRepository;
import my.chatting.service.RedisPublisher;
import my.chatting.repository.ChatRoomRepository;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Controller
@Slf4j
public class ChatController {

    private final RedisPublisher redisPublisher;
    private final ChatRoomRepository chatRoomRepository;

    private final ChatRepository chatRepository;

    /**
     * websocket "/pub/chat/message"로 들어오는 메시징 처리
     */

    @MessageMapping("/chat/message")
    public void message(Chat message) {
        log.info("Received message: {}", message);

        if (Type.ENTER.equals(message.getType())) {
            chatRoomRepository.enterChatRoom(message.getRoomId());
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
            log.info("User {} entered room {}", message.getSender(), message.getRoomId());
        }

        redisPublisher.publish(chatRoomRepository.getTopic(message.getRoomId()), message);

        message.setTime(LocalDateTime.now());
        chatRepository.save(message);
    }

}