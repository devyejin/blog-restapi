package me.yejin.springbootdeveloper;

import org.junit.jupiter.api.*;

public class JUnitCycleTest {

    @BeforeAll // 전체 테스트 시작 전 1회만 실행, static 메서드로 선언, ex) DB연결, 테스트환경 초기화
    static void beforeAll() {
        System.out.println("@BeforeAll");
    }

    @BeforeEach // 테스트 케이스 시작하기 전마다 실행 ex) 테스트 메서드에서 사용하는 객체 초기화, 테스트에 필요한 데이터 값 넣을 때 사용
    public void beforeEach() {
        System.out.println("@BeforeEach");
    }

    @Test
    public void test1() {
        System.out.println("test1");
    }

    @Test
    public void test2() {
        System.out.println("test2");
    }
    @Test
    public void test3() {
        System.out.println("test3");
    }

    @AfterAll // 전체 테스트 종료 전 1회 실행, static 메서드로 선언 ex) DB 연결 종료, 공통적으로 사용하는 자원 해제
    static void afterAll() {
        System.out.println("@AfterAll");
    }

    @AfterEach // 테스트 케이스 종료하기 전마다 실행 ex) 테스트 이후 특정 데이터 삭제
    public void afterEach() {
        System.out.println("@AfterEach");
    }

}
