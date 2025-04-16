package com.credit.service;

import com.credit.config.RabbitMQConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessagePublisherService {

    private final RabbitTemplate rabbitTemplate;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    /**
     * Initialize the service with properly configured ObjectMapper
     */
    @PostConstruct
    public void init() {
        // Always register JavaTimeModule to ensure proper datetime handling
        log.info("Registering JavaTimeModule for datetime handling");
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /**
     * Publish a message to RabbitMQ
     * 
     * @param routingKey The routing key to use (determines which queue receives the message)
     * @param message The message object to send
     */
    public void publishMessage(String routingKey, Object message) {
        try {
            log.info("Publishing message with routing key: {}", routingKey);
            
            // Log message content for debugging
            try {
                String messageJson = objectMapper.writeValueAsString(message);
                log.debug("Message content: {}", messageJson);
            } catch (JsonProcessingException e) {
                log.warn("Could not serialize message for logging", e);
            }
            
            // Send the message
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, routingKey, message);
            log.info("Message published successfully");
        } catch (AmqpException e) {
            log.error("Failed to publish message: {}", e.getMessage());
            throw e;
        }
    }
    
    /**
     * Publish a customer event message
     * 
     * @param message The message to publish
     */
    public void publishCustomerEvent(Object message) {
        log.info("Publishing customer event message");
        publishMessage("credit.customer.event", message);
    }
} 