package cl.duoc.mgaray.guardalotwo.endpoints.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RequestPostRequestDetail {
    @NotBlank
    private String sku;
    @NotNull
    private Long quantity;
}
