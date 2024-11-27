package com.backend.proprental.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @RequestMapping("/hello")
    public String helloWorld() {
        return "Hello, World! from unsecured endpoint";
    }
}
