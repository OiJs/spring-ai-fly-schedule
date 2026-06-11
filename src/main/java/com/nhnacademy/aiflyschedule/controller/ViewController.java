package com.nhnacademy.aiflyschedule.controller;

import com.nhnacademy.aiflyschedule.dto.request.FlightSearchRequest;
import com.nhnacademy.aiflyschedule.dto.request.LlmRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/")
    public String mainView() {
        return "index";
    }

    @GetMapping("/search")
    public String normalSearchView(Model model) {
        model.addAttribute("flightSearchRequest", new FlightSearchRequest("", "", "", "", null, null));
        return "search-view";
    }

    @GetMapping("/llm")
    public String llmSearchView(Model model) {
        model.addAttribute("llmRequest", new LlmRequest(""));
        return "llm-search-view";
    }
}
