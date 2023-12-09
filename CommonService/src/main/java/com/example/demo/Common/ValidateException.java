package com.example.demo.Common;

import java.util.Map;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ValidateException extends RuntimeException{
	public final String code;
	public final Map<String, String> exceptionMap;
	public final HttpStatus httpStatus;
	public ValidateException(String code,Map<String,String> message,HttpStatus status){
        this.code = code;
        this.exceptionMap = message;
        this.httpStatus = status;
    }
}
