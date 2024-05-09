package com.ttokttak.jellydiary.user.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserProfileUpdateRequestDto {
    @Pattern(regexp = "^[가-힣\u3131-\u314E\u314F-\u3163a-zA-Z0-9._]*$", message = "이름은 한글, 영문 대소문자, 숫자 및 언더바(_), 점(.)만 사용 가능합니다.")
    @Size(min = 2, max = 15, message = "이름은 2~15자 사이여야 합니다.")
    private String userName;

    @Pattern(regexp = "^[가-힣a-zA-Z0-9\\p{Punct}\\s\\x{1F600}-\\x{1F64F}\\x{1F300}-\\x{1F5FF}\\x{1F680}-\\x{1F6FF}\\x{2600}-\\x{26FF}]*$", message = "한글, 영문 대소문자, 숫자, 특수문자, 이모티콘만 사용 가능합니다.")
    @Size(min = 0, max = 100, message = "설명은 0~100자 사이여야 합니다.")
    private String userDescription;
}
