package me.yejin.springbootdeveloper;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JUnitCycleQuiz {

    @Test
    public void test() {
        System.out.println("This is first test");
    }

    @Test
    public void test2() {
        System.out.println("This is second test");
    }

    //각 테스트 시작 전에, hello 모든 테스트를 끝마치고 bye를 출력
    @BeforeEach
    public void beforeEach() {
        System.out.println("Hello!");
    }

    @AfterAll
    public static void afterAll() {
        System.out.println("Bye!");
    }
}
