package ru.nikitazhelonkin.coinbalance.data.exception;


public class ApiError extends RuntimeException {

    public ApiError(String message){
        super(message);
    }
}
