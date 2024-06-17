package com.dreamgames.backendengineeringcasestudy.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// It is implemented to check server status. Don't modify class.

@RestController
public class StatusController {
    @GetMapping("/status")
    public String status() {
        return "Server is up!";
    }
}
