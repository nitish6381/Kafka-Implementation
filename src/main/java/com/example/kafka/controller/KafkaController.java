package com.example.kafka.controller;

import com.example.kafka.dto.StudentDto;
import com.example.kafka.kafka.ImageProcessing;
import com.example.kafka.kafka.JsonKafkaProducer;
import com.example.kafka.kafka.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/kafka")
public class KafkaController {
    KafkaProducer kafkaProducer;

    @Autowired
    JsonKafkaProducer jsonKafkaProducer;
    @Autowired
    ImageProcessing service;
    public KafkaController(KafkaProducer kafkaProducer){
        this.kafkaProducer = kafkaProducer;
    }

//    @GetMapping("/publish")
//  public ResponseEntity<String> sendMessage(@RequestParam("message") String message){
//        kafkaProducer.sendMessage(message);
//        return ResponseEntity.ok("Message sent successfully");
//    }
//
//    @PostMapping("/student")
//    public ResponseEntity<String> pushStudent(@RequestBody StudentDto message){
//        jsonKafkaProducer.sendMessage(message);
//        return ResponseEntity.ok("Message sent successfully");
//    }

    @PostMapping("/pdf")
    public ResponseEntity<List<Double>> bulkDepartment(@RequestParam MultipartFile bulk_upload) {
        return service.processImage(bulk_upload);
    }
}
