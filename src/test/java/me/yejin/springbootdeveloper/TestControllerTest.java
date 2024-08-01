package me.yejin.springbootdeveloper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc // MockMvc 생성 및 자동 구성 (테스트용 MVC환경을 만들어 요청 및 전송, 응답 기능 제공 하는 유틸리티 즉, 컨트롤러 테스트할 때 사용)
@SpringBootTest // 테스트용 애플리케이션 컨텍스트 생성 (스프링 컨테이너에 있는 빈들을 가지고 테스트용 컨텍스트 생성)
class TestControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach //각 테스트 실행 전 실행
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
    }

    @AfterEach
    public void cleanUp() {
        memberRepository.deleteAll();
    }

    @DisplayName("getAllMembers : 아티클 조회에 성공한다.")
    @Test
    public void getAllMembers() throws Exception {
        //given
        final String url = "/test";
        Member savedMember = memberRepository.save(new Member(1L, "홍길동"));

        //when
        final ResultActions result = mockMvc.perform(get(url) // perform() : request역할, 결과로 ResultActions 객체를 받음
                .accept(MediaType.APPLICATION_JSON)); // accept() : 무슨 타입으로 응답을 받을지

        //then
        result // ResultActions 객체는 응답을 검증&확인하는 andExpect() 메서드 제공
                .andExpect(status().isOk()) // 응답코드 200이라 isOk , 404는 isNotFound()
                .andExpect(jsonPath("$[0].id").value(savedMember.getId())) // jsonPath() : JSON 응답값의 값을 가져옴
                .andExpect(jsonPath("$[0].name").value(savedMember.getName()));

    }
}