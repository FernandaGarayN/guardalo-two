package cl.duoc.mgaray.guardalotwo.service.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Builder
@Getter
@Setter
public class ProductsRequest {
    private Long id;
    private String warehouse;
    private String subsidiary;
    private String address;
    private LocalDate date;
    private Long orderNumber;
    private String trackCode;
    private String transport;
    private Set<RequestDetail> details;
}
