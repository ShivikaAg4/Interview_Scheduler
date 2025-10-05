package com.example.scheduler.Controller;

import org.springframework.web.multipart.MultipartFile;

public class test {
    public void test(MultipartFile file) {
        System.out.println(file.getOriginalFilename());
    }
}