package hello.hello_spring.repository;

import hello.hello_spring.domain.Member;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class JpaMemberRepository implements MemberRepository {
    // JPA는 EntityManager로 모든 걸 동작
    // 스프링 부트가 자동으로 EntityManager 생성
    // 자동으로 DB와 연결하고 내부적으로 알아서 dataSource 가지고 있어서 통신 등을 처리
    // => JPA를 쓰려면 EntityManger를 주입받아야 함!
    private final EntityManager em;

    public JpaMemberRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Member save(Member member) {
        // 이렇게만 해 주면 JPA가 알아서 insert 쿼리 다 만들어서 DB에 넣어줌
        // member에 알아서 setId까지 해 줌
        em.persist(member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }
    
    // PK 기반이 아닌 조회는 jpql 필요
    // spring-data-jpa를 사용하면 쿼리문을 짤 필요가 없음

    @Override
    public Optional<Member> findByName(String name) {
        List<Member> result = em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
        // 하나만 찾기 때문에
        return result.stream().findAny();
    }

    @Override
    public List<Member> findAll() {
        // Jpql이라는 객체 지향 쿼리 작성 필요 (거의 sql과 비슷함)
        // m 이라는 객체 자체를 select: column을 따로 선택할 필요 없이 알아서 매핑이 되어 있음
        List<Member> result = em.createQuery("select m from Member m", Member.class)
                .getResultList();
        return result;
    }
}
