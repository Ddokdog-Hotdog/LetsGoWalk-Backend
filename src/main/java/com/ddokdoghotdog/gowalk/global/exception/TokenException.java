package com.ddokdoghotdog.gowalk.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenException extends RuntimeException {
    private final ErrorCode errorCode;

}
