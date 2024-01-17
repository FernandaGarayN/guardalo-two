package cl.duoc.mgaray.guardalotwo.service.cmd;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RequestStatusCmd {
    private String trackCode;
    private String transport;
}
