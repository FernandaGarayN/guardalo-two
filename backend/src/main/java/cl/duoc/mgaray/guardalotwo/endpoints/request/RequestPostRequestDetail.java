package cl.duoc.mgaray.guardalotwo.endpoints.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RequestPostRequestDetail {
  @NotNull
  private Long id;
  @NotBlank
  private String sku;
  @NotNull
  private Long quantity;
  @NotBlank
  private String description;
  @NotNull
  private double price;
  @NotBlank
  private String name;

}
