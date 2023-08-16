package com.acorn.tracking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/status")
@RequiredArgsConstructor
public class StatusController {

    @GetMapping()
    public String getStatus() {
        return "status";
    }
    
}
