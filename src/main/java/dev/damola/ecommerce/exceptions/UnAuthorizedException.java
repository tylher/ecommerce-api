package dev.damola.ecommerce.exceptions;

public class UnAuthorizedException extends RuntimeException {


    public UnAuthorizedException(String msg) {
        super(msg);
    }
}
