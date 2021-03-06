package com.pauldaniv.aws.s3.application.test;

import com.pauldaniv.aws.s3.client.S3StorageClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Data
    @RestController
    @RequestMapping("/test")
    public static class TestController {
        @Autowired
        private S3StorageClient s3StorageClient;

        @GetMapping
        String test() {
            return "It's working";
        }
    }
}
