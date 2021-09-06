package com.example.demo.student.exception;

public class ApiException extends RuntimeException{
    public ApiException(String message){
        super(message);
    }
}
