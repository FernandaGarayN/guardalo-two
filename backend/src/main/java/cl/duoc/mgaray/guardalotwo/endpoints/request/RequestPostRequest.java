package cl.duoc.mgaray.guardalotwo.endpoints.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class RequestPostRequest {
    @NotBlank
    private String warehouse;
    @NotBlank
    private String subsidiary;
    @NotBlank
    private String address;
    @NotEmpty
    private List<@Valid RequestPostRequestDetail> details;
}
