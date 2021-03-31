package study.datajpa.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

//진짜 JPA 상속관계가 아닌 속성만 받아서 테이블에서 같이 쓸 수 있는..
//MappedSuperclass가 없으면 Member 테이블 생성시 create, updateDate가 없음.
@MappedSuperclass
@Getter @Setter
public class JpaBaseEntity {
    
    //DB value 변경 불가
    @Column(updatable = false)
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    
    //이벤트 발생 전에 persist 동작
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createDate = now;
        updateDate = now; // 처음에 null이면 데이터가 지저분하기 때문 // 어차피 업데이트 시간은 등록되기 때문
    }

    @PreUpdate
    public void preUpdate() {
        updateDate = LocalDateTime.now();
    }

}
