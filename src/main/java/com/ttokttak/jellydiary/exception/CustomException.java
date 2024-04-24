package com.ttokttak.jellydiary.exception;

import com.ttokttak.jellydiary.exception.message.ErrorMsg;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {
    private final ErrorMsg errorMsg;
}
