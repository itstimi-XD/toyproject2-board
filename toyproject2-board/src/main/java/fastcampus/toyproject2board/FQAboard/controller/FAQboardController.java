package fastcampus.toyproject2board.FQAboard.controller;

import fastcampus.toyproject2board.FQAboard.dto.FAQboardDTO;
import fastcampus.toyproject2board.FQAboard.service.FAQboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
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

    @GetMapping("/register")
    public void register(){

    }
    @PostMapping("/register")
    public String registerPost(@Valid FAQboardDTO faQboardDTO, BindingResult bindingResult,
                               RedirectAttributes redirectAttributes){
        log.info("board POST Register.....");
        if(bindingResult.hasErrors()){
            log.info("has error.......");
            redirectAttributes.addFlashAttribute("errors",bindingResult.getAllErrors());
            return "redirect:/FAQ/board/register";
        }
        log.info(faQboardDTO);
        Long tno = faQboardService.register(faQboardDTO);
        redirectAttributes.addFlashAttribute("result",tno);
        return "redirect:/FAQ/board/list";
    }
    @PostMapping("/remove")
    public String remove(Long tno, RedirectAttributes redirectAttributes){
        log.info("remove post.."+tno);
        faQboardService.remove(tno);
        redirectAttributes.addFlashAttribute("result","removed");
        return "redirect:/FAQ/board/list";
    }
    @GetMapping("/modify")
    public void read(Long tno, Model model){
        FAQboardDTO faQboardDTO = faQboardService.readOne(tno);
        log.info(tno);
        model.addAttribute("dto",faQboardDTO);
    }
    @PostMapping("/modify")
    public String modify(@Valid FAQboardDTO faQboardDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        log.info("board modify post.....");
        if(bindingResult.hasErrors()){
            log.info("has error.......");
            redirectAttributes.addFlashAttribute("errors",bindingResult.getAllErrors());
            return "redirect:/FAQ/board/modify";
        }
        faQboardService.modify(faQboardDTO);
        redirectAttributes.addFlashAttribute("result","modified");
        redirectAttributes.addAttribute("tno",faQboardDTO.getTno());
        return "redirect:/FAQ/board/list";
    }
}
