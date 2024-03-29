package cl.duoc.mgaray.guardalotwo.service.cmd;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SendToTransportCmd {
    private String transport;
    private String destination;
    private String destinationAddress;
    private String orderNumber;
    private String info;
    private String comment;
}
