package cl.duoc.mgaray.guardalotwo.apiclients.exception;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message){
        super(message);
    }
}
