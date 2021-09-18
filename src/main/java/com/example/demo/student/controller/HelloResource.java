package com.example.demo.student.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloResource {
    @RequestMapping("/hello")
    public String hello(){
        return "Hello world";
    }
}
