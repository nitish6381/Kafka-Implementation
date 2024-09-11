package com.example.kafka.kafka;

import com.example.kafka.dto.StudentDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class JsonKafkaProducer {
//    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class.getName());
//    private KafkaTemplate<String, StudentDto> kafkaTemplate;
//    public JsonKafkaProducer(KafkaTemplate<String, StudentDto> kafkaTemplate) {
//        this.kafkaTemplate = kafkaTemplate;
//    }
//
//    public void sendMessage(StudentDto studentDto){
//        LOGGER.info(String.format("Logged Successfully "+studentDto));
//        kafkaTemplate.send("javaJson", studentDto);
//    }
}
