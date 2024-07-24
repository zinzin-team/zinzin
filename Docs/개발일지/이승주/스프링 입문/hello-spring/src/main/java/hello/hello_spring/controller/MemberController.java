package hello.hello_spring.controller;

import hello.hello_spring.domain.Member;
import hello.hello_spring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
// 어노테이션이 있으면 내용이 없더라도 일단 스프링 컨테이너에 MemberController 객체를 생성해서 넣어둠
public class MemberController {

    private final MemberService memberService;

    // 굳이 여러 개의 MemberService 인스턴스가 필요 없으므로
    // 컨테이너에 등록한 후 하나를 공용으로 사용
    @Autowired  // 생성자에 @Autowired가 있으면, 스프링 컨테이너의 MemberService 객체와 연결시켜 줌
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }
    // => 생성자 주입

    // @Autowired private MemberService memberService;
    // => 필드 주입 (권장하지 않음)

    // private MemberService memberService;
    // @Autowired
    // public void setMemberService(MemberService memberService) {
    //     this.memberService = memberService;
    // }
    // => setter 주입

    @GetMapping("/members/new")
    public String createForm() {
        return "members/createMemberForm";
    }

    @PostMapping("members/new")
    public String create(MemberForm form) {
        Member member = new Member();
        member.setName(form.getName());

        memberService.join(member);

        return "redirect:/";
    }

    @GetMapping("/members")
    public String List(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }
}
