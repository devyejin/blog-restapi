package me.yejin.springbootdeveloper;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class QuizControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper; //Jackson 라이브러리 제공, 클래스 <-> JSON 객체 (직렬화, 역직렬화)

    @BeforeEach
    public void mockMvcSetup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @DisplayName("quiz() : GET/quiz=?code=1 이면 응답 코드는 201, 응답 바디는 Created!를 리턴한다")
    @Test
    public void getQuiz1() throws Exception {
        //given
        final String url = "/quiz";

        //when
        final ResultActions result = mockMvc.perform(get(url).param("code", "1"));

        //then
        result
                .andExpect(status().isCreated())
                .andExpect(content().string("Created!"));

    }

    @DisplayName("quiz() : GET/quiz=?code=2 이면 응답 코드는 400, 응답 바디는 Bad Request!를 리턴한다")
    @Test
    public void getQuiz2() throws Exception {
        //given
        final String url = "/quiz";

        //when
        final ResultActions result = mockMvc.perform(get(url).param("code", "2"));

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request!"));
    }

    @DisplayName("quiz() : POST/quiz?code=1 이면 응답 코드는 403, 응답 바디는 Forbidden!를 리턴한다")
    @Test
    public void postQuiz1() throws Exception {
        //given
        final String url = "/quiz";

        //when
//        final ResultActions result = mockMvc.perform(post(url).param("code", "1"));
        //post요청이니, body에 JSON타입으로 보내기
        final ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Code(1))));

        //then
        result
                .andExpect(status().isForbidden())
                .andExpect(content().string("Forbidden!"));
    }

    @DisplayName("quiz() : POST/quiz?code=3 이면 응답코드는 200, 응답 바디는 OK!를 리턴한다")
    @Test
    public void postQuiz2() throws Exception {
        //given
        final String url = "/quiz";

        //when
        final ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Code(3))));

        //then
        result
                .andExpect(status().isOk())
                .andExpect(content().string("OK!"));
    }

}