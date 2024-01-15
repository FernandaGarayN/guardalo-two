package cl.duoc.mgaray.guardalotwo.service.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class User {
    private Long id;
    private String userName;
    private String password;
    private String subsidiary;
    private String address;
}
