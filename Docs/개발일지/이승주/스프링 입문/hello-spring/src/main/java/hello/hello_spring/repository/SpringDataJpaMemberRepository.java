package hello.hello_spring.repository;

import hello.hello_spring.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// 스프링 데이터 jpa가 JpaRepository를 받고 있으면 자동으로 구현체 생성, 스프링 빈 자동 등록
public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long>, MemberRepository {

    // JPQL: select m from Member m where m.name = ?
    @Override
    Optional<Member> findByName(String name);
    // 메서드 명에 규칙이 있음
    // findByNameAndId(String name, Long Id); 도 가능
}
