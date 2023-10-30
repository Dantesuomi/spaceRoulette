package com.example.spaceRoulette.React;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {
    @GetMapping("/my-page")
    public String getMyPage() {
        return "my-page";
    }
}
