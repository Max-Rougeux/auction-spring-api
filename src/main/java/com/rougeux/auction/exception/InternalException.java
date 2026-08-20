package com.rougeux.auction.exception;

import lombok.Getter;

@Getter
public class InternalException extends RuntimeException {
    private final String code;

    public InternalException(String code, String message) {
        super(message);
        this.code = code;
    }
}