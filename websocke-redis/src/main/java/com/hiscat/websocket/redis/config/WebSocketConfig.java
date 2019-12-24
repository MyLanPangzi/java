package com.hiscat.websocket.redis.config;

import com.hiscat.websocket.redis.message.Greeting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Administrator
 */
@Slf4j
@SuppressWarnings("ConstantConditions")
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private SimpMessagingTemplate simpMessagingTemplate;
    private RedisMessageListenerContainer redisMessageListenerContainer;

    @Autowired
    public void setSimpMessagingTemplate(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Autowired
    public void setRedisMessageListenerContainer(RedisMessageListenerContainer redisMessageListenerContainer) {
        this.redisMessageListenerContainer = redisMessageListenerContainer;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/gs-guide-websocket").withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new RedisSubscribeChannelInterceptor());
    }

    /**
     * 每个节点每个主题最多只能订阅一次
     */
    private class RedisSubscribeChannelInterceptor implements ChannelInterceptor {
        private List<String> topics = new CopyOnWriteArrayList<>();

        @Override
        public Message<?> preSend(Message<?> message, MessageChannel channel) {
            StompHeaderAccessor accessor = StompHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
            if (!StompCommand.SUBSCRIBE.equals(accessor.getCommand()) || topics.contains(accessor.getDestination())) {
                return message;
            }
            topics.add(accessor.getDestination());
            redisMessageListenerContainer.addMessageListener((message1, pattern) -> {
                Greeting payload = new Greeting(new String(message1.getBody()));
                LOGGER.info("dest:{}, message:{}", accessor.getDestination(), payload);
                simpMessagingTemplate.convertAndSend(accessor.getDestination(), payload);
            }, new ChannelTopic(accessor.getDestination()));
            return message;
        }
    }
}