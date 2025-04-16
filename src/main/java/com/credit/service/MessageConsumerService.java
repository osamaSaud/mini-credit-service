package com.credit.service;

import com.credit.config.RabbitMQConfig;
import com.credit.model.CustomerEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Service that consumes messages from RabbitMQ
 * This is just for demonstration purposes - in a real application,
 * this might be in a separate microservice
 */
@Service
@Slf4j
public class MessageConsumerService {

    @Autowired
    private ObjectMapper objectMapper;
    
    @PostConstruct
    public void init() {
        // Always register JavaTimeModule to ensure proper datetime handling
        log.info("Registering JavaTimeModule for MessageConsumerService");
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /**
     * Listen for messages on the customer-events queue
     * 
     * @param event The customer event received from the queue
     */
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveCustomerEvent(CustomerEvent event) {
        try {
            String eventJson = objectMapper.writeValueAsString(event);
            log.info("✅ RECEIVED EVENT: {}", eventJson);
            
            // In a real application, we would process the event here
            // For example, we might update a cache, send a notification, etc.
            switch (event.getEventType()) {
                case CREATED:
                    log.info("👤 Processing CUSTOMER CREATED event for customer ID: {}", event.getCustomerId());
                    // Handle customer created event
                    break;
                case UPDATED:
                    log.info("📝 Processing CUSTOMER UPDATED event for customer ID: {}", event.getCustomerId());
                    // Handle customer updated event
                    break;
                case DELETED:
                    log.info("🗑️ Processing CUSTOMER DELETED event for customer ID: {}", event.getCustomerId());
                    // Handle customer deleted event
                    break;
                default:
                    log.warn("⚠️ Unknown event type: {}", event.getEventType());
            }
        } catch (JsonProcessingException e) {
            log.error("❌ Error processing event: {}", e.getMessage());
        }
    }
    
    /**
     * Listen for simple messages
     */
    @RabbitListener(queues = "#{@simpleQueue}")
    public void receiveSimpleMessage(Map<String, Object> message) {
        log.info("📨 Received simple message: {}", message);
    }
} 