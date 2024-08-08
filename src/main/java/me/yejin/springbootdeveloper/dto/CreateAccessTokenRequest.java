package me.yejin.springbootdeveloper.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
public class CreateAccessTokenRequest {

    private String refreshToken;
}
