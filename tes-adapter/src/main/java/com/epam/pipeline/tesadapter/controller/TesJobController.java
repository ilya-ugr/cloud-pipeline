package com.epam.pipeline.tesadapter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TesJobController {
    @GetMapping("/v1/tasks/service-info/")
    public ResponseEntity<String> getInfo(){
        return new ResponseEntity<> ( "OK", HttpStatus.OK );
    }
}
