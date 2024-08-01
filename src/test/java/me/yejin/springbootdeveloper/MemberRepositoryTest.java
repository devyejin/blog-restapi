package me.yejin.springbootdeveloper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Sql("/insert-members.sql") // 테스트 실행 전 SQL 스크립트 실행
    @Test
    void getAllMembers() {
        //when
        List<Member> members = memberRepository.findAll();

        //then
        assertThat(members.size()).isEqualTo(3);
    }

    @Sql("/insert-members.sql")
    @Test
    void getMemberById() {
        //when
        Member member = memberRepository.findById(2L).get();

        //then
        assertThat(member.getName()).isEqualTo("B");
    }

    @Sql("/insert-members.sql")
    @Test
    void getMemberByName() {
        //when
        Member member = memberRepository.findByName("C").get();

        //then
        assertThat(member.getId()).isEqualTo(3L);
    }

    @Test
    void saveMember() {
        //given
        Member member = new Member(1L, "A");

        //when
        memberRepository.save(member);

        //then
        assertThat(memberRepository.findById(1L).get().getName()).isEqualTo("A");
    }

    @Test
    void saveMembers() {
        //given
        List<Member> members = List.of(new Member(2L, "B"),
                new Member(3L, "C"));

        //when
        memberRepository.saveAll(members);

        //then
        assertThat(memberRepository.findAll().size()).isEqualTo(2);
    }

    @Sql("/insert-members.sql")
    @Test
    void deleteMemberById() {
        //when
        memberRepository.deleteById(2L);

        //then
        assertThat(memberRepository.findById(2L).isEmpty()).isTrue();
    }

    @Sql("/insert-members.sql")
    @Test
    void deleteMemberAll() {
        //when
        memberRepository.deleteAll(); // 실제 서비스에서는 deleteAll을 사용할 일은 없음 -> 테스트에서 격리성 보장을 위해 사용

        //then
        assertThat(memberRepository.findAll().size()).isZero();

    }

    //테스트 간 격리성을 보장하기 위해 각 테스트 종료 전 테스트 데이터를 삭제한다.
    @AfterEach
    public void cleanUp() {
        System.out.println("cleanUp  수행");
        memberRepository.deleteAll();
    }

    // JPA에서의 수정은 트랜잭션 내에서 데이터를 수정
    // @DataJpaTest는 테스트를 위한 설정을 제공, 자동으로 데이터베이스 트랜잭션 관리, 실제 서비스단에서는 트랜잭션 처리 필요
    @Sql("/insert-members.sql")
    @Test
    void update() {
        //given
        Member member = memberRepository.findById(2L).get();

        //when
        member.changeName("BC");

        //then
        assertThat(memberRepository.findById(2L).get().getName()).isEqualTo("BC");
    }

}