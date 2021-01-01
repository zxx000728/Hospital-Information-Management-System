package com.hims.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping("/login")
    public String hello() {
        return "Hello, this is a Springboot demo!";
    }
}
