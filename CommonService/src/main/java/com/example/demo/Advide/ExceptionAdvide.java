package com.example.demo.Advide;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.demo.Common.CommonException;
import com.example.demo.Common.ErrorMessage;
import com.example.demo.Common.ValidateException;

import io.micrometer.core.ipc.http.HttpSender.Response;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ExceptionAdvide {
	
	@ExceptionHandler
	public ResponseEntity<ErrorMessage> handlExpception (CommonException ex){
		return new ResponseEntity(new ErrorMessage(ex.getCode(), ex.getMessage(),ex.getStatus()),ex.getStatus());
	}
	
	@ExceptionHandler
    public ResponseEntity<Map<String,String>> handleValidateException(ValidateException ex){
		
        return new ResponseEntity(ex.getExceptionMap(),ex.getHttpStatus());
    }
}
