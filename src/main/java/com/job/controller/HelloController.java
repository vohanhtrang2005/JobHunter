package com.job.controller;

import org.springframework.web.bind.annotation.RestController;

import com.job.util.error.IdInvalidException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class HelloController {
@GetMapping("/")
public String getHelloWorld() throws IdInvalidException {
    return "Hello World!";
}

    
}
