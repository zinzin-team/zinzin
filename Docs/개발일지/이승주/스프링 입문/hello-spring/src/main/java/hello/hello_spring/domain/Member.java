package hello.hello_spring.domain;

// JPA를 사용하려면 entity 매핑 필요
// jpa는 표준 인터페이스, 구현은 따로
// ORM(Object Relational Mmapping)
import jakarta.persistence.*;

@Entity
public class Member {
    // id 매핑 + 알아서 자동으로 생성
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    @Column(name = "name")
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
