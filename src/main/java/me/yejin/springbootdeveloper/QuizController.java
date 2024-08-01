package me.yejin.springbootdeveloper;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class QuizController {

    @GetMapping("/quiz")
    public ResponseEntity<String> quiz(@RequestParam("code") int code) {

        switch (code) {
            case 1:
                return ResponseEntity.created(null).body("Created!"); // 응답코드 201
            case 2:
                return ResponseEntity.badRequest().body("Bad Request!"); // 응답코드 400
            default:
                return ResponseEntity.ok().body("OK!"); //응답코드 200
        }
    }

    @PostMapping("/quiz")
    public ResponseEntity<String> quiz2(@RequestBody Code code) {

        switch (code.value()) {
            case 1:
                return ResponseEntity.status(403).body("Forbidden!");
            default:
                return ResponseEntity.ok().body("OK!");
        }
    }
}

record Code(int value) {} // 데이터 전달을 목적으로 하는 객체, 파라미터를 기준으로 필드, 생성자, getter 등 자동 생성
