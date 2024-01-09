package cl.duoc.mgaray.guardalotwo.endpoints.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestPatchProduct {
  @NotBlank
  private String sku;
  @NotBlank
  private String name;
  @NotBlank
  private String description;
  @Min(0)
  private double price;
  @Min(1)
  private int stock;
  private String image;
  private boolean active;
  @Min(0)
  private long version;
}
