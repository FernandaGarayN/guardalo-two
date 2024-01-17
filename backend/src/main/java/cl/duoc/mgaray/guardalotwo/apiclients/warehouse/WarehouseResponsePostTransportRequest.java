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
public class WarehouseResponsePostTransportRequest {
  private Integer status;
  private String message;
  @JsonProperty("codigo_seguimiento")
  private String trackCode;
}
