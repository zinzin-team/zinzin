package hello.hello_spring.repository;

import hello.hello_spring.domain.Member;

import java.util.*;

public class MemoryMemberRepository implements MemberRepository {
    // save 할 메모리
    // key는 회원ID(Long), 값은 회원객체(Member)
    // 실무에서는 동시성 문제가 생길 수 있어서 공유되는 변수는 ConcurrentHashMap 사용
    private static Map<Long, Member> store = new HashMap<>();
    // 0, 1, 2 등 key 값을 생성해 줌
    // 실무에서는 동시성 문제를 고려하여 AtomicLong 사용
    private static long sequence = 0L;

    @Override
    public Member save(Member member) {
        member.setId(++sequence);  // ID 지정
        store.put(member.getId(), member);  // store에 저장
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id));  // null이 반환될 수 있기 때문
    }

    @Override
    public Optional<Member> findByName(String name) {
        return store.values().stream()
                // member의 name과 인자로 받은 name이 같은 경우에만 필터링
                .filter(member -> member.getName().equals(name))
                // 하나라도 찾으면 반환
                .findAny();
                // 못 찾으면 Optional에 null이 포함돼서 반환
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }
}
