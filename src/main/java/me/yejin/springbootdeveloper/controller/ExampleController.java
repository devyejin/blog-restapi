package me.yejin.springbootdeveloper.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ExampleController {

    @GetMapping("/thymeleaf/example")
    public String thymeleafExample(Model model) { // model 객체가 view로 데이터를 넘겨준다
        Person examplePerson = new Person();
        examplePerson.setId(1L);
        examplePerson.setName("홍길동");
        examplePerson.setAge(11);
        examplePerson.setHobbies(List.of("운동", "독서"));

        //model에 데이터를 저장
        model.addAttribute("person", examplePerson);
        model.addAttribute("today", LocalDate.now());

        return "example"; // example.html 뷰

    }

    @Getter
    @Setter
    class Person {
        private Long id;
        private String name;
        private int age;
        private List<String> hobbies;
    }
}


