package cl.duoc.mgaray.guardalotwo.service.cmd;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class NewRequestCmd {
    private String subsidiary;
    private String address;
    private List<NewRequestDetailCmd> details;
}
