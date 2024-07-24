package hello.hello_spring;

import hello.hello_spring.repository.JdbcMemberRepository;
import hello.hello_spring.repository.JdbcTemplateMemberRepository;
import hello.hello_spring.repository.MemberRepository;
import hello.hello_spring.repository.MemoryMemberRepository;
import hello.hello_spring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class SpringConfig {

    private DataSource dataSource;

    @Autowired
    public SpringConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        // return new MemoryMemberRepository();

        // return new JdbcMemberRepository(dataSource);
        // 애플리케이션을 조립하는 이 코드만 변경해주면
        // 애플리케이션 내부 코드는 건드리지 않고
        // 손쉽게 Repository 변경 가능

        return new JdbcTemplateMemberRepository(dataSource);
    }
}
