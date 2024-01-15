package cl.duoc.mgaray.guardalotwo.apiclients.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message){
        super(message);
    }
}
