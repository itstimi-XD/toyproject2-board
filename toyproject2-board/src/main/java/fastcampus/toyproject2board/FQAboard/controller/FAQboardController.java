package fastcampus.toyproject2board.FQAboard.controller;

import fastcampus.toyproject2board.FQAboard.dto.FAQboardDTO;
import fastcampus.toyproject2board.FQAboard.service.FAQboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/FAQ/board")
@Log4j2
@RequiredArgsConstructor
public class FAQboardController {
    private final FAQboardService faQboardService;
    @GetMapping("/list")
    public void list(Model model){
        List<FAQboardDTO> faQboardDTOList = faQboardService.readAll();
        log.info(faQboardDTOList);
        model.addAttribute("DTOList",faQboardDTOList);
    }
}
