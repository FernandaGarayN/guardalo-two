package cl.duoc.mgaray.guardalotwo.apiclients.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message){
        super(message);
    }
}
