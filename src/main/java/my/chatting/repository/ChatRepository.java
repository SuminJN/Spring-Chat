package my.chatting.repository;

import my.chatting.entity.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatRepository extends MongoRepository<Chat,String> {
//    public List<Chat> findAllByRoomIdAndTimeAfter(String roomId, LocalDateTime time);
}