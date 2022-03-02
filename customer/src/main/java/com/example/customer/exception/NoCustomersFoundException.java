package com.example.customer.exception;

public class NoCustomersFoundException extends RuntimeException{

    public NoCustomersFoundException(String str){
        super(str);
    }
}
