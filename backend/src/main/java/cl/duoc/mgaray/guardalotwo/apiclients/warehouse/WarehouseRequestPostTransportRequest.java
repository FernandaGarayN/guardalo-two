package cl.duoc.mgaray.guardalotwo.apiclients.warehouse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseRequestPostTransportRequest {
  @JsonProperty("nombre_origen")
  private String originName;
  @JsonProperty("direccion_origen")
  private String originAddress;
  @JsonProperty("nombre_destino")
  private String destinationName;
  @JsonProperty("direccion_destino")
  private String destinationAddress;
  @JsonProperty("comentario")
  private String comment;
  private String info;
}
