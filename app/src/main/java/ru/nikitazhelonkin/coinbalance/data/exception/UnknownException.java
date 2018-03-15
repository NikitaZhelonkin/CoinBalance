package ru.nikitazhelonkin.coinbalance.data.exception;


public class UnknownException extends RuntimeException {

    public UnknownException(){
    }

    public UnknownException(String message){
        super(message);
    }
}
