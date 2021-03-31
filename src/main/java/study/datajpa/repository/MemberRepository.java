package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

// 스프링 data spa
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom, JpaSpecificationExecutor<Member> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findTop3HelloBy();

    //@Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);

    // 아래 쿼리는 이름이 없는 정적 namedQuery => 애플리케이션 로딩시점에 parsing을 함 => parsing하려고 보니 에러발견함
    @Query("selec" +
            "t m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    List<Member> findListByUsername(String username);//컬렉션

    Member findMemberByUsername(String username); //단건

    Optional<Member> findOptionalByUsername(String username); //단건 Optional

    //페이징
    //count조회의 경우 어차피 totalCount는 외부조인으로 걸릴때나 그냥 join으로 걸릴 때나 동일하기 때문에
    //굳이 성능 낭비를 할 필요가 없다. 따라서 아래와 같이 countQuery를 통해 분리 시킬 수 있다.
    /*@Query(value = "select m from Member m left join m.team t",
                countQuery = "select count(m.username) from Member m")*/
    @Query(value = "select m from Member m")
    Page<Member> findByAge(int age, Pageable pageable);

    //bulk update
    @Modifying(clearAutomatically = true) //없으면 DML operations에 대해 작동을 안함
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    //fetch join Member연관된 team을 한방 쿼리로 가져옴
    //proxy객체가 아닌 진짜 값이 채워져있는 진짜 team 엔티티임
    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    //@EntityGragh(객체간 연관관계 나열)
    //Member와 Team을 한방에 가져오기 위해선
    // 원래는 위와 같이 FetchJoin을 사용해야 하는데
    // JPQL 대신 Spring-Data- JPA에서 제공하는 기능
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    //그냥 fetchjoin만 춫가해주고 싶어 위와 결과 같다.
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    //그냥 단건 회원 조회 할 때도
    //팀 데이터도 쓸 일이 많기 때문에

    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUsername(@Param("username") String username);

    //Entity 자체에 기술
    @EntityGraph("Member.all")
    List<Member> findEntityGraph2ByUsername(@Param("username") String username);

    //JPA HINT
    //내부적으로 읽기전용으로 쓴다고 힌트를 줬기 때문에
    //아무리 set메소드로 데이터를 변경해도 update 쿼리가 안나감
    //암달의 법칙 찾아보기
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    //Lock
    //select for update
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);

    //projections
    //List<UsernameOnly> findProjectionsByUsername(@Param("username") String username);

    //projections-dto
    //List<UsernameOnlyDto> findProjectionsByUsername(@Param("username") String username);

    //projections-generics(동적 프로젝션)
    <T> List<T> findProjectionsByUsername(@Param("username") String username, Class<T> type);

    @Query(value = "select * from member where username = ?", nativeQuery = true)
    Member findByNativeQuery(String username);


    //native query + projections(정적쿼리에서 주로 사용할 듯.. 최근에 지원하게 된거라 실무에서 아직 잘..)
    @Query(value = "select m.member_id as id, m.username, t.name as teamName " +
            "from member m left join team t",
            countQuery = "select count(*) from member",
            nativeQuery = true)
    Page<MemberProjections> findByNativeProjection(Pageable pageable);
}

