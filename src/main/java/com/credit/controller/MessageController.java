package com.credit.controller;

import com.credit.model.CustomerEvent;
import com.credit.service.MessagePublisherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller for testing message publishing to RabbitMQ
 */
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final MessagePublisherService messagePublisherService;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    /**
     * Send a test message to RabbitMQ
     * 
     * @param message The message text to send
     * @return Response entity
     */
    @PostMapping(value = "/test", consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<Map<String, String>> sendTestMessage(@RequestBody String message) {
        log.info("Sending test message: {}", message);
        
        // Create a test customer event
        CustomerEvent event = CustomerEvent.builder()
                .eventType(CustomerEvent.EventType.CREATED)
                .customerId(999L)
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .message(message)
                .build();
        
        // Publish the event
        messagePublisherService.publishCustomerEvent(event);
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Message sent: " + message);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * An alternative endpoint that accepts JSON
     */
    @PostMapping(value = "/testJson", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> sendTestJsonMessage(@RequestBody Map<String, String> payload) {
        String message = payload.getOrDefault("message", "No message provided");
        log.info("Sending test JSON message: {}", message);
        
        // Create a test customer event
        CustomerEvent event = CustomerEvent.builder()
                .eventType(CustomerEvent.EventType.CREATED)
                .customerId(999L)
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .message(message)
                .build();
        
        // Publish the event
        messagePublisherService.publishCustomerEvent(event);
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Message sent: " + message);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Simple endpoint for sending a text message
     */
    @PostMapping("/simple")
    public ResponseEntity<Map<String, String>> sendSimpleMessage(@RequestBody(required = false) String message) {
        if (message == null) {
            message = "Empty message";
        }
        
        log.info("Sending simple message: {}", message);
        
        // Create a simple message without using Customer entity
        Map<String, Object> simpleMessage = new HashMap<>();
        simpleMessage.put("type", "SIMPLE_MESSAGE");
        simpleMessage.put("message", message);
        simpleMessage.put("timestamp", LocalDateTime.now().toString());
        
        // Publish directly to RabbitMQ
        messagePublisherService.publishMessage("credit.simple.message", simpleMessage);
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Simple message sent successfully");
        
        return ResponseEntity.ok(response);
    }
} 