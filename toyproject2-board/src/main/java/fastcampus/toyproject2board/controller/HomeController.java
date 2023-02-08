package fastcampus.toyproject2board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    //기본페이지
    @GetMapping("/")
    public String index() {
        System.out.println("index get ........................");
        return "index";
    }
}
