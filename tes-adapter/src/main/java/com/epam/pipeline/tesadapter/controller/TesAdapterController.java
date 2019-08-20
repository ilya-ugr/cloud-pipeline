package com.epam.pipeline.tesadapter.controller;

import com.epam.pipeline.tesadapter.entity.TesListTasksResponse;
import com.epam.pipeline.tesadapter.service.TesTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TesAdapterController {
  
    private TesTaskService tesTaskService;
  
    @Autowired
    public TesAdapterController(TesTaskService tesTaskService) {
        this.tesTaskService = tesTaskService;
    }

    @GetMapping("/v1/tasks/service-info")
    public ResponseEntity<String> serviceInfo() {
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @GetMapping("/v1/tasks")
    public ResponseEntity<TesListTasksResponse> listTesTasks(@RequestParam("name_prefix") String name_prefix,
                                                             @RequestParam("page_size") int page_size,
                                                             @RequestParam("page_token") String page_token,
                                                             @RequestParam("view") Enum view,
                                                             @RequestParam(defaultValue = "MINIMAL") String defaultVal) {
        return ResponseEntity.status(HttpStatus.OK).body(tesTaskService.listTesTask());
    }
}
