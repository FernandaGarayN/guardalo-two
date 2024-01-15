package cl.duoc.mgaray.guardalotwo.apiclients.transport;

import cl.duoc.mgaray.guardalotwo.apiclients.exception.BadRequestException;
import cl.duoc.mgaray.guardalotwo.apiclients.exception.ForbiddenException;
import cl.duoc.mgaray.guardalotwo.apiclients.exception.UnauthorizedException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TransportErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultErrorDecoder = new Default();
    @Override
    public Exception decode(String methodKey, Response response) {
        log.error(String.format("Error to call method [%s]: [Status=%d] | [Body=%s]", methodKey, response.status(), response.body()));
        return switch (response.status()) {
            case 400 -> new BadRequestException("invalid request");
            case 401 -> new UnauthorizedException("unauthorized request");
            case 403 -> new ForbiddenException("forbidden request");
            default -> defaultErrorDecoder.decode(methodKey, response);
        };
    }
}
