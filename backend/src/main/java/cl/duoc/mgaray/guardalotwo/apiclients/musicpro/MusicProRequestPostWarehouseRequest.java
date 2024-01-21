package cl.duoc.mgaray.guardalotwo.apiclients.musicpro;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MusicProRequestPostWarehouseRequest {
  @JsonProperty("nombre_empresa")
  private String enterpriseName;
  @JsonProperty("direccion_empresa")
  private String enterpriseAddress;
  @JsonProperty("productos")
  private List<Product> products;

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Product {
    @JsonProperty("id_producto")
    private String productId;
    @JsonProperty("cantidad")
    private Long quantity;
  }

}
