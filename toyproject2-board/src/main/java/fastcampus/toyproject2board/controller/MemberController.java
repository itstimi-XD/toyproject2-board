package fastcampus.toyproject2board.controller;

import fastcampus.toyproject2board.dto.MemberDTO;
import fastcampus.toyproject2board.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;


    @GetMapping("/login")

    public void login() {
        log.info("login get.............");
    }
    @PostMapping("/login")
    public String loginPost(@ModelAttribute MemberDTO memberDTO, HttpSession session, RedirectAttributes ra) {
        MemberDTO loginResult = memberService.login(memberDTO);
        if(loginResult != null) {
            session.setAttribute("loginInfo",loginResult);
            return "redirect:/";
        }else {
            session.setAttribute("msg", "login 실패!!!!!");
            return "redirect:/member/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("loginInfo");
        session.invalidate();
        return "/member/login";
    }

    @GetMapping("/join")
    public void joinGet() {
        log.info("join get..............");
    }

    @PostMapping("/join")
    public String joinPost(@ModelAttribute MemberDTO memberDTO) {
        log.info("join post...............");
        log.info(memberDTO.toString());
        memberService.signUp(memberDTO);
        return "redirect:/member/login";
    }

    @PostMapping("/email-check")
    @ResponseBody
    public String emailCheck(@RequestParam("memberEmail")String  memberEmail) {
        log.info("member email check............");
        log.info("memberEmail = " + memberEmail);
        String check = memberService.emailCheck(memberEmail);
        log.info(check);

        return check.equals("OK") ? "OK" : "NO";

    }

    @GetMapping("/list")
    public void findAll(Model model) {
        List<MemberDTO> memberDTOList = memberService.findAll();
        model.addAttribute("memberList", memberDTOList);
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        MemberDTO memberDTO = memberService.findById(id);
        model.addAttribute("member", memberDTO);
        return "/member/list";
    }
}
