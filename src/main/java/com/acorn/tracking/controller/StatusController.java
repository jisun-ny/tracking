package com.acorn.tracking.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acorn.tracking.domain.Deliveries;
import com.acorn.tracking.service.DeliveriesService;
import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/status")
@RequiredArgsConstructor
public class StatusController {

    private final DeliveriesService deliveriesService;

    @GetMapping()
    public String getStatus(Model model) {
        model.addAttribute("jsonLocations", convertToJson(deliveriesService.getLocations()));
        return "status";
    }

    private String convertToJson(List<Deliveries> locations) {
        Gson gson = new Gson();
        return gson.toJson(locations);
    }
}
