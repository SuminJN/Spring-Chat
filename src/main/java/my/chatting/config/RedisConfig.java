package my.chatting.config;

import my.chatting.entity.ChatRoom;
import my.chatting.service.RedisSubscriber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

    // Redis 서버의 호스트 주소를 가져오는 변수
    @Value("${spring.data.redis.host}")
    private String redisHost;

    // Redis 서버의 포트 번호를 가져오는 변수
    @Value("${spring.data.redis.port}")
    private int redisPort;

    // RedisConnectionFactory 빈을 생성하는 메서드
    // Redis 서버와의 연결을 설정하고 관리하는데 사용됩니다.
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisHost, redisPort);
    }

    // ChannelTopic 빈을 생성하는 메서드
    // Redis의 pub/sub 메시징을 위한 채널 토픽을 설정합니다.
    @Bean
    public ChannelTopic channelTopic() {
        return new ChannelTopic("chatroom");
    }

    // RedisMessageListenerContainer 빈을 생성하는 메서드
    // Redis 메시지를 수신하고 리스너에 전달하는 컨테이너를 설정합니다.
    @Bean
    public RedisMessageListenerContainer redisMessage(
            MessageListenerAdapter listenerAdapterChatMessage,
            ChannelTopic channelTopic
    ){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        container.addMessageListener(listenerAdapterChatMessage, channelTopic);
        return container;
    }

    // 실제 메시지를 처리하는 subscriber 설정 추가
    // RedisSubscriber 클래스를 사용하여 메시지를 처리하는 어댑터를 설정합니다.
    @Bean
    public MessageListenerAdapter listenerAdapterChatMessage(RedisSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "onMessage");
    }

    // RedisTemplate 빈을 생성하는 메서드
    // Redis 데이터를 직렬화하고 역직렬화하는 데 사용됩니다.
    @Bean
    public RedisTemplate<String, Object> chatRoomRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // 키를 위한 직렬화 설정 - StringRedisSerializer 사용
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // 값을 위한 직렬화 설정 - Jackson2JsonRedisSerializer 사용
        Jackson2JsonRedisSerializer<ChatRoom> serializer = new Jackson2JsonRedisSerializer<>(ChatRoom.class);
        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);

        return template;
    }
}