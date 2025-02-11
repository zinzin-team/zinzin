package hello.hello_spring;

import hello.hello_spring.repository.*;
import hello.hello_spring.service.MemberService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class SpringConfig {
//    private DataSource dataSource;
//    @PersistenceContext
//    private EntityManager em;
//
//    @Autowired
//    public SpringConfig(DataSource dataSource, EntityManager em) {
//        this.dataSource = dataSource;
//        this.em = em;
//    }

    private final MemberRepository memberRepository;

    @Autowired
    public SpringConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository);
    }

//    @Bean
//    public MemberRepository memberRepository() {
        // return new MemoryMemberRepository();

        // return new JdbcMemberRepository(dataSource);
        // 애플리케이션을 조립하는 이 코드만 변경해주면
        // 애플리케이션 내부 코드는 건드리지 않고
        // 손쉽게 Repository 변경 가능

        // return new JdbcTemplateMemberRepository(dataSource);

        // return new JpaMemberRepository(em);
//    }
}
