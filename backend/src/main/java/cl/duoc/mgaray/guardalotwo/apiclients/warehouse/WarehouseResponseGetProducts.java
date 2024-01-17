package cl.duoc.mgaray.guardalotwo.apiclients.warehouse;

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
public class WarehouseResponseGetProducts {
  private String message;
  @JsonProperty("productos")
  private List<WarehouseProduct> products;
}
