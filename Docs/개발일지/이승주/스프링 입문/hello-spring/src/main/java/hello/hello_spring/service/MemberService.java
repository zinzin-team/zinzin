package hello.hello_spring.service;

import hello.hello_spring.domain.Member;
import hello.hello_spring.repository.MemberRepository;
import hello.hello_spring.repository.MemoryMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

// 데이터를 변경할 땐 항상 transaction이 있어야 함
@Transactional
//@Service
public class MemberService {

    private final MemberRepository memberRepository;

    // Test에서도 같은 MemberRepository를 사용할 수 있게
    // Test 파일에서 직접 new로 생성하는 것이 아니라, 외부에서 가져올 수 있도록
//    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 회원가입
    public Long join(Member member) {

//        long start = System.currentTimeMillis();

        // 중복 이름의 회원은 없어야 함
        // Optional<Member> result = memberRepository.findByName(member.getName());
        // result에 null이 아닌 값이 있다면
        // result.ifPresent(m -> {
        //     throw new IllegalStateException("이미 존재하는 회원입니다.");
        // });

        // 아래와 같은 방법 사용 권장
//        try {
            validateDuplicateMember(member); // 중복 회원 검증
            memberRepository.save(member);
            return member.getId();
//        } finally {
//            long finish = System.currentTimeMillis();
//            long timeMs = finish - start;
//            System.out.println("join = " + timeMs + "ms");
//        }
    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    // 전체 회원 조회
    public List<Member> findMembers() {

//        long start = System.currentTimeMillis();

//        try {
            return memberRepository.findAll();
//        } finally {
//            long finish = System.currentTimeMillis();
//            long timeMs = finish - start;
//            System.out.println("findMembers = " + timeMs + "ms");
//        }
    }

    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
