package com.free4developer.sampleserver.domain.chat.config

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
class ChatConfig : WebSocketMessageBrokerConfigurer {
    companion object {
        const val OPEN_CHAT_PATH = "/stomp/chat"
        const val OPEN_CHAT_BROKER_SUBSCRIBE_PREFIX = "/channel"
        const val OPEN_CHAT_SEND_PREFIX = "/publish"
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint(OPEN_CHAT_PATH).setAllowedOriginPatterns("*").withSockJS()
    }

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.enableSimpleBroker(OPEN_CHAT_BROKER_SUBSCRIBE_PREFIX)
        registry.setApplicationDestinationPrefixes(OPEN_CHAT_SEND_PREFIX)
    }
}