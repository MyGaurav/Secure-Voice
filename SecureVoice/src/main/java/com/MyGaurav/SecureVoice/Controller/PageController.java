package com.MyGaurav.SecureVoice.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/admin/login")
    public String login() {
        return "login"; // matches templates/login.html
    }
}
