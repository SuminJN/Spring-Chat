package my.chatting.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(collation = "chat") //mongoDB
public class Chat {

    @Id
    private String id;

    private Type type;
    private String roomId;
    private String sender;
    private String message;
    private LocalDateTime time;
}