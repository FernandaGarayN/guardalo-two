package cl.duoc.mgaray.guardalotwo.endpoints.response;

import cl.duoc.mgaray.guardalotwo.service.domain.RequestDetail;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Set;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseGetRequest {
    private Long id;
    private String warehouse;
    private String subsidiary;
    private String address;
    private LocalDate date;
    private Long orderNumber;
    private String trackCode;
    private String transport;
    private Set<ResponseGetRequestDetail> details;
}
