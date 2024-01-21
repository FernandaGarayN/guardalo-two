package cl.duoc.mgaray.guardalotwo.apiclients.musicpro;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MusicProWarehouseProduct {
  private Integer id;
  @JsonProperty("codigo")
  private String code;
  @JsonProperty("nombre")
  private String name;
  @JsonProperty("descripcion")
  private String description;
  @JsonProperty("precio")
  private String price;
  @JsonProperty("precio_raw")
  private Integer rawPrice;
  private String asset;
  @JsonProperty("asset_raw")
  private String assetRaw;
  @JsonProperty("estado")
  private Integer status;
  private Integer stock;
  private Integer cantidad;
}
