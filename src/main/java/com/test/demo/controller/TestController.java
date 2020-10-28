package com.test.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TestController {

    @GetMapping("/test")
    public String test() {
        log.info("Test: INFO");
        log.debug("Test: DEBUG");
        log.error("Test: ERROR");
        int i = 1 / 0;
        /*try {
            int i = 1 / 0;
        } catch (ArithmeticException ae) {
            for (int i = 0; i < ae.getStackTrace().length; i++) {
                log.error(String.valueOf(ae.getStackTrace()[i]));
            }
        }*/
        return "SUCCESS";
    }
}
