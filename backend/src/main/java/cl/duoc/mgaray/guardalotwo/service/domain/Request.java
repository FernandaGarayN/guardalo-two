package cl.duoc.mgaray.guardalotwo.service.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Builder
@Getter
@Setter
public class Request {
    private Long id;
    private String subsidiary;
    private String address;
    private LocalDate date;
    private Long orderNumber;
    private Set<RequestDetail> details;
}
