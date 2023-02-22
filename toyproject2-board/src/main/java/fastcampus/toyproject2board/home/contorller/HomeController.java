package fastcampus.toyproject2board.home.contorller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@Log4j2
@RequiredArgsConstructor
public class HomeController {
    @GetMapping("/home")
    public void home(){

    }
}