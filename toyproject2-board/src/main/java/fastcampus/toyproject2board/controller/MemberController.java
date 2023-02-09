package fastcampus.toyproject2board.controller;

import fastcampus.toyproject2board.dto.MemberDTO;
import fastcampus.toyproject2board.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;


    @GetMapping("/login")

    public void login() {
        System.out.println("login get.............");
    }
    @PostMapping("/login")
    public String loginPost(@ModelAttribute MemberDTO memberDTO, HttpSession session) {
        MemberDTO loginResult = memberService.login(memberDTO);
        if(loginResult != null) {
            session.setAttribute("loginInfo",loginResult);
            return "redirect:/";
        }else {
            return "redirect:/member/login";
        }
    }

    @GetMapping("/join")
    public void joinGet() {
        System.out.println("join get..............");
    }

    @PostMapping("/join")
    public String joinPost(@ModelAttribute MemberDTO memberDTO) {
        System.out.println("join post...............");
        System.out.println(memberDTO);
        memberService.signUp(memberDTO);
        return "redirect:/member/login";
    }
}
