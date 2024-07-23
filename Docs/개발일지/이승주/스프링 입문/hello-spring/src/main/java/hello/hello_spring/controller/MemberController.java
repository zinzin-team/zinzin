package hello.hello_spring.controller;

import hello.hello_spring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

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
}
