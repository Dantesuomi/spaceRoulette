package com.example.spaceRoulette.configuration.sessions;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionController {

    @GetMapping("/set-session")
    public String setSessionAttribute(HttpSession session) {
        session.setAttribute("key", "value");
        return "Session attribute set!";
    }

    @GetMapping("/get-session")
    public String getSessionAttribute(HttpSession session) {
        String value = (String) session.getAttribute("key");
        return "Session attribute: " + value;
    }
}
