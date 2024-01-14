package cl.duoc.mgaray.guardalotwo.endpoints.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Set;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseGetRequestDetail {
    private String sku;
    private String name;
    private double price;
    private Long quantity;
}
