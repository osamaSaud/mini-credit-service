package com.credit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Autowired
    private ObjectMapper objectMapper;
    
    // Queue name
    public static final String QUEUE_NAME = "customer-events";
    
    // Simple queue name
    public static final String SIMPLE_QUEUE_NAME = "simple-messages";
    
    // Exchange name
    public static final String EXCHANGE_NAME = "credit-service-exchange";
    
    // Routing key
    public static final String ROUTING_KEY = "credit.customer.#";
    
    // Simple routing key
    public static final String SIMPLE_ROUTING_KEY = "credit.simple.#";

    // Create a queue
    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME, true);
    }
    
    // Create a simple queue
    @Bean
    public Queue simpleQueue() {
        return new Queue(SIMPLE_QUEUE_NAME, true);
    }

    // Create a topic exchange
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    // Bind the queue to the exchange with routing key
    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }
    
    // Bind the simple queue to the exchange with simple routing key
    @Bean
    public Binding simpleBinding(Queue simpleQueue, TopicExchange exchange) {
        return BindingBuilder.bind(simpleQueue).to(exchange).with(SIMPLE_ROUTING_KEY);
    }

    // Message converter to convert Java objects to JSON
    @Bean
    public MessageConverter jsonMessageConverter() {
        // Use our pre-configured ObjectMapper that has datetime handling
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(objectMapper);
        return converter;
    }

    // RabbitTemplate with message converter
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
} 