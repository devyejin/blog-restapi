package me.yejin.springbootdeveloper;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class JUnitQuiz {
    
    @Test
    public void test() {
        String name1 = "홍길동";
        String name2 = "홍길동";
        String name3 = "홍길은";
        
        //모든 변수가 null이 아닌지 확인
        assertThat(name1).isNotNull();
        assertThat(name2).isNotNull();
        assertThat(name3).isNotNull();

        //name1 , name2가 같은지 확인
        assertThat(name1).isEqualTo(name2);

        //name1, name3 다른지 확인
        assertThat(name1).isNotEqualTo(name3);
    }

    @Test
    public void test2() {
        int number1 = 15;
        int number2 = 0;
        int number3 = -5;

        //num1이 양수인지 확인
        assertThat(number1).isPositive();

        //num2 0인지 확인
        assertThat(number2).isZero();

        //num3 음수인지 확인
        assertThat(number3).isNegative();

        //num1 > num2 ?
        assertThat(number1).isGreaterThan(number2);

        //num3 < num2 ?
        assertThat(number3).isLessThan(number2);
    }

}
