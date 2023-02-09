package fastcampus.toyproject2board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/notices")
@Controller
public class NoticeController {

    @GetMapping
    public String notices(ModelMap map){
        map.addAttribute("notices", List.of());
        return "notices/index";
    }
}
