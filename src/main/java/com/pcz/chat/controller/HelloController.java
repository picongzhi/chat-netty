package com.pcz.chat.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author picongzhi
 */
@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello() {
        return "hello chat";
    }
}
