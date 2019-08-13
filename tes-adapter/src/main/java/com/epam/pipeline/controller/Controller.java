package com.epam.pipeline.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @GetMapping("/v1/tasks/service-info")
    private String getInfo(){
        return "OK";
    }
}
