package com.ttokttak.jellydiary.user.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserNameCheckRequestDto {
    @Pattern(regexp = "^[가-힣\u3131-\u314E\u314F-\u3163a-zA-Z0-9._]{2,15}$", message = "이름은 2~15자의 한글, 영문 대소문자, 숫자 및 언더바(_), 점(.)만 사용 가능합니다.")
    @Size(min = 2, max = 16, message = "이름은 2~15자 사이여야 합니다.")
    private String userName;
}
