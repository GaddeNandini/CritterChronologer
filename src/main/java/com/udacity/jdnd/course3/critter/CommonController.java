package com.udacity.jdnd.course3.critter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommonController {
    @GetMapping("/test")
    public String test() {
        return "Critter Starter installed successfully";
    }
}
