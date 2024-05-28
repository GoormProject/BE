package com.ttokttak.jellydiary.diary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiaryProfileRequestDto {

      @NotBlank(message = "다이어리 이름은 필수 입력 값입니다.")
      @Size(min = 2, max = 15, message = "다이어리 이름은 2자에서 15자 사이여야 합니다.")
      @Pattern(regexp = "^[a-zA-Z0-9가-힣_.\\s]+$",
              message = "다이어리 이름은 영어, 한글, 숫자, '_', '.'만 사용할 수 있습니다.")
      private String diaryName;

      @Size(max = 100, message = "다이어리 설명은 100자 이내로 작성해주세요.")
      private String diaryDescription;
      private String diaryProfileImage;

}
