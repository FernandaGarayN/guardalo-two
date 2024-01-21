package cl.duoc.mgaray.guardalotwo.endpoints.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RequestPostTransportRequest {
  @NotNull
  private Long orderNumber;
  @NotBlank
  private String transport;
}
