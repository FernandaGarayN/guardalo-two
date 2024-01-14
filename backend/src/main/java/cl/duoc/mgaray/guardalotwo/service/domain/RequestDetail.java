package cl.duoc.mgaray.guardalotwo.service.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class RequestDetail {
    private String sku;
    private String name;
    private double price;
    private Long quantity;
}
