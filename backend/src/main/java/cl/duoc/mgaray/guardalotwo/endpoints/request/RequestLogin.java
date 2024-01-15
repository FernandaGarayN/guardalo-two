package cl.duoc.mgaray.guardalotwo.endpoints.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RequestLogin {
    private String userName;
    private String password;
}
