package cl.duoc.mgaray.guardalotwo.service.exception;

public class InvalidLogin extends RuntimeException{
    public InvalidLogin(String message){
        super(message);
    }
}
