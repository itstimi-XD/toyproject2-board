package fastcampus.toyproject2board.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class HomeController {
    //기본페이지
    @GetMapping("/")
    public String index() {
        log.info("index get ........................");
        return "index";
    }
}
