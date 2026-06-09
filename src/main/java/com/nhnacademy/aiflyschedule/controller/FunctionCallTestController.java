package com.nhnacademy.aiflyschedule.controller;

import com.nhnacademy.aiflyschedule.service.FunctionCallTestService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class FunctionCallTestController {

    private final FunctionCallTestService testService;

    public FunctionCallTestController(FunctionCallTestService testService) {
        this.testService = testService;
    }

    /**
     * Function Calling 테스트
     * GET /api/test/function-calling?message=10과20의합은?
     */
    @GetMapping("/function-calling")
    public String testFunctionCalling(@RequestParam String message) {
        return testService.testFunctionCalling(message);
    }
}