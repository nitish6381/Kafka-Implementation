package com.example.kafka.kafka;

import com.example.kafka.dto.StudentDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class JsonKafkaConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonKafkaConsumer.class.getName());

//    @KafkaListener(topics = "javaJson", groupId = "myGroup")
//    public void consume(StudentDto message){
//        LOGGER.info(String.format("Consumed message: %s", message.toString()));
//    }
}
